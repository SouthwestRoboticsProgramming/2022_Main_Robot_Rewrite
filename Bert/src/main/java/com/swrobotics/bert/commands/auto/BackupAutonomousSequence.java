package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

// Auto sequence used if Messenger connection fails
public final class BackupAutonomousSequence extends CommandSequence {
    public BackupAutonomousSequence(SwerveDriveController drive) {
        append(new DriveForTimeCommand(drive, 0, -1, 1));
    }
}
