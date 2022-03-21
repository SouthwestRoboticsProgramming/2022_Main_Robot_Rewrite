package com.swrobotics.fieldviewer.overlay;

import com.swrobotics.fieldviewer.Conversions;
import com.swrobotics.fieldviewer.FieldViewer;
import com.swrobotics.messenger.client.MessengerClient;
import processing.core.PApplet;

public final class LocalizationOverlay implements FieldOverlay {
    private final float ROBOT_SIZE = Conversions.inchesToMeters(25);
    private final float ARROW_SIZE = ROBOT_SIZE / 3.0f;

    private final String MESSAGE = "RoboRIO:Location";

    private float posX, posY;
    private float posRot;

    public LocalizationOverlay(MessengerClient msg) {
        posX = 0;
        posY = 0;
        posRot = 0;

        msg.makeHandler()
                .listen(MESSAGE)
                .setHandler((type, in) -> {
                    posX = (float) in.readDouble();
                    posY = (float) in.readDouble();
                    posRot = (float) in.readDouble();
                });
    }

    @Override
    public void draw(FieldViewer p) {
        p.pushMatrix();

        p.noFill();
        p.strokeWeight(3);
        p.stroke(0, 0, 255);

        p.translate(posX, posY);
        p.rotate(-PApplet.radians(posRot));

        p.rect(0, 0, ROBOT_SIZE, ROBOT_SIZE);

        p.line(0, -ARROW_SIZE, 0, ARROW_SIZE);
        p.line(-ARROW_SIZE, 0, 0, ARROW_SIZE);
        p.line(0, ARROW_SIZE, ARROW_SIZE, 0);

        p.popMatrix();
    }
}
