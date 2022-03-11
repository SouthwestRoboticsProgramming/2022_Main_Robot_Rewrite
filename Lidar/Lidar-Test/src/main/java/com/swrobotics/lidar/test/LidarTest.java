package com.swrobotics.lidar.test;

import com.swrobotics.lidar.library.Lidar;
import com.swrobotics.lidar.library.ScanEntry;
import processing.core.PApplet;
import processing.core.PFont;

import java.util.HashSet;
import java.util.Set;

public final class LidarTest extends PApplet {
    private Lidar lidar;
    private ScanEntry lastEntry = new ScanEntry(0, 0, 0);
    private Set<ScanEntry> scan, incomingScan;

    @Override
    public void settings() {
        fullScreen(P2D);
    }

    @Override
    public void setup() {
        frameRate(240);

        scan = new HashSet<>();
        incomingScan = new HashSet<>();

        lidar = new Lidar();

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        println("Lidar health: " + lidar.getHealth().join());
        println("Lidar info: " + lidar.getInfo().join());

        lidar.setScanStartCallback(() -> {
            scan = incomingScan;
            incomingScan = new HashSet<>();
        });
        lidar.setScanDataCallback((entry) -> {
            if (entry.getQuality() == 0 || entry.getDistance() == 0)
                return;

            lastEntry = entry;
            incomingScan.add(entry);
        });

        lidar.startScanning();
    }

    @Override
    public void draw() {
        background(0);

        fill(255);
        noStroke();
        text(frameRate + " FPS", 10, 20);

        translate(width / 2f, height / 2f);

        strokeWeight(2);
        stroke(128, 128, 255);

        float lx = (float) lastEntry.getDistance() * cos(radians((float) lastEntry.getAngle()));
        float ly = (float) lastEntry.getDistance() * sin(radians((float) lastEntry.getAngle()));
        line(0, 0, lx, ly);

        strokeWeight(4);
        stroke(255, 0, 0);

        beginShape(POINTS);
        for (ScanEntry entry : scan) {
            float x = (float) entry.getDistance() * cos(radians((float) entry.getAngle()));
            float y = (float) entry.getDistance() * sin(radians((float) entry.getAngle()));

            vertex(x, y);
        }
        endShape();
    }

    @Override
    public void keyPressed() {
        lidar.stopScanning();
        lidar.close();

        exit();
    }

    public static void main(String[] args) {
        PApplet.main(LidarTest.class);
    }
}
