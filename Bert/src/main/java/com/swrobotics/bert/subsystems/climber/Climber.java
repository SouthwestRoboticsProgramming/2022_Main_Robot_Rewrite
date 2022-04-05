package com.swrobotics.bert.subsystems.climber;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.climber.ResetClimberCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.subsystems.climber.rotating.RotatingArms;
import com.swrobotics.bert.subsystems.climber.telescoping.TelescopingArms;
import com.kauailabs.navx.frc.AHRS;

public final class Climber implements Subsystem {
    private final TelescopingArms telescoping;
    private final RotatingArms rotating;
    private final Input input;
    private final AHRS gyro;

    public Climber(Input input, AHRS gyro) {
        telescoping = new TelescopingArms();
        rotating = new RotatingArms();
        this.input = input;
        this.gyro = gyro;
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
        return telescoping.isInTolarence() && rotating.isInTolerance();
    }

    public TelescopingArms getTeles() {
        return telescoping;
    }

    public RotatingArms getRots() {
        return rotating;
    }

    @Override
    public void teleopInit() {
        Scheduler.get().addCommand(new ResetClimberCommand(this, input, gyro));
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
