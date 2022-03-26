package com.swrobotics.bert.commands.intake;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.intake.Intake;
import com.swrobotics.bert.subsystems.shooter.Hopper;

import static com.swrobotics.bert.constants.Constants.PERIODIC_PER_SECOND;
import static com.swrobotics.bert.constants.IntakeConstants.*;
import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class IntakeEjectCommand implements Command {
    private final Intake intake;
    private final Hopper hopper;
    private int timer;

    public IntakeEjectCommand(Intake intake, Hopper hopper) {
        this.intake = intake;
        this.hopper = hopper;
        this.timer = (int) (EJECT_TIME.get() * PERIODIC_PER_SECOND);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        intake.setState(Intake.State.EJECT);
        hopper.setIndexSpeed(INDEX_EJECT_SPEED.get());

        return --timer <= 0;
    }

    @Override
    public void end() {
        intake.setState(Intake.State.OFF);
        hopper.setIndexSpeed(0);
    }
}
