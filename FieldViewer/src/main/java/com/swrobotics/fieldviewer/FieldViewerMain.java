package com.swrobotics.fieldviewer;

import processing.core.PApplet;

public final class FieldViewerMain {
    public static void main(String[] args) {
        PApplet.main(FieldViewer.class);
    }

    private FieldViewerMain() {
        throw new AssertionError();
    }
}
