package com.swrobotics.bert.subsystems.intake;

import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;

public final class IntakeController implements Subsystem {
    private final Input input;
    private final Intake intake;

    public IntakeController(Input input, Intake intake) {
        this.input = input;
        this.intake = intake;
    }

    @Override
    public void teleopPeriodic() {
        if (input.getToggleIntake()) {
            // intake.setState(intake.getState() != Intake.State.OFF ? Intake.State.ON : Intake.State.OFF);
            if (intake.getState() == Intake.State.OFF) { 
                intake.setState(Intake.State.ON);} else {
                    intake.setState(Intake.State.OFF);
                }
        }
    }
}
