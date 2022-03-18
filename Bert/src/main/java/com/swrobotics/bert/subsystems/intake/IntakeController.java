package com.swrobotics.bert.subsystems.intake;

import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;

public class IntakeController implements Subsystem {
    private final Input input;
    private final Intake intake;

    public IntakeController(Input input, Intake intake) {
        this.input = input;
        this.intake = intake;
    }

    @Override
    public void teleopPeriodic() {
        if (input.getToggleIntake()) {
            intake.setRunning(!intake.isRunning());
        }
    }
}