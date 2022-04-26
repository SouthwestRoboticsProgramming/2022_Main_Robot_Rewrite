package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.WaitCommand;
import com.swrobotics.bert.commands.shooter.ShootCommand;

public final class OneBallAuto extends CommandSequence {
    public OneBallAuto(RobotContainer robot) {
        append(new TaxiSequence(robot));
        append(new TurnTowardsTargetCommand(robot.driveController, robot.localization));
        append(new WaitCommand(1));
        append(new ShootCommand(robot.hopper, robot.input));
    }
}
