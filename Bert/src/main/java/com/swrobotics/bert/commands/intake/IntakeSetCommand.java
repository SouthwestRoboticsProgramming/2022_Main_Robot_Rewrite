package com.swrobotics.bert.commands.intake;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.intake.Intake;

public final class IntakeSetCommand implements Command {
    private final Intake intake;
    private final Intake.State state;

    public IntakeSetCommand(Intake intake, Intake.State state) {
        this.intake = intake;
        this.state = state;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        intake.setState(state);
        return true;
    }

    @Override
    public void end() {

    }
}
