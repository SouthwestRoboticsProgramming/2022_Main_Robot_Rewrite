package com.swrobotics.fieldviewer.overlay;

import com.swrobotics.fieldviewer.FieldViewer;
import com.swrobotics.messenger.client.MessengerClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class LidarOverlay implements FieldOverlay {
    private static final String IN_READY = "Lidar:Ready";
    private static final String IN_SCAN_START = "Lidar:ScanStart";
    private static final String IN_SCAN = "Lidar:Scan";

    private static final String OUT_START = "Lidar:Start";
    private static final String OUT_STOP = "Lidar:Stop";

    private static class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }

    private final MessengerClient msg;

    private List<Point> scan;
    private List<Point> incomingScan;

    public LidarOverlay(MessengerClient msg) {
        this.msg = msg;
        msg.makeHandler()
                .listen(IN_READY)
                .listen(IN_SCAN_START)
                .listen(IN_SCAN)
                .setHandler(this::onMessage);

        scan = new ArrayList<>();
        incomingScan = new ArrayList<>();
    }

    private void onMessage(String type, DataInputStream in) throws IOException {
        switch (type) {
            case IN_READY: {
                msg.sendMessage(OUT_START);
                break;
            }
            case IN_SCAN_START: {
                scan = incomingScan;
                incomingScan = new ArrayList<>();
                break;
            }
            case IN_SCAN: {
                int quality = in.readInt();
                double x = in.readDouble();
                double y = in.readDouble();

                incomingScan.add(new Point((float) -y, (float) x));
            }
        }
    }

    @Override
    public void draw(FieldViewer p) {
        p.strokeWeight(6);
        p.stroke(255, 0, 0);

        for (Point point : scan) {
            p.point(point.x, point.y);
        }
    }
}
