package com.swrobotics.fieldviewer.overlay;

import com.swrobotics.fieldviewer.FieldViewer;
import com.swrobotics.messenger.client.MessengerClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.*;

public final class LidarOverlay implements FieldOverlay {
    private static final String IN_READY = "Lidar:Ready";
    private static final String IN_SCAN_START = "Lidar:ScanStart";
    private static final String IN_SCAN = "Lidar:Scan";

    private static final String OUT_START = "Lidar:Start";
    private static final String OUT_STOP = "Lidar:Stop";

    private static final int PERSIST_TIME = 1000; // milliseconds

    private static class Point {
        public float x;
        public float y;
        public long timestamp;

        public Point(float x, float y, long timestamp) {
            this.x = x;
            this.y = y;
            this.timestamp = timestamp;
        }
    }

    private final MessengerClient msg;

    private final Set<Point> scan;

    public LidarOverlay(MessengerClient msg) {
        this.msg = msg;
        msg.makeHandler()
                .listen(IN_READY)
                .listen(IN_SCAN_START)
                .listen(IN_SCAN)
                .setHandler(this::onMessage);

        scan = Collections.synchronizedSet(new HashSet<>());
    }

    private void onMessage(String type, DataInputStream in) throws IOException {
        switch (type) {
            case IN_READY: {
                msg.sendMessage(OUT_START);
                break;
            }
            case IN_SCAN_START: {
                break;
            }
            case IN_SCAN: {
                int quality = in.readInt();
                double x = in.readDouble();
                double y = in.readDouble();

                scan.add(new Point((float) x, (float) y, System.currentTimeMillis()));
            }
        }
    }

    @Override
    public void draw(FieldViewer p) {
        p.strokeWeight(6);

        for (Iterator<Point> iter = scan.iterator(); iter.hasNext(); ) {
            Point point = iter.next();

            long age = System.currentTimeMillis() - point.timestamp;
            if (age >= PERSIST_TIME) {
                iter.remove();
            }
        }

        List<Point> sorted = new ArrayList<>(scan);
        sorted.sort(Comparator.comparingLong((point) -> point.timestamp));

        for (Point point : sorted) {
            long age = System.currentTimeMillis() - point.timestamp;
            p.stroke((1 - (float) age / PERSIST_TIME) * 255, 0, 0);
            p.point(point.x, point.y);
//            p.line(0, 0, point.x, point.y);
        }
    }
}
