package com.swrobotics.bert.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonFXBuilder;

import static com.swrobotics.bert.constants.Constants.CANIVORE;
import static com.swrobotics.bert.constants.IntakeConstants.*;
import static com.swrobotics.bert.constants.DriveConstants.TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION;

public class Intake implements Subsystem {
    private final TalonFX motor;
    private boolean running;

    public Intake() {
        motor = new TalonFXBuilder(INTAKE_MOTOR_ID)
                .setCANBus(CANIVORE)
                .setPIDF(
                    INTAKE_KP.get(),
                    INTAKE_KI.get(),
                    INTAKE_KD.get(),
                    INTAKE_KF.get()
                )
                .build();

        motor.set(ControlMode.PercentOutput, 0);
        running = false;

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

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        System.out.println("Setting run state: " + (running ? "RUNNING" : "STOP"));

        if (running && !this.running) {
            motor.set(ControlMode.Velocity, INTAKE_SPEED.get() * TALON_FX_NATIVE_SENSOR_UNITS_PER_ROTATION);
        } else if (!running && this.running) {
            motor.set(ControlMode.Velocity, 0);
        }
        this.running = running;
    }
}
