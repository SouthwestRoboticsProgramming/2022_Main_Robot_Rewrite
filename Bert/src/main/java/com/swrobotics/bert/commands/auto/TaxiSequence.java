package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.auto.Pathfinding;
import com.swrobotics.messenger.client.MessengerClient;

import edu.wpi.first.math.geometry.Rotation2d;

public final class TaxiSequence extends CommandSequence {
    public TaxiSequence(RobotContainer robot) {
        Localization loc = robot.localization;
        MessengerClient msg = robot.msg;
        Pathfinding path = robot.pathfinding;

        double taxiDistance = 2;

        Rotation2d angleToTarget = loc.getAngleToTarget();
        double targetX = -taxiDistance * angleToTarget.getCos();
        double targetY = -taxiDistance * angleToTarget.getSin();

        append(new DriveToPointCommand(msg, path, targetX, targetY, 15));
    }
}
