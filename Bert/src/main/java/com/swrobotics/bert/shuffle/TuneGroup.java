package com.swrobotics.bert.shuffle;

import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardContainer;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public final class TuneGroup {
    private final ShuffleboardLayout layout;

    public TuneGroup(String name, ShuffleboardContainer parent) {
        layout = parent.getLayout(name, BuiltInLayouts.kList);
    }

    public TuneGroup(String name, TuneGroup parent) {
        layout = parent.layout.getLayout(name);
    }

    public ShuffleboardLayout getLayout() {
        return layout;
    }

    public TunableDouble getDouble(String name, double defaultVal) {
        return new TunableDouble(layout, name, defaultVal);
    }

    public TunableInt getInt(String name, int defaultVal) {
        return new TunableInt(layout, name, defaultVal);
    }

    public TunableBoolean getBoolean(String name, boolean defaultVal) {
        return new TunableBoolean(layout, name, defaultVal);
    }

    public TunableDoubleArray getDoubleArray(String name, double... defaultVals) {
        return new TunableDoubleArray(layout, name, defaultVals);
    }

    public <T extends Enum<T>> TunableEnum<T> getEnum(String name, Class<T> type, T defaultVal) {
        return new TunableEnum<T>(layout, name, type, defaultVal);
    }
}
