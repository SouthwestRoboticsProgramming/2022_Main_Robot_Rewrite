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

    private double offset = 0;

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
        return isInTolarence(TELESCOPING_TOLERANCE.get()); // TODO: Why Nate
    }

    private double encoderPerc() {
        return Utils.map(getEncoder(), TELESCOPING_MIN_TICKS.get(), TELESCOPING_MAX_TICKS.get(), 0, 1);
    }

    private boolean isInTolarence(double tolerance) {
        double offset = Math.abs(Math.abs(target) - Math.abs(encoderPerc()));
        boolean inTolarence = tolerance > offset;
        if (inTolarence) {
            return true;
        } else {
            System.out.println(name + " not in tolarence with offset of " + offset);
            return false;
        }
    }

    public void zero() {
        offset = encoder.getPosition();
    }

    private double getEncoder() {
        return encoder.getPosition() - offset;
    }

    public void update() {
        double percentOut;
        if (manualMoving) {
            percentOut = target;
        } else {
            if (loaded && isInTolarence(TELESCOPING_LOADED_PID_ENGAGE_PERC.get())) {
                percentOut = TELESCOPING_LOADED_PERCENT_OUT.get();
            } else {
                double targetTicks = Utils.map(target, 0, 1, TELESCOPING_MIN_TICKS.get(), TELESCOPING_MAX_TICKS.get());
                percentOut = pid.calculate(getEncoder(), targetTicks) + kF;
            }

            if (loaded) {
                percentOut = Utils.clamp(percentOut, -TELESCOPING_MAX_LOADED_PERCENT.get(), TELESCOPING_MAX_LOADED_PERCENT.get());
            } else {
                percentOut = Utils.clamp(percentOut, -TELESCOPING_MAX_UNLOADED_PERCENT.get(), TELESCOPING_MAX_UNLOADED_PERCENT.get());
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
