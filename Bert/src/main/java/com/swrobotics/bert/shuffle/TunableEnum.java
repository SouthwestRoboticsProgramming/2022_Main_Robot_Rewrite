package com.swrobotics.bert.shuffle;

import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class TunableEnum<T extends Enum<T>> extends ShuffleBoardTunable<T> {
    private final T defaultValue;
    private final Class<T> type;

    public TunableEnum(ShuffleboardLayout layout, String name, Class<T> type, T defaultValue) {
        super(layout.addPersistent(name, defaultValue.toString()).getEntry());
        this.defaultValue = defaultValue;
        this.type = type;

        entry.addListener((event) -> {
            fireOnChanged();
        }, EntryListenerFlags.kNew | EntryListenerFlags.kUpdate);
    }

    @Override
    public T get() {
        try {
            return Enum.valueOf(type, entry.getString(defaultValue.toString()));
        } catch (IllegalArgumentException e) {
            return defaultValue;
        }
    }

    @Override
    public void set(T t) {
        entry.setString(t.toString());
    }
}
