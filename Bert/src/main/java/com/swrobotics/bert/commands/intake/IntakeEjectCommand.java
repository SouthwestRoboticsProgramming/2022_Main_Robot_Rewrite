  package com.swrobotics.bert.commands.intake;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.WaitCommand;
import com.swrobotics.bert.commands.shooter.IndexCommand;
import com.swrobotics.bert.subsystems.intake.Intake;
import com.swrobotics.bert.subsystems.intake.Intake.State;
import com.swrobotics.bert.subsystems.shooter.Hopper;

import static com.swrobotics.bert.constants.IntakeConstants.*;
import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class IntakeEjectCommand extends CommandSequence {
    public IntakeEjectCommand(Intake intake, Hopper hopper) {
        State savedState = intake.getState();
        if (savedState == State.EJECT) {
            savedState = State.OFF;
        }

        append(new IntakeSetCommand(intake, State.EJECT));
        append(new WaitCommand(0.5));
        append(new IndexCommand(hopper, INDEX_EJECT_SPEED.get(), EJECT_TIME.get()));
        append(new IntakeSetCommand(intake, savedState));
    }
}
