package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

import static com.swrobotics.bert.constants.Constants.*;

public final class DriveForTimeCommand implements Command {
    private final SwerveDriveController drive;
    private final double speedX;
    private final double speedY;
    private int timer;

    public DriveForTimeCommand(SwerveDriveController drive, double speedX, double speedY, double seconds) {
        this.drive = drive;
        this.speedX = speedX;
        this.speedY = speedY;
        timer = (int) (seconds * PERIODIC_PER_SECOND);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        drive.drive(speedX, speedY);

        return --timer <= 0;
    }

    @Override
    public void end() {

    }
}
