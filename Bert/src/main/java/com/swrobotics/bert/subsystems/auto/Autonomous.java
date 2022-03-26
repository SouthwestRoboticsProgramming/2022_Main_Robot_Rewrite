package com.swrobotics.bert.subsystems.auto;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.commands.auto.AutonomousSequence;
import com.swrobotics.bert.commands.auto.BackupAutonomousSequence;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;
import com.swrobotics.messenger.client.MessengerClient;

public class Autonomous implements Subsystem {
    private final SwerveDriveController drive;
    private final MessengerClient msg;
    private final Pathfinding path;

    private Command autoCommand;

    public Autonomous(SwerveDriveController drive, MessengerClient msg, Pathfinding path) {
        this.drive = drive;
        this.msg = msg;
        this.path = path;
    }

    @Override
    public void autonomousInit() {
        if (msg != null) {
            autoCommand = new AutonomousSequence(msg, path);
        } else {
            autoCommand = new BackupAutonomousSequence(drive);
        }

        Scheduler.get().addCommand(autoCommand);
    }

    @Override
    public void disabledInit() {
        if (autoCommand != null) {
            Scheduler.get().cancelCommand(autoCommand);
        }
    }

    @Override
    public void teleopInit() {
        disabledInit();
    }
}
