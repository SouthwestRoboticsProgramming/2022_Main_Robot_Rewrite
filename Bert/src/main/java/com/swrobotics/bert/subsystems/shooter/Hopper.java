package com.swrobotics.bert.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonFXBuilder;

import static com.swrobotics.bert.constants.Constants.*;
import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class Hopper implements Subsystem {
    private final BallDetector detector;
    private final TalonFX index;
    private final Input input;

    private boolean shooting = false;

    public Hopper(BallDetector detector, /* TEMPORARY */ Input input) {
        this.input = input;
        this.detector = detector;

        index = new TalonFXBuilder(INDEX_ID)
            .setCANBus(CANIVORE)
            .setPIDF(
                INDEX_KP.get(),
                INDEX_KI.get(),
                INDEX_KD.get(),
                INDEX_KF.get()
            )
            .build();
        
        INDEX_KP.onChange(this::updatePID);
        INDEX_KI.onChange(this::updatePID);
        INDEX_KD.onChange(this::updatePID);
        INDEX_KF.onChange(this::updatePID);
    }

    private void updatePID() {
        index.config_kP(0, INDEX_KP.get());
        index.config_kI(0, INDEX_KI.get());
        index.config_kD(0, INDEX_KD.get());
        index.config_kF(0, INDEX_KF.get());
    }

    public void setIndexSpeed(double speed) {
        shooting = true;
        index.set(TalonFXControlMode.Velocity, speed * RPM_TO_FX_VELOCITY);
    }

    @Override
    public void robotPeriodic() {
        if (!shooting) {
            if (true || !detector.isBallDetected() || input.getShoot()) {
                index.set(TalonFXControlMode.Velocity, INDEX_IDLE_SPEED.get() * RPM_TO_FX_VELOCITY);
            } else {
                index.set(TalonFXControlMode.Velocity, 0);
            }
        }

        shooting = false;
    }
}
