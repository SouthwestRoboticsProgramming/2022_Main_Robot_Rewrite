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
    private final RotatingArms armPair;

    private boolean manualMoving;
    private boolean loaded;
    private double target;
    
    private double arm;
    private double base;
    private double rotsPerInch;
    private double offset;

    private boolean safetyDisable = false;

    public RotatingArm(int motorID, boolean inverted, String name, RotatingArms armPair){
        this.name = name;
        motor = new CANSparkMax(motorID, MotorType.kBrushless);
        motor.setIdleMode(IdleMode.kBrake);
        motor.setInverted(inverted);
        this.armPair = armPair;

        encoder = motor.getEncoder();
        offset = -encoder.getPosition();

        pid = new PIDController(
            ROTATING_PID_KP.get(),
            ROTATING_PID_KI.get(),
            ROTATING_PID_KD.get()
        );

        // DO NOT EJECT ROTATING ARMS
        motor.set(0);
        motor.stopMotor();

        ROTATING_PID_KP.onChange(this::updatePID);
        ROTATING_PID_KI.onChange(this::updatePID);
        ROTATING_PID_KD.onChange(this::updatePID);
        ROTATING_PID_LOADED_KP.onChange(this::updatePID);
        ROTATING_PID_LOADED_KI.onChange(this::updatePID);
        ROTATING_PID_LOADED_KD.onChange(this::updatePID);

        arm = ROTATING_ARM_LENGTH;
        base = ROTATING_BASE_LENGTH;
        rotsPerInch = ROTATING_ROTS_PER_INCH;
    }

    public void zero() {
        offset = -encoder.getPosition();
    }

    private void updatePID() {
        if (loaded) {
            pid.setPID(
                ROTATING_PID_LOADED_KP.get(),
                ROTATING_PID_LOADED_KI.get(),
                ROTATING_PID_LOADED_KD.get()
            );
        } else {
            pid.setPID(
                ROTATING_PID_KP.get(),
                ROTATING_PID_KI.get(),
                ROTATING_PID_KD.get()
            );
        }
    }

    public void setTargetAngleDegrees(double angle) {
        target = Utils.clamp(angle, 40, 120);;
        manualMoving = false;
    }

    public void manualMove(double percentOutput) {
        manualMoving = true;
        target = percentOutput;
    }

    public void stop() {
        manualMoving = true;
        target =  0;
    }

    public void stopMotor() {
        motor.stopMotor();
    }

    public boolean isInTolerance() {
        double offset = Math.abs((Math.abs(target) - Math.abs(getCurrentAngle())));
        boolean inTolerance = ROTATING_TOLERANCE.get() > offset;
        if (inTolerance) {
            return true;
        } else {
            System.out.println(name + " not in tolerance with offset of " + offset);
            return false;
        }
    }

    double lastEncoder = 0;
    boolean doEncoderCheck = false;
    private double getCurrentAngle() {
        double encoderPos = encoder.getPosition();

        // if (doEncoderCheck) {
        //     double encoderDiff = Math.abs(encoderPos - lastEncoder);
        //     if (encoderDiff > ROTATING_ARM_ENCODER_DIFF_SHUTOFF_THRESHOLD.get()) {
        //         armPair.safetyShutoff();
        //         throw new RuntimeException("Test (offset " + encoderDiff + ")");
        //     }
        // } else {
        //     doEncoderCheck = true;
        // }
        // lastEncoder = encoderPos;

        // Do law of cosines
        double currentPose = (encoderPos + offset) / rotsPerInch + ROTATING_STARTING_LENGTH;
        double currentAngle = Math.acos((base*base + arm*arm - currentPose*currentPose)/(2*arm*base));
        return Math.toDegrees(currentAngle);
    }
    
    public void update() {
        if (safetyDisable) {
            stopMotor();
            return;
        }

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

    public void safetyShutoff() {
        safetyDisable = true;
        stopMotor();
    }

    public void disable() {
        safetyDisable = true;
    }
}
