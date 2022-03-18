package com.swrobotics.bert.shuffle;

import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;

public final class ShuffleBoard {
    public static final ShuffleboardTab driveTab = Shuffleboard.getTab("Drive");
    public static final ShuffleboardTab intakeTab = Shuffleboard.getTab("Intake");
    public static final ShuffleboardTab climberTab = Shuffleboard.getTab("Climber");
    public static final ShuffleboardTab inputTab = Shuffleboard.getTab("Input");
    public static final ShuffleboardTab settingsTab = Shuffleboard.getTab("Settings");
}
