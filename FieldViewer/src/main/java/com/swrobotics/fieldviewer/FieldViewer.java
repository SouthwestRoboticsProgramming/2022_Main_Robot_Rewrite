package com.swrobotics.fieldviewer;

import com.swrobotics.fieldviewer.overlay.FieldOverlay;
import com.swrobotics.fieldviewer.overlay.LidarOverlay;
import com.swrobotics.fieldviewer.overlay.LocalizationOverlay;
import com.swrobotics.fieldviewer.overlay.PathfindingOverlay;
import com.swrobotics.messenger.client.MessengerClient;
import processing.core.PApplet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public final class FieldViewer extends PApplet {
    public static final float FIELD_WIDTH = Conversions.feetToMeters(27);
    public static final float FIELD_HEIGHT = Conversions.feetToMeters(54);

    public static final int PADDING = 75;
    public static final float PIXELS_PER_METER = 50;

    private MessengerClient msg;
    private List<FieldOverlay> overlays;

    @Override
    public void strokeWeight(float weight) {
        super.strokeWeight(weight / PIXELS_PER_METER);
    }

    @Override
    public void settings() {
        size(
                (int) Math.ceil(FIELD_WIDTH * PIXELS_PER_METER) + PADDING * 2,
                (int) Math.ceil(FIELD_HEIGHT * PIXELS_PER_METER) + PADDING * 2,
                P2D
        );
    }

    @Override
    public void setup() {
        rectMode(CENTER);
        ellipseMode(CENTER);

        try {
            msg = new MessengerClient("10.21.29.3", 5805, "FieldViewer");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        overlays = new ArrayList<>();
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
        translate(FIELD_WIDTH / 2, -FIELD_HEIGHT / 2);

        // Field border
        strokeWeight(1);
        stroke(255);
        rect(0, 0, FIELD_WIDTH, FIELD_HEIGHT);

        for (FieldOverlay overlay : overlays) {
            overlay.draw(this);
        }

        float fieldX = (mouseX - PADDING) / PIXELS_PER_METER - FIELD_WIDTH / 2;
        float fieldY = (mouseY - PADDING) / -PIXELS_PER_METER + FIELD_HEIGHT / 2;

        strokeWeight(10);
        stroke(255);
        point(fieldX, fieldY);

        msg.builder("RoboRIO:Location")
                .addDouble(0)
                .addDouble(0)
                .addDouble(millis() / 1000.0 * 45)
                .send();
    }

    @Override
    public void mousePressed() {
        float fieldX = (mouseX - PADDING) / PIXELS_PER_METER - FIELD_WIDTH / 2;
        float fieldY = (mouseY - PADDING) / -PIXELS_PER_METER + FIELD_HEIGHT / 2;

        if (mouseButton == LEFT) {
            msg.builder("Pathfinder:SetTarget")
                    .addDouble(fieldY)
                    .addDouble(-fieldX)
                    .send();
        } else if (mouseButton == RIGHT) {
            msg.builder("Pathfinder:SetPosition")
                    .addDouble(fieldY)
                    .addDouble(-fieldX)
                    .send();
        }
    }

    @Override
    public void mouseDragged() {
        mousePressed();
    }
}
