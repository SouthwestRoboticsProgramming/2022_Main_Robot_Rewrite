package com.swrobotics.bert.subsystems.climber;

import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;

public final class ClimberController implements Subsystem {
    private final Input input;

    public ClimberController(Input input) {
        this.input = input;
    }

    @Override
    public void teleopPeriodic() {

    }
}
