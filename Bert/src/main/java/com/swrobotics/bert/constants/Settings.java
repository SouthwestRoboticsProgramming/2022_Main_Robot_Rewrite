package com.swrobotics.bert.constants;

import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TunableBoolean;
import com.swrobotics.bert.shuffle.TuneGroup;

public final class Settings {
    private static final TuneGroup PROFILER = new TuneGroup("Profiler", ShuffleBoard.settingsTab);
        public static final TunableBoolean DUMP_PROFILE_DATA = PROFILER.getBoolean("Dump Profile Data", false);

    private static final TuneGroup LOCALIZATION = new TuneGroup("Localization", ShuffleBoard.settingsTab);
        public static final TunableBoolean USE_LIMELIGHT = LOCALIZATION.getBoolean("Use Limelight", true);


    private Settings() {
        throw new AssertionError();
    }
}
