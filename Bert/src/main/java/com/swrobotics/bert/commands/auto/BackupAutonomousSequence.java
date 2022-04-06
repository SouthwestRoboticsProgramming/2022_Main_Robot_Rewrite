package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

// Auto sequence used if Messenger connection fails
public final class BackupAutonomousSequence extends CommandSequence {
    public BackupAutonomousSequence(RobotContainer robot) {
        SwerveDriveController drive = robot.driveController;

        append(new DriveForTimeCommand(drive, 0, -1, 2));
    }
}
