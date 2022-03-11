package com.swrobotics.lidar.library;

public final class LidarInfo {
    private final int modelID;
    private final int firmwareMinor;
    private final int firmwareMajor;
    private final int hardwareVersion;
    private final long serialNumberLow;
    private final long serialNumberHigh;

    public LidarInfo(int modelID, int firmwareMinor, int firmwareMajor, int hardwareVersion, long serialNumberLow, long serialNumberHigh) {
        this.modelID = modelID;
        this.firmwareMinor = firmwareMinor;
        this.firmwareMajor = firmwareMajor;
        this.hardwareVersion = hardwareVersion;
        this.serialNumberLow = serialNumberLow;
        this.serialNumberHigh = serialNumberHigh;
    }

    public int getModelID() {
        return modelID;
    }

    public int getFirmwareMinor() {
        return firmwareMinor;
    }

    public int getFirmwareMajor() {
        return firmwareMajor;
    }

    public int getHardwareVersion() {
        return hardwareVersion;
    }

    public long getSerialNumberLow() {
        return serialNumberLow;
    }

    public long getSerialNumberHigh() {
        return serialNumberHigh;
    }

    @Override
    public String toString() {
        return "LidarInfo{" +
                "modelID=" + modelID +
                ", firmwareMinor=" + firmwareMinor +
                ", firmwareMajor=" + firmwareMajor +
                ", hardwareVersion=" + hardwareVersion +
                ", serialNumberLow=" + serialNumberLow +
                ", serialNumberHigh=" + serialNumberHigh +
                '}';
    }
}
