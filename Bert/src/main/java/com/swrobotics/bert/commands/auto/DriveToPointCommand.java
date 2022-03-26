package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.commands.auto.path.Point;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

public class DriveToPointCommand implements Command {
    private final SwerveDriveController drive;
    private final Localization loc;
    private final Point target;

    public DriveToPointCommand(SwerveDriveController drive, Localization loc, Point target) {
        this.drive = drive;
        this.loc = loc;
        this.target = target;
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
        double locX = loc.getFieldX();
        double locY = loc.getFieldY();

        double deltaX = target.getX() - locX;
        double deltaY = target.getY() - locY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX /= distance;
        deltaY /= distance;

        deltaX *= 0.25;
        deltaY *= 0.25;

        return atTarget();
    }

    @Override
    public void end() {

    }
}
