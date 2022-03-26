package com.swrobotics.bert.subsystems.climber;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.climber.ResetClimberCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.subsystems.climber.rotating.RotatingArms;
import com.swrobotics.bert.subsystems.climber.telescoping.TelescopingArms;

public final class Climber implements Subsystem {
    private final TelescopingArms telescoping;
    private final RotatingArms rotating;
    private final Input input;

    public Climber(Input input) {
        telescoping = new TelescopingArms();
        rotating = new RotatingArms();
        this.input = input;
    }

    public void setTargetState(ClimberState state) {
        telescoping.setTargetDistancePercent(state.getTelescopingDistance());
        telescoping.setLoaded(state.isLoaded());
        rotating.setTargetAngleDegrees(state.getRotatingAngle());
    }

    public void manualMove(double teleMove, double rotMove) {
        telescoping.manualMove(teleMove);
        rotating.manualMove(rotMove);
    }

    public void zero() {
        telescoping.zero();
        rotating.zero();
    }

    public boolean isInTolarence() {
        return telescoping.isInTolarence() && rotating.isInTolarence();
    }

    @Override
    public void teleopInit() {
        Scheduler.get().addCommand(new ResetClimberCommand(this, input));
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
