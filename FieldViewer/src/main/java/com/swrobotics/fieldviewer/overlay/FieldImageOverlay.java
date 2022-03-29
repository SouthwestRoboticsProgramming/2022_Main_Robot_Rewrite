package com.swrobotics.fieldviewer.overlay;

import com.swrobotics.fieldviewer.FieldViewer;
import processing.core.PApplet;
import processing.core.PImage;

public final class FieldImageOverlay implements FieldOverlay {
    private final PImage image;

    public FieldImageOverlay(PApplet applet) {
        image = applet.loadImage("field-image-2.jpg");
    }

    @Override
    public void draw(FieldViewer p) {
        p.pushMatrix();
        float scale = FieldViewer.FIELD_HEIGHT / image.width;
        p.scale(-scale, scale);

        p.rotate((float) Math.PI / 2f);
        p.image(image, -image.width / 2f, -image.height / 2f);

        p.popMatrix();
    }
}
