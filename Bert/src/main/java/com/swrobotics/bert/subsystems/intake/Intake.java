package com.swrobotics.bert.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonFXBuilder;

import static com.swrobotics.bert.constants.Constants.CANIVORE;
import static com.swrobotics.bert.constants.IntakeConstants.*;
import static com.swrobotics.bert.constants.DriveConstants.TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION;

public final class Intake implements Subsystem {
    public enum State {
        ON(INTAKE_SPEED),
        OFF(IDLE_SPEED),
        EJECT(EJECT_SPEED);

        private final TunableDouble speed;

        State(TunableDouble speed) {
            this.speed = speed;
        }

        public double getSpeed() {
            return speed.get();
        }
    }

    private final TalonFX motor;
    private State state;

    public Intake() {
        motor = new TalonFXBuilder(INTAKE_MOTOR_ID)
                .setCANBus(CANIVORE)
                .setPIDF(
                    INTAKE_KP.get(),
                    INTAKE_KI.get(),
                    INTAKE_KD.get(),
                    INTAKE_KF.get()
                )
                .setRamp(1)
                .build();

        motor.set(ControlMode.PercentOutput, 0);
        state = State.OFF;

        INTAKE_KP.onChange(this::updatePID);
        INTAKE_KI.onChange(this::updatePID);
        INTAKE_KD.onChange(this::updatePID);
        INTAKE_KF.onChange(this::updatePID);
    }

    private void updatePID() {
        System.out.println("UPDATE PID");
        motor.config_kP(0, INTAKE_KP.get());
        motor.config_kI(0, INTAKE_KI.get());
        motor.config_kD(0, INTAKE_KD.get());
        motor.config_kF(0, INTAKE_KF.get());
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        System.out.println("Setting run state: " + state);

        if (state != this.state) {
            motor.set(ControlMode.Velocity, state.getSpeed() * TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION);
        }
        this.state = state;
        System.out.println(state);
    }
}
