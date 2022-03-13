package com.swrobotics.bert.subsystems.intake;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonFXBuilder;

import static com.swrobotics.bert.constants.Constants.CANIVORE;
import static com.swrobotics.bert.constants.IntakeConstants.*;

public class Intake implements Subsystem {
    private final TalonFX motor;
    private boolean running;

    public Intake() {
        motor = new TalonFXBuilder(INTAKE_MOTOR_ID)
                .setCANBus(CANIVORE)
                .build();

        motor.set(ControlMode.PercentOutput, 0);
        running = false;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        if (running && !this.running) {
            motor.set(ControlMode.PercentOutput, INTAKE_SPEED);
        } else if (!running && this.running) {
            motor.set(ControlMode.PercentOutput, 0);
        }
        this.running = running;
    }
}
