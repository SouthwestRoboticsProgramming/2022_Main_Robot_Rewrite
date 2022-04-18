package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableBoolean;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class CameraConstants {

    public static final double LIMELIGHT_MOUNT_HEIGHT = 0.6604 + 0.2; // Meters
    public static final double LIMELIGHT_MOUNT_ANGLE = 15.95; // Degrees
    public static final double TARGET_HEIGHT = 2.6416 - 0.4; // Meters

    private static final TuneGroup CAMERAS = new TuneGroup("Cameras", ShuffleBoard.cameraTab);
        public static final TunableDouble LOOKING_AT_TARGET_THRESH = CAMERAS.getDouble("Looking at Target Threshold", 100);

    private static final TuneGroup LIGHTS = new TuneGroup("Lights", ShuffleBoard.settingsTab);
        public static final TunableBoolean RING_LIGHTS = LIGHTS.getBoolean("Ring Lights", true);
        public static final TunableBoolean LIMELIGHT_LIGHTS = LIGHTS.getBoolean("Limelight lights", true);
        

    private CameraConstants() {
        throw new AssertionError();
    }
}
