package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;
import com.swrobotics.bert.util.Utils;

import static com.swrobotics.bert.constants.AutonomousConstants.*;

public final class TurnToAngleCommand implements Command {
    private final SwerveDriveController drive;
    private final Localization loc;
    private final double angle;

    public TurnToAngleCommand(SwerveDriveController drive, Localization loc, double angle) {
        this.drive = drive;
        this.loc = loc;
        this.angle = angle;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        drive.turnToAngle(angle);

        return Utils.checkTolerance(loc.getFieldRot().getDegrees(), TURN_THRESHOLD.get());
    }

    @Override
    public void end() {

    }
}
