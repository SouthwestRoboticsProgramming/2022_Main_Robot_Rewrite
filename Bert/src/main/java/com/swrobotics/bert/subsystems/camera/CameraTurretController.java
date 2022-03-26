package com.swrobotics.bert.subsystems.camera;

import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;

public final class CameraTurretController implements Subsystem {
    private final Input input;
    private final CameraTurret turret;

    public CameraTurretController(Input input, CameraTurret turret) {
        this.input = input;
        this.turret = turret;
    }

    @Override
    public void teleopPeriodic() {
        turret.turnTo(input.getServoAngle());
    }
}
