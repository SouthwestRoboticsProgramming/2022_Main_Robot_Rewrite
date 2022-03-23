package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.climber.Climber;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class ClimberSequence extends CommandSequence {
    private final Input input;

    public ClimberSequence(Climber climber, Input input) {
        this.input = input;

        // append(new ClimberStep(tele, rot, loaded));
        append(new ClimberStep(climber, CLIMB_STEP_1_TELE.get(), CLIMB_STEP_1_ROT.get(), false)); // Arms up
        append(new ClimberStep(climber, CLIMB_STEP_2_TELE.get(), CLIMB_STEP_1_ROT.get(), true)); // Pull up
        append(new ClimberStep(climber, CLIMB_STEP_2_TELE.get(), CLIMB_STEP_3_ROT.get(), true)); // Snap in
        append(new ClimberStep(climber, CLIMB_STEP_4_TELE.get(), CLIMB_STEP_3_ROT.get(), true)); // Release tele
        append(new ClimberStep(climber, CLIMB_STEP_4_TELE.get(), CLIMB_STEP_5_ROT.get(), true)); // Big swing
        append(new ClimberStep(climber, CLIMB_STEP_6_TELE.get(), CLIMB_STEP_5_ROT.get(), true)); // Extend to third
        append(new ClimberStep(climber, CLIMB_STEP_6_TELE.get(), CLIMB_STEP_7_ROT.get(), true)); // Third bar compression

        // TODO: What happens after compression?

        // Repeat

        // TODO: Finish steps
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
