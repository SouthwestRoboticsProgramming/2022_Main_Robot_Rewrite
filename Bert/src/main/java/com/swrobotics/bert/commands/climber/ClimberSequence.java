package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.climber.Climber;
import com.kauailabs.navx.frc.AHRS;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class ClimberSequence extends CommandSequence {
    private final Input input;
    private final Climber climber;
    private final AHRS gyro;

    public ClimberSequence(Climber climber, Input input, AHRS gyro) {
        this.input = input;
        this.climber = climber;
        this.gyro = gyro;

        // append(new ClimberStep(tele, rot, loaded));
        append(new ClimberStep(input, climber, CLIMB_STEP_1_TELE, CLIMB_STEP_1_ROT, false)); // 1:Base
        append(new ClimberStep(input, climber, CLIMB_STEP_1_5_TELE, CLIMB_STEP_1_ROT, false)); // 1.5:Arms up
        append(new ClimberStep(input, climber, CLIMB_STEP_2_TELE, CLIMB_STEP_1_ROT, true)); // 2:Pull up
        append(new ClimberStep(input, climber, CLIMB_STEP_2_TELE, CLIMB_STEP_3_ROT, true)); // 3:Lock in
        append(new ClimberStep(input, climber, CLIMB_STEP_4_TELE, CLIMB_STEP_4_ROT, false)); // 4:Handoff
        append(new WaitForAngle(gyro, CLIMB_STEP_4_5_GYRO));
        append(new ClimberStep(input, climber, CLIMB_STEP_5_TELE, CLIMB_STEP_4_ROT, false)); // 5:Extend to 3
        append(new ClimberStep(input, climber, CLIMB_STEP_5_TELE, CLIMB_STEP_6_ROT, false)); // 6:Pressure on 3
        append(new ClimberStep(input, climber, CLIMB_STEP_2_TELE, CLIMB_STEP_1_ROT, true)); // 2:Pull Up
        append(new ClimberStep(input, climber, CLIMB_STEP_2_TELE, CLIMB_STEP_3_ROT, true)); // 3:Lock in
        append(new ClimberStep(input, climber, CLIMB_STEP_4_TELE, CLIMB_STEP_4_ROT, false)); // 4:Handoff
        append(new WaitForAngle(gyro, CLIMB_STEP_4_5_GYRO));
        append(new ClimberStep(input, climber, CLIMB_STEP_5_TELE, CLIMB_STEP_4_ROT, false)); // 5:Extend to 3
        append(new ClimberStep(input, climber, CLIMB_STEP_5_TELE, CLIMB_STEP_6_ROT, false)); // 6:Pressure on 3
        append(new ClimberStep(input, climber, CLIMB_STEP_2_TELE, CLIMB_STEP_1_ROT, true)); // 2:Pull Up
        append(new ClimberStep(input, climber, CLIMB_STEP_2_TELE, CLIMB_STEP_3_ROT, true)); // 3:Lock in
        append(new ClimberStep(input, climber, CLIMB_STEP_4_TELE, CLIMB_STEP_4_ROT, false)); // 4:Handoff
        append(new ClimberStep(input, climber, CLIMB_STEP_1_5_TELE, CLIMB_STEP_4_ROT, false)); // 1.5:Arms up
        // append(new ClimberStep(climber, CLIMB_STEP_6_TELE.get(), CLIMB_STEP_7_ROT.get(), true)); // 7:Pull up to 3

        // TODO: What happens after compression?

        // Repeat

        // TODO: Finish steps
    }

    @Override
    public boolean run() {
        if (input.getClimberNextStep() && climber.isInTolarence()) {
            next();
        }
        if (input.getClimberPreviousStep()) {
            back();
        }

        return super.run();
    }
}
