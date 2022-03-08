package com.swrobotics.bert.subsystems.drive;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;
import com.ctre.phoenix.sensors.CANCoder;
import com.swrobotics.bert.subsystems.Subsystem;

import com.swrobotics.bert.util.TalonFXBuilder;

import static com.swrobotics.bert.constants.Constants.*;
import static com.swrobotics.bert.constants.DriveConstants.*;

/**
 * A single swerve module.
 */
public class SwerveModule implements Subsystem {
    private final TalonFX drive;
    private final CANCoder can;
    private final TalonSRX turn;

    public SwerveModule(int driveID, int turnID, int canCoderID, double canCoderOffset) {
        drive = new TalonFXBuilder(driveID)
                .setCANBus(CANIVORE)
                .setPIDF(
                        WHEEL_DRIVE_KP,
                        WHEEL_DRIVE_KI,
                        WHEEL_DRIVE_KD,
                        WHEEL_DRIVE_KF
                )
                .build();

        turn = new TalonSRX(turnID);
        can = new CANCoder(canCoderID);

        TalonSRXConfiguration turnConfig = new TalonSRXConfiguration();
        turnConfig.primaryPID.selectedFeedbackSensor = FeedbackDevice.RemoteSensor0;
        turnConfig.remoteFilter0.remoteSensorDeviceID = canCoderID;
        turnConfig.remoteFilter0.remoteSensorSource = RemoteSensorSource.CANCoder;

        can.setPosition(canCoderOffset);
    }

    public void update(double rotation) {
        /*
        Get the sensor pose
        Get the pose of where we want it to be
        Calculate how much to move it
        Move it

        Drive the drive motor to the right amount
        */

        double position = can.getAbsolutePosition();
        double desiredPosition = rotation;
    }

    @Override
    public void robotPeriodic() {

    }

}
