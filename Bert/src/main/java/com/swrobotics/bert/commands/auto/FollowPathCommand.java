package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.commands.auto.path.Point;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

import java.util.List;

public final class FollowPathCommand implements Command {
    private final SwerveDriveController drive;
    private final Localization loc;
    private final List<Point> path;
    private int targetIndex;
    private Point target;

    public FollowPathCommand(SwerveDriveController drive, Localization loc, List<Point> path) {
        this.drive = drive;
        this.loc = loc;
        this.path = path;

        targetIndex = 0;
    }

    private boolean atTarget() {
        double deltaX = target.getX() - loc.getFieldX();
        double deltaY = target.getY() - loc.getFieldY();
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);

        return distance < 0.20;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        if (atTarget()) {
            targetIndex++;
            if (targetIndex >= path.size()) {
                return true;
            }
            target = path.get(targetIndex);
        }

        double locX = loc.getFieldX();
        double locY = loc.getFieldY();

        double deltaX = target.getX() - locX;
        double deltaY = target.getY() - locY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX /= distance;
        deltaY /= distance;

        deltaX *= 0.25;
        deltaY *= 0.25;

        // System.out.println(deltaX + " " + deltaY);
        drive.drive(deltaX, deltaY);

        return false;
    }

    @Override
    public void end() {

    }
}
