package com.swrobotics.bert.shuffle;

import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.wpilibj.shuffleboard.BuiltInLayouts;
import edu.wpi.first.wpilibj.shuffleboard.Shuffleboard;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardTab;
import edu.wpi.first.wpilibj.shuffleboard.ShuffleboardLayout;

public class ShuffleBoard {
    public static ShuffleboardTab swerveTab = Shuffleboard.getTab("Swerve");
        private static ShuffleboardLayout swerveHardware = swerveTab.getLayout("Swerve Hardware", BuiltInLayouts.kList);
            public static NetworkTableEntry frontLeftModule = swerveHardware.add("Front Left Module", 1).getEntry();
            public static NetworkTableEntry frontRightModule = swerveHardware.add("Front Right Module", 2).getEntry();
            public static NetworkTableEntry backRightModule = swerveHardware.add("Back Right Module", 3).getEntry();
            public static NetworkTableEntry backLeftModule = swerveHardware.add("Back Left Module", 4).getEntry();
}
