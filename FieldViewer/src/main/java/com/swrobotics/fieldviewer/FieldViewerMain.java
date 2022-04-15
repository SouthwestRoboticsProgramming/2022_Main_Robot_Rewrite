package com.swrobotics.fieldviewer;

import processing.core.PApplet;

public final class FieldViewerMain {
    public static void main(String[] args) {
        FieldViewer.host = args[0];
        FieldViewer.port = Integer.parseInt(args[1]);

        PApplet.main(FieldViewer.class);
    }

    private FieldViewerMain() {
        throw new AssertionError();
    }
}
