package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.climber.Climber;

public final class ClimberSequence extends CommandSequence {
    private final Input input;

    public ClimberSequence(Climber climber, Input input) {
        this.input = input;

        // append(new ClimberStep(tele, rot, loaded));
    }

    @Override
    public boolean run() {
        if (input.getClimberNextStep()) {
            next();
        }
        if (input.getClimberPreviousStep()) {
            back();
        }

        return super.run();
    }
}
