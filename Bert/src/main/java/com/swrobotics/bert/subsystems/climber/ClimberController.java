package com.swrobotics.bert.subsystems.climber;

import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;

public final class ClimberController implements Subsystem {
    private final Input input;
    private final Climber climber;

    public ClimberController(Input input, Climber climber) {
        this.input = input;
        this.climber = climber;
    }

    @Override
    public void teleopPeriodic() {
        if (input.getClimberManualOverride()) {
            climber.manualMove(input.getTeleManual(), input.getRotManual());
        } else {
            double telescopingDistance = input.getTeleDistance();
            double rotatingAngle = input.getRotAngle();
            boolean loaded = false;

            ClimberState state = new ClimberState(telescopingDistance, rotatingAngle, loaded);

            climber.setTargetState(state);
        }
    }
}
