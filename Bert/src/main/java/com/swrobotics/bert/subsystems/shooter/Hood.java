package com.swrobotics.bert.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonSRXBuilder;
import com.swrobotics.bert.util.Utils;
import edu.wpi.first.wpilibj.DigitalInput;

import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class Hood implements Subsystem {
    private final TalonSRX hood;
    private final DigitalInput limitSwitch;

    private double targetPosition;
    private boolean isCalibrating;

    public Hood() {
//        hood = new TalonSRXBuilder(HOOD_ID)
//                .setInverted(true)
//                .setPIDF(
//                        HOOD_KP.get(),
//                        HOOD_KI.get(),
//                        HOOD_KD.get(),
//                        HOOD_KF.get()
//                )
//                .build();
//
//        hood.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);

        hood = new TalonSRX(HOOD_ID);
        TalonSRXConfiguration hoodConfig = new TalonSRXConfiguration();
        hoodConfig.neutralDeadband = 0.001;
        hoodConfig.slot0.kP = HOOD_KP.get();
        hoodConfig.slot0.kI = HOOD_KI.get();
        hoodConfig.slot0.kD = HOOD_KD.get();
        hoodConfig.slot0.closedLoopPeakOutput = 1;
        hoodConfig.openloopRamp = 0.5;
        hoodConfig.closedloopRamp = 0.5;
        hood.configAllSettings(hoodConfig);
        hood.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);
        hood.setInverted(true);

        limitSwitch = new DigitalInput(HOOD_LIMIT_ID);

        targetPosition = 0;
        isCalibrating = true;

        HOOD_KP.onChange(this::updatePID);
        HOOD_KI.onChange(this::updatePID);
        HOOD_KD.onChange(this::updatePID);
        HOOD_KF.onChange(this::updatePID);
    }

    private void updatePID() {
        hood.config_kP(0, HOOD_KP.get());
        hood.config_kI(0, HOOD_KI.get());
        hood.config_kD(0, HOOD_KD.get());
        hood.config_kF(0, HOOD_KF.get());
    }

    @Override
    public void robotPeriodic() {
        if (isCalibrating) {
            System.out.println("it is calibrating");
            hood.set(TalonSRXControlMode.PercentOutput, HOOD_CALIBRATE_SPEED.get());

            if (limitSwitch.get()) {
                isCalibrating = false;
                hood.setSelectedSensorPosition(0);
            }
        } else {
            hood.set(TalonSRXControlMode.Position, targetPosition);
        }

        // System.out.println("Encoder: " + hood.getSelectedSensorPosition() + ", Limit: " + limitSwitch.get() + ", calibrating: " + isCalibrating);
    }

    public void setPosition(double position) {
        position = Utils.clamp(position, 0, 3);

//        if (position == 0) {
//            calibrate();
//            return;
//        }

        targetPosition = Utils.map(position, 0, 3, HOOD_LOWEST_TICKS.get(), HOOD_HIGHEST_TICKS.get());
    }

    public void calibrate() {
        isCalibrating = true;
    }
}
