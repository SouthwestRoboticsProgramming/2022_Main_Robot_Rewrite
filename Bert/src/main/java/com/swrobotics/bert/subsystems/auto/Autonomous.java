package com.swrobotics.bert.subsystems.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.commands.auto.AutonomousSequence;
import com.swrobotics.bert.commands.auto.BackupAutonomousSequence;
import com.swrobotics.bert.subsystems.Subsystem;

public final class Autonomous implements Subsystem {
    private final RobotContainer robot;

    private Command autoCommand;

    public Autonomous(RobotContainer robot) {
        this.robot = robot;
    }

    @Override
    public void autonomousInit() {
        if (robot.msg != null) {
            autoCommand = new AutonomousSequence(robot);
        } else {
            autoCommand = new BackupAutonomousSequence(robot);
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
