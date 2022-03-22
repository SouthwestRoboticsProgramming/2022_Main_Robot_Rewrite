package com.swrobotics.bert.shuffle;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

import java.util.LinkedHashMap;
import java.util.Map;

public final class ShuffleBoard {
    public static final ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
    public static final ShuffleboardTab intakeTab = Shuffleboard.getTab("Intake");
    public static final ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");
    public static final ShuffleboardTab inputTab = Shuffleboard.getTab("Input");
    public static final ShuffleboardTab settingsTab = Shuffleboard.getTab("Settings");
    public static final ShuffleboardTab shooterTab = Shuffleboard.getTab("Shooter");

    public static final ShuffleboardTab statusTab = Shuffleboard.getTab("Status");
        private static final ShuffleboardLayout valueDisplay = statusTab.getLayout("valueDisplay", BuiltInLayouts.kList);
            private static final NetworkTableEntry valueDisplayEntry = valueDisplay.add("Value Display", "").getEntry();

    private static final Map<String, Object> values = new LinkedHashMap<>();

    public static void show(String name, Object value) {
        values.put(name, value);
        updateValueDisplay();
    }

    private static void updateValueDisplay() {
        StringBuilder builder = new StringBuilder();

        for (Map.Entry<String, Object> entry : values.entrySet()) {
            builder.append(entry.getKey());
            builder.append(": ");
            builder.append(entry.getValue());
            builder.append("\n");
        }

        valueDisplayEntry.setString(builder.toString());
    }
}
