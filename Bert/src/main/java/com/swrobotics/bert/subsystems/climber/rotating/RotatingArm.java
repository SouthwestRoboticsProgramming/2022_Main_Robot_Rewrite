package com.swrobotics.bert.subsystems.climber.rotating;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class RotatingArm {
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;
    private final PIDController pid;

    private boolean manualMoving;
    private boolean loaded;
    private double kF;
    private double target;

    public RotatingArm(int motorID, boolean inverted){
        motor = new CANSparkMax(motorID, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setInverted(inverted);

        encoder = motor.getEncoder();
        encoder.setPosition(0);

        pid = new PIDController(
            ROTATING_PID_KP.get(),
            ROTATING_PID_KI.get(),
            ROTATING_PID_KD.get()
        );
        kF = ROTATING_PID_KF.get();

        ROTATING_PID_KP.onChange(this::updatePID);
        ROTATING_PID_KI.onChange(this::updatePID);
        ROTATING_PID_KD.onChange(this::updatePID);
        ROTATING_PID_KF.onChange(this::updatePID);
        ROTATING_PID_LOADED_KP.onChange(this::updatePID);
        ROTATING_PID_LOADED_KI.onChange(this::updatePID);
        ROTATING_PID_LOADED_KD.onChange(this::updatePID);
        ROTATING_PID_LOADED_KF.onChange(this::updatePID);
    }

    public void zero() {
        encoder.setPosition(0);
    }

    private void updatePID() {
        if (loaded) {
            pid.setPID(
                ROTATING_PID_LOADED_KP.get(),
                ROTATING_PID_LOADED_KI.get(),
                ROTATING_PID_LOADED_KD.get()
            );
            kF = ROTATING_PID_LOADED_KF.get();
        } else {
            pid.setPID(
                ROTATING_PID_KP.get(),
                ROTATING_PID_KI.get(),
                ROTATING_PID_KD.get()
            );
            kF = ROTATING_PID_KF.get();
        }
    }

    public void setTargetAngleDegrees(double angle) {
        target = angle;
        manualMoving = false;
    }

    public void manualMove(double percentOutput) {
        manualMoving = true;
        target = percentOutput;
    }
    
    public void update() {
        double percentOut;
        if (manualMoving) {
            percentOut = target;
        } else {
            // Do law of cosines
            percentOut = 0;
        }

        motor.set(percentOut);
    }
    
    public void setLoaded(boolean loaded) {
        
    }
}
