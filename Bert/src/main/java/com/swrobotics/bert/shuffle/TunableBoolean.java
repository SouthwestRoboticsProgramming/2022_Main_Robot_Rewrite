package com.swrobotics.bert.shuffle;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class TunableBoolean extends ShuffleBoardTunable<Boolean> {
    private final boolean defaultValue;

    public TunableBoolean(ShuffleboardLayout layout, String name, boolean defaultValue) {
        super(layout.addPersistent(name, defaultValue).getEntry());
        this.defaultValue = defaultValue;

        entry.addListener((event) -> {
            fireOnChanged();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    @Override
    public void set(Boolean value) {
        entry.setBoolean(value);
    }

    @Override
    public Boolean get() {
        return entry.getBoolean(defaultValue);
    }
}
