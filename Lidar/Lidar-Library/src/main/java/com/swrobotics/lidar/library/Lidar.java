package com.swrobotics.lidar.library;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortInvalidPortException;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public final class Lidar implements SerialPortDataListener {
    private static final String MODEL_NUM = "CP2102";

    // Start flags
    private static final byte START_FLAG = (byte) 0xA5;
    private static final byte START_FLAG_2 = (byte) 0x5A;

    // Request IDs
    private static final byte REQUEST_SCAN = 0x20;
    private static final byte REQUEST_STOP = 0x25;
    private static final byte REQUEST_RESET = 0x40;
    private static final byte REQUEST_GET_INFO = 0x50;
    private static final byte REQUEST_GET_HEALTH = 0x52;

    // Response IDs
    private static final byte RESPONSE_INFO = 0x04;
    private static final byte RESPONSE_HEALTH = 0x06;
    private static final byte RESPONSE_SCAN = (byte) 0x81;

    // Time to wait for timed busy
    private static final long BUSY_TIME = 2;

    // Serial connection to lidar
    private final SerialPort port;

    // Future storage
    private final Set<CompletableFuture<LidarInfo>> infoFutures;
    private final Set<CompletableFuture<LidarHealth>> healthFutures;

    // Callbacks
    private ScanStartCallback scanStartCallback = () -> {};
    private ScanDataCallback scanDataCallback = (entry) -> {};

    // Busy state storage
    enum BusyState { NOT_BUSY, BUSY_UNTIL_RESPONSE, BUSY_FOR_TIME }
    private long busyBegin;
    private BusyState busy;

    // Response reading
    enum ReadState { READ_DESCRIPTOR, READ_RESPONSE, NO_READ }
    private ReadState readState;
    private byte expectedResponse;
    private byte[] readBuffer = null;
    private int readIndex = 0;

    public static Set<SerialPort> findAvailableLidars() {
        Set<SerialPort> ports = new HashSet<>();

        for (SerialPort port : SerialPort.getCommPorts()) {
            String desc = port.getPortDescription();
            if (desc.contains(MODEL_NUM)) {
                ports.add(port);
            }
        }

        return ports;
    }

    public Lidar() {
        this(getDefaultPort());
    }

    public Lidar(String portName) {
        this(getPort(portName));
    }

    public Lidar(SerialPort port) {
        checkPort(port);
        System.out.println("Starting lidar on port " + port.getSystemPortName());
        this.port = port;

        infoFutures = new HashSet<>();
        healthFutures = new HashSet<>();

        busy = BusyState.NOT_BUSY;
        readState = ReadState.NO_READ;

        port.setBaudRate(115200);
        port.setParity(SerialPort.NO_PARITY);
        port.setNumStopBits(1);
        port.setNumDataBits(8);
        if (!port.openPort()) {
            throw new RuntimeException("Failed to open serial port");
        }
        port.addDataListener(this);
    }

    public CompletableFuture<LidarInfo> getInfo() {
        if (isBusy()) {
            throw new LidarBusyException();
        }

        CompletableFuture<LidarInfo> future = new CompletableFuture<>();
        infoFutures.add(future);

        sendRequest(REQUEST_GET_INFO, null);
        busy = BusyState.BUSY_UNTIL_RESPONSE;

        readBuffer = new byte[7];
        readState = ReadState.READ_DESCRIPTOR;

        return future;
    }

    public CompletableFuture<LidarHealth> getHealth() {
        if (isBusy()) {
            throw new LidarBusyException();
        }

        CompletableFuture<LidarHealth> future = new CompletableFuture<>();
        healthFutures.add(future);

        sendRequest(REQUEST_GET_HEALTH, null);
        busy = BusyState.BUSY_UNTIL_RESPONSE;

        readBuffer = new byte[7];
        readState = ReadState.READ_DESCRIPTOR;

        return future;
    }

    public void startScanning() {
        if (isBusy()) {
            throw new LidarBusyException();
        }

        port.clearDTR(); // Start motor
        sendRequest(REQUEST_SCAN, null);
        beginTimedBusy();

        readBuffer = new byte[7];
        readState = ReadState.READ_DESCRIPTOR;
    }

    public void stopScanning() {
        sendRequest(REQUEST_STOP, null);
        port.setDTR(); // Stop motor
        beginTimedBusy();
    }

    public void setScanStartCallback(ScanStartCallback callback) {
        this.scanStartCallback = callback;
    }

    public void setScanDataCallback(ScanDataCallback callback) {
        this.scanDataCallback = callback;
    }

    public void reset() {
        sendRequest(REQUEST_RESET, null);
        beginTimedBusy();
    }

    public boolean isBusy() {
        if (busy == BusyState.BUSY_FOR_TIME) {
            long now = System.currentTimeMillis();

            if (now - busyBegin > BUSY_TIME) {
                busy = BusyState.NOT_BUSY;
            }
        }

        return busy != BusyState.NOT_BUSY;
    }

    public void close() {
        port.closePort();
    }

    private static SerialPort getDefaultPort() {
        Set<SerialPort> ports = findAvailableLidars();
        if (ports.isEmpty()) {
            throw new NoSuchLidarPortException("No lidar ports found");
        }
        return ports.iterator().next();
    }

    private static SerialPort getPort(String name) {
        SerialPort port;
        try {
            port = SerialPort.getCommPort(name);
        } catch (SerialPortInvalidPortException e) {
            throw new NoSuchLidarPortException("Port not found: '" + name + "'");
        }

        return port;
    }

    private static void checkPort(SerialPort port) {
        if (!port.getPortDescription().contains(MODEL_NUM)) {
            throw new InvalidLidarPortException("Device on port '" + port.getSystemPortName() + "' is not a lidar");
        }
    }

    private void sendRequest(byte id, byte[] payload) {
        byte[] requestData = new byte[payload == null ? 2 : (payload.length + 4)];

        requestData[0] = START_FLAG;
        requestData[1] = id;

        if (payload != null) {
            if (payload.length > 255) {
                throw new IllegalArgumentException("Payload too large");
            }

            byte payloadLength = (byte) payload.length;
            requestData[2] = payloadLength;

            System.arraycopy(payload, 0, requestData, 3, payload.length);

            byte checksum = 0;
            for (int i = 0; i < requestData.length - 1; i++) {
                checksum ^= requestData[i];
            }
            requestData[requestData.length - 1] = checksum;
        }

        int written = port.writeBytes(requestData, requestData.length);
        if (written != requestData.length) {
            throw new RuntimeException("Failed to write all data: " + written);
        }
    }

    private void beginTimedBusy() {
        busy = BusyState.BUSY_FOR_TIME;
        busyBegin = System.currentTimeMillis();
    }

    private void readResponseInfo() {
        check(readBuffer.length == 20, "Read buffer incorrect size: " + readBuffer.length);

        int modelId       = readBuffer[0] & 0xFF;
        int firmwareMinor = readBuffer[1] & 0xFF;
        int firmwareMajor = readBuffer[2] & 0xFF;
        int hardwareVer   = readBuffer[3] & 0xFF;
        long serialNumberLow = 0;
        long serialNumberHigh = 0;
        for (int i = 0; i < 8; i++) {
            serialNumberLow  |= (long) (readBuffer[i +  4] & 0xFF) << (i * 8);
            serialNumberHigh |= (long) (readBuffer[i + 12] & 0xFF) << (i * 8);
        }

        LidarInfo info = new LidarInfo(
                modelId,
                firmwareMinor, firmwareMajor,
                hardwareVer,
                serialNumberLow, serialNumberHigh
        );

        for (CompletableFuture<LidarInfo> future : infoFutures) {
            future.complete(info);
        }
        infoFutures.clear();

        readState = ReadState.NO_READ;
        readBuffer = null;
    }

    private void readResponseHealth() {
        check(readBuffer.length == 3, "Read buffer incorrect size: " + readBuffer.length);

        int status = readBuffer[0] & 0xFF;
        LidarHealth health;

        switch (status) {
            case 0:
                health = LidarHealth.GOOD;
                break;
            case 1:
                health = LidarHealth.WARNING;
                break;
            case 2:
                health = LidarHealth.ERROR;
                break;
            default:
                throw new RuntimeException("Unknown lidar status ID: " + status);
        }

        for (CompletableFuture<LidarHealth> future : healthFutures) {
            future.complete(health);
        }
        healthFutures.clear();

        readState = ReadState.NO_READ;
        readBuffer = null;
    }

    private void readResponseScan() {
        check(readBuffer.length == 5, "Read buffer incorrect size: " + readBuffer.length);

        check((readBuffer[1] & 0x01) == 1, "Check bit 2 incorrect");
        check(((readBuffer[0] & 0x02) ^ (readBuffer[0] & 0x01)) == 1, "Check bit 1 incorrect");

        boolean isStart = (readBuffer[0] & 0x01) != 0;

        int quality = (readBuffer[0] & 0xFC) >> 2;

        int angleFixed = (readBuffer[1] & 0xFE) >> 2;
        angleFixed |= (readBuffer[2] & 0xFF) << 7;
        double angle = angleFixed / 64.0;

        int distanceFixed = (readBuffer[3] & 0xFF) | ((readBuffer[4] & 0xFF) << 8);
        double distance = distanceFixed / 4.0;

        if (isStart) {
            try {
                scanStartCallback.call();
            } catch (Throwable e) {
                System.err.println("Exception in scan start callback:");
                e.printStackTrace();
            }
        }

        ScanEntry entry = new ScanEntry(quality, angle, distance);
        try {
            scanDataCallback.call(entry);
        } catch (Throwable e) {
            System.err.println("Exception in scan data callback:");
            e.printStackTrace();
        }
    }

    private void onReadBufferFilled() {
        switch (readState) {
            case NO_READ:
                break;
            case READ_DESCRIPTOR: {
                check(readBuffer.length == 7, "Read buffer incorrect size: " + readBuffer.length);
                check(readBuffer[0] == START_FLAG, "Start flag 1 incorrect");
                check(readBuffer[1] == START_FLAG_2, "Start flag 2 incorrect");

                int responseLength =
                        (readBuffer[2] & 0xFF) |
                        (readBuffer[3] & 0xFF) << 8 |
                        (readBuffer[4] & 0xFF) << 16 |
                        (readBuffer[5] & 0x3F) << 24;

                expectedResponse = readBuffer[6];

                readState = ReadState.READ_RESPONSE;
                readBuffer = new byte[responseLength];

                break;
            }
            case READ_RESPONSE: {
                switch (expectedResponse) {
                    case RESPONSE_INFO:
                        readResponseInfo();
                        break;
                    case RESPONSE_HEALTH:
                        readResponseHealth();
                        break;
                    case RESPONSE_SCAN:
                        readResponseScan();
                        break;
                    default:
                        throw new RuntimeException("Unexpected response of id: " + expectedResponse);
                }

                if (busy == BusyState.BUSY_UNTIL_RESPONSE) {
                    busy = BusyState.NOT_BUSY;
                }

                break;
            }
            default:
                throw new IllegalStateException("Invalid read state: " + readState);
        }
    }

    private void check(boolean val, String message) {
//        if (!val) {
//            throw new RuntimeException("Check failed: " + message);
//        }
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    private void checkedSerialEvent() {
        if (readBuffer == null) {
            return;
        }

        if (readState == ReadState.NO_READ) {
            byte[] buffer = new byte[1024];
            int available;
            while ((available = port.bytesAvailable()) > 0) {
                port.readBytes(buffer, Math.min(available, 1024));
            }
        }

        while (port.bytesAvailable() > 0) {
            byte[] buffer = new byte[readBuffer.length - readIndex];
            int read = port.readBytes(buffer, buffer.length);

            if (read > 0) {
                System.arraycopy(buffer, 0, readBuffer, readIndex, read);
                readIndex += read;

                if (readIndex == readBuffer.length) {
                    onReadBufferFilled();
                    readIndex = 0;
                }
            }
        }
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        try {
            checkedSerialEvent();
        } catch (Throwable t) {
            t.printStackTrace(System.err);
        }
    }
}
