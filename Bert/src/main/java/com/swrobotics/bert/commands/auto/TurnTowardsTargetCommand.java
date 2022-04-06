package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

public final class TurnTowardsTargetCommand extends TurnToAngleCommand {
    public TurnTowardsTargetCommand(SwerveDriveController drive, Localization loc) {
        super(drive, loc, loc.getAngleToTarget().getDegrees());
    }

    @Override
    public boolean run() {
        angle = loc.getAngleToTarget().getDegrees();
        return super.run();
    }
}
