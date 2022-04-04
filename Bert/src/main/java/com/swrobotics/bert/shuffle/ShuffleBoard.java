package com.swrobotics.bert.shuffle;

import java.util.HashMap;
import java.util.Map;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public final class ShuffleBoard {
    public static final ShuffleboardTab driveTab     = Shuffleboard.getTab("Drive");
    public static final ShuffleboardTab intakeTab    = Shuffleboard.getTab("Intake");
    public static final ShuffleboardTab climberTab   = Shuffleboard.getTab("Climber");
    public static final ShuffleboardTab inputTab     = Shuffleboard.getTab("Input");
    public static final ShuffleboardTab settingsTab  = Shuffleboard.getTab("Settings");
    public static final ShuffleboardTab shooterTab   = Shuffleboard.getTab("Shooter");
    public static final ShuffleboardTab autoTab      = Shuffleboard.getTab("Auto");
    public static final ShuffleboardTab cameraTab    = Shuffleboard.getTab("Camera");
    public static final ShuffleboardTab subsystemTab = Shuffleboard.getTab("Subsystems");

    public static final ShuffleboardTab statusTab = Shuffleboard.getTab("Status");
        private static final ShuffleboardLayout valueDisplay = statusTab.getLayout("valueDisplay", BuiltInLayouts.kList);

    private static final Map<String, NetworkTableEntry> valueDisplayEntries = new HashMap<>();

    public static void show(String key, Object value) {
        String str = String.valueOf(value);

        NetworkTableEntry entry = valueDisplayEntries.computeIfAbsent(key, (k) -> valueDisplay.add(k, "").getEntry());
        entry.setString(str);
    }

    public static void showBoolean(String key, boolean value) {
        NetworkTableEntry entry = valueDisplayEntries.computeIfAbsent(key, (k) -> valueDisplay.add(k, false).getEntry());
        entry.setBoolean(value);
    }
}
