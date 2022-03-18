package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.subsystems.climber.Climber;

public final class ClimberSequence extends CommandSequence {
    public ClimberSequence(Climber climber) {
        // append(new ClimberStep(tele, rot, loaded));
    }

    @Override
    public boolean run() {
        // Check inputs

        return super.run();
    }
}
