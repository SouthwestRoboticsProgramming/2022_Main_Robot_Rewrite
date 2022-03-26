package com.swrobotics.bert.subsystems.climber.rotating;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.swrobotics.bert.util.Utils;

import edu.wpi.first.math.controller.PIDController;

import static com.swrobotics.bert.constants.ClimberConstants.*;

public final class RotatingArm {
    private final CANSparkMax motor;
    private final RelativeEncoder encoder;
    private final PIDController pid;
    private final String name;

    private boolean manualMoving;
    private boolean loaded;
    private double kF;
    private double target;
    
    private double arm;
    private double base;
    private double rotsPerInch;

    public RotatingArm(int motorID, boolean inverted, String name){
        this.name = name;
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

        arm = ROTATING_ARM_LENGTH;
        base = ROTATING_BASE_LENGTH;
        rotsPerInch = ROTATING_ROTS_PER_INCH;
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

    public boolean isInTolarence() {
        double offset = Math.abs((Math.abs(target) - Math.abs(getCurrentAngle())));
        boolean inTolarence = ROTATING_TOLERANCE.get() > offset;
        if (inTolarence) {
            return true;
        } else {
            System.out.println(name + " not in tolarence with offset of " + offset);
            return false;
        }
    }

    private double getCurrentAngle() {
        // Do law of cosines
        double currentPose = encoder.getPosition() / rotsPerInch + ROTATING_STARTING_LENGTH;
        double currentAngle = Math.acos((base*base + arm*arm - currentPose*currentPose)/(2*arm*base));
        return Math.toDegrees(currentAngle);
    }
    
    public void update() {
        double percentOut;
        if (manualMoving) {
            percentOut = target;
        } else {
            
            percentOut = pid.calculate(getCurrentAngle(), target);
        }

        double out = Utils.clamp(percentOut, -ROTATING_MAX_PERCENT.get(), ROTATING_MAX_PERCENT.get());
        motor.set(out);

        //System.out.println("Encoder (" + motor.getDeviceId() + "): " + encoder.getPosition());
    }
    
    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }
}
