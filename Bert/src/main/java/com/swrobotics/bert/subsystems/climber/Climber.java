package com.swrobotics.bert.subsystems.climber;

import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.subsystems.climber.rotating.RotatingArms;
import com.swrobotics.bert.subsystems.climber.telescoping.TelescopingArms;

public final class Climber implements Subsystem {
    private final TelescopingArms telescoping;
    private final RotatingArms rotating;

    public Climber() {
        telescoping = new TelescopingArms();
        rotating = new RotatingArms();
    }

    public void setTargetState(ClimberState state) {
        telescoping.setTargetDistancePercent(state.getTelescopingDistance());
        rotating.setTargetAngleDegrees(state.getRotatingAngle());
    }

    @Override
    public void robotPeriodic() {
        telescoping.update();
        rotating.update();
    }

    @Override
    public String toString() {
        return "Climber{"
            + "telescoping=" + telescoping + ","
            + "rotating=" + rotating
            + "}";
    }
}
