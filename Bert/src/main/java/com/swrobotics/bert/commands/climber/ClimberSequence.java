package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.subsystems.climber.Climber;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class ClimberSequence extends CommandSequence {
    public ClimberSequence(Climber climber) {
        // append(new ClimberStep(tele, rot, loaded));
        append(new ClimberStep(climber, CLIMB_STEP_1_TELE.get(), CLIMB_STEP_1_ROT.get(), false));
        append(new ClimberStep(climber, CLIMB_STEP_2_TELE.get(), CLIMB_STEP_1_ROT.get(), true));
        append(new ClimberStep(climber, CLIMB_STEP_2_TELE.get(), CLIMB_STEP_3_ROT.get(), true));
    }

    @Override
    public boolean run() {
        // Check inputs

        return super.run();
    }
}
