package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableBoolean;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class Settings {
    private static final TuneGroup PROFILER = new TuneGroup("Profiler", ShuffleBoard.settingsTab);
        public static final TunableBoolean DUMP_PROFILE_DATA = PROFILER.getBoolean("Dump Profile Data", false);

    private static final TuneGroup LIGHTS = new TuneGroup("Lights", ShuffleBoard.settingsTab);
        public static final TunableBoolean RING_LIGHTS = LIGHTS.getBoolean("Ring Lights", true);

    private Settings() {
        throw new AssertionError();
    }
}
