package com.swrobotics.bert.subsystems.climber.telescoping;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.swrobotics.bert.util.Utils;

import edu.wpi.first.math.controller.PIDController;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class TelescopingArm {
    private final CANSparkMax motor1;
    private final CANSparkMax motor2;
    private final RelativeEncoder encoder;
    private final PIDController pid;
    private final String name;

    private boolean manualMoving;
    private boolean loaded;
    private double kF;
    private double target;

    public TelescopingArm(int motor1ID, int motor2ID, boolean inverted, String name) {
        this.name = name;
        motor1 = new CANSparkMax(motor1ID, MotorType.kBrushless);
        motor1.restoreFactoryDefaults();
        motor1.setIdleMode(IdleMode.kBrake);
        motor1.setInverted(inverted);

        motor2 = new CANSparkMax(motor2ID, MotorType.kBrushless);
        motor2.restoreFactoryDefaults();
        motor2.setIdleMode(IdleMode.kBrake);
        motor2.setInverted(inverted);

        encoder = motor1.getEncoder();
        encoder.setPosition(0);

        pid = new PIDController(
            TELESCOPING_PID_KP.get(),
            TELESCOPING_PID_KI.get(),
            TELESCOPING_PID_KD.get()
        );
        kF = TELESCOPING_PID_KF.get();

        TELESCOPING_PID_KP.onChange(this::updatePID);
        TELESCOPING_PID_KI.onChange(this::updatePID);
        TELESCOPING_PID_KD.onChange(this::updatePID);
        TELESCOPING_PID_KF.onChange(this::updatePID);
        TELESCOPING_PID_LOADED_KP.onChange(this::updatePID);
        TELESCOPING_PID_LOADED_KI.onChange(this::updatePID);
        TELESCOPING_PID_LOADED_KD.onChange(this::updatePID);
        TELESCOPING_PID_LOADED_KF.onChange(this::updatePID);

        manualMoving = false;
    }

    private void updatePID() {
        if (loaded) {
            pid.setPID(
                TELESCOPING_PID_LOADED_KP.get(),
                TELESCOPING_PID_LOADED_KI.get(),
                TELESCOPING_PID_LOADED_KD.get()
            );
            kF = TELESCOPING_PID_LOADED_KF.get();
        } else {
            pid.setPID(
                    TELESCOPING_PID_KP.get(),
                    TELESCOPING_PID_KI.get(),
                    TELESCOPING_PID_KD.get()
            );
            kF = TELESCOPING_PID_KF.get();
        }
    }

    public void setTargetDistancePercent(double distance) {
        target = distance;
        manualMoving = false;
    }

    public void manualMove(double percentOutput) {
        manualMoving = true;
        target = percentOutput;
    }

    public boolean isInTolarence() {
        double offset = Math.abs((Math.abs(target) - Math.abs((Utils.map(encoder.getPosition(), TELESCOPING_MIN_TICKS.get(), TELESCOPING_MAX_TICKS.get(), 0, 1)))));
        boolean inTolarence = TELESCOPING_TOLERANCE.get() > offset;
        if (inTolarence) {
            return true;
        } else {
            System.out.println(name + " not in tolarence with offset of " + offset);
            return false;
        }
    }

    public void zero() {
        encoder.setPosition(0);
    }

    public void update() {
        double percentOut;
        if (manualMoving) {
            percentOut = target;
        } else {
            double targetTicks = Utils.map(target, 0, 1, TELESCOPING_MIN_TICKS.get(), TELESCOPING_MAX_TICKS.get());
            double pidOut = pid.calculate(encoder.getPosition(), targetTicks) + kF;

            if (loaded) {
                percentOut = Utils.clamp(pidOut, -TELESCOPING_MAX_LOADED_PERCENT.get(), TELESCOPING_MAX_LOADED_PERCENT.get());
            } else {
                percentOut = Utils.clamp(pidOut, -TELESCOPING_MAX_UNLOADED_PERCENT.get(), TELESCOPING_MAX_UNLOADED_PERCENT.get());
            }
        }
        motor1.set(percentOut);
        motor2.set(percentOut);
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
        updatePID();
    }
}
