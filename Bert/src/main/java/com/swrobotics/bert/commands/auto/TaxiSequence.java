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

        double taxiDistance = 3;

        double locX = loc.getFieldX();
        double locY = loc.getFieldY();
        double len = Math.sqrt(locX * locX + locY * locY);
        locX /= len;
        locY /= len;
        locX *= taxiDistance;
        locY *= taxiDistance;

        append(new DriveToPointCommand(msg, path, locX, locY, 7));
    }
}
