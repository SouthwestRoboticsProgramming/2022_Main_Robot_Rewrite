package com.swrobotics.bert.subsystems.shooter;

import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonSRXBuilder;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Encoder;

import static com.swrobotics.bert.constants.ShooterConstants.*;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;

public final class NewHood implements Subsystem {
    private final Encoder encoder;
    private final TalonSRX hood;
    private final DigitalInput limitSwitch;
    private final PIDController pid;


    private double targetPosition;
    private boolean isCalibrating;

    public NewHood() {
        encoder = new Encoder(HOOD_ENCODER_ID_1, HOOD_ENCODER_ID_2);
        hood = new TalonSRXBuilder(HOOD_ID)
            .setInverted(true)
            .setPIDF(
                HOOD_KP.get(),
                HOOD_KI.get(),
                HOOD_KD.get(),
                HOOD_KF.get()
               )
               .build();

        limitSwitch = new DigitalInput(HOOD_LIMIT_ID);
        pid = new PIDController(HOOD_KP.get(), HOOD_KI.get(), HOOD_KD.get());

        targetPosition = 0;
        isCalibrating = true;

        HOOD_KP.onChange(this::updatePID);
        HOOD_KI.onChange(this::updatePID);
        HOOD_KD.onChange(this::updatePID);
    }

    private void updatePID() {
        pid.setP(HOOD_KP.get());
        pid.setI(HOOD_KI.get());
        pid.setD(HOOD_KD.get());
    }

    public void setPosition(double position) {
        targetPosition = position;
    }

    @Override
    public void robotPeriodic() {
        if (isCalibrating) {
            System.out.println("Calibrating Hood");
            hood.set(TalonSRXControlMode.PercentOutput, HOOD_CALIBRATE_SPEED.get());

            if (limitSwitch.get()) {
                isCalibrating = false;
                encoder.reset();
            }
        } else {
            double out = pid.calculate(encoder.get(), targetPosition);
            hood.set(TalonSRXControlMode.PercentOutput, out);
        }

//        System.out.println("Encoder: " + hood.getSelectedSensorPosition() + ", Limit: " + limitSwitch.get() + ", calibrating: " + isCalibrating);
    }
}
