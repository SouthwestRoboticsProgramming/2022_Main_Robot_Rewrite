package com.swrobotics.bert.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonFXBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import static com.swrobotics.bert.constants.ShooterConstants.*;
import static com.swrobotics.bert.constants.Constants.*;

public final class Flywheel implements Subsystem {

    private final TalonFX flywheel;
    private State state;

    public Flywheel() {
        flywheel = new TalonFXBuilder(FLYWHEEL_ID)
                .setCANBus(CANIVORE)
                .setPIDF(
                        FLYWHEEL_KP.get(),
                        FLYWHEEL_KI.get(),
                        FLYWHEEL_KD.get(),
                        FLYWHEEL_KF.get()
                )
                .build();


        FLYWHEEL_KP.onChange(this::updatePID);
        FLYWHEEL_KI.onChange(this::updatePID);
        FLYWHEEL_KD.onChange(this::updatePID);
        FLYWHEEL_KF.onChange(this::updatePID);

        state = State.ON;
    }

    private void updatePID() {
        flywheel.config_kP(0, FLYWHEEL_KP.get());
        flywheel.config_kI(0, FLYWHEEL_KI.get());
        flywheel.config_kD(0, FLYWHEEL_KD.get());
        flywheel.config_kF(0, FLYWHEEL_KF.get());
        flywheel.setIntegralAccumulator(0);
    }


    @Override
    public void robotPeriodic() {
        // flywheel.set(TalonFXControlMode.PercentOutput, 1);
        // System.out.println(flywheel.getTemperature());
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setFlywheelSpeed(double rpm) {
        if (state == State.ON) {
            flywheel.set(TalonFXControlMode.Velocity, rpm * RPM_TO_FX_VELOCITY * FLYWHEEL_GEAR_RATIO);
        } else {
            flywheel.set(TalonFXControlMode.Velocity, FLYWHEEL_IDLE_SPEED.get() * RPM_TO_FX_VELOCITY * FLYWHEEL_GEAR_RATIO);
        }
//    System.out.println("Current: " + flywheel.getSelectedSensorVelocity() /
//    RPM_TO_FX_VELOCITY / FLYWHEEL_GEAR_RATIO + " Target: " + rpm);
    }

}
