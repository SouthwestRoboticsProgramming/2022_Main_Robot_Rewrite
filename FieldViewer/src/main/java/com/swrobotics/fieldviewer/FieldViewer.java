package com.swrobotics.fieldviewer;

import com.swrobotics.fieldviewer.overlay.*;
import com.swrobotics.messenger.client.MessengerClient;
import processing.core.PApplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class FieldViewer extends PApplet {
    public static final float FIELD_WIDTH = Conversions.feetToMeters(27);
    public static final float FIELD_HEIGHT = Conversions.feetToMeters(54);

    public static final int PADDING = 75;
    public static float PIXELS_PER_METER = 80;

    private static final float STROKE_MULTIPLIER = 1.25f;

    private MessengerClient msg;
    private List<FieldOverlay> overlays;

    @Override
    public void strokeWeight(float weight) {
        super.strokeWeight(weight / PIXELS_PER_METER * STROKE_MULTIPLIER);
    }

    @Override
    public void settings() {
        size(
                (int) Math.ceil(FIELD_HEIGHT * PIXELS_PER_METER) + PADDING * 2,
                (int) Math.ceil(FIELD_WIDTH * PIXELS_PER_METER) + PADDING * 2,
                P2D
        );
    }

    @Override
    public void setup() {
        rectMode(CENTER);
        ellipseMode(CENTER);

        try {
            msg = new MessengerClient("localhost", 5805, "FieldViewer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        overlays = new ArrayList<>();
        overlays.add(new FieldImageOverlay(this));
        overlays.add(new LidarOverlay(msg));
        overlays.add(new PathfindingOverlay(msg));
        overlays.add(new LocalizationOverlay(msg));
    }

    @Override
    public void draw() {
        msg.readMessages();
        background(32);

        translate(PADDING, PADDING);
        scale(PIXELS_PER_METER, -PIXELS_PER_METER);
        translate(FIELD_HEIGHT / 2, -FIELD_WIDTH / 2);
        rotate(-HALF_PI);

        // Field border
        strokeWeight(1);
        stroke(255);
        rect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);

        for (FieldOverlay overlay : overlays) {
            overlay.draw(this);
        }

        float fieldX = (mouseY - height / 2f) / PIXELS_PER_METER;
        float fieldY = (mouseX - width / 2f) / PIXELS_PER_METER;

        strokeWeight(10);
        stroke(255);
        point(fieldX, fieldY);

        msg.builder("RoboRIO:Location")
                .addDouble(rx)
                .addDouble(ry)
                .addDouble(0)
                .send();
    }

    private double rx, ry;

    @Override
    public void mousePressed() {
        float fieldX = (mouseY - height / 2f) / PIXELS_PER_METER;
        float fieldY = (mouseX - width / 2f) / PIXELS_PER_METER;

        if (mouseButton == LEFT) {
            msg.builder("Pathfinder:SetTarget")
                    .addDouble(fieldX)
                    .addDouble(fieldY)
                    .send();
        } else if (mouseButton == RIGHT) {
            rx = fieldX;
            ry = fieldY;
        }
    }

    @Override
    public void mouseDragged() {
        mousePressed();
    }
}
