package com.swrobotics.bert.subsystems.drive;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.swrobotics.bert.subsystems.Subsystem;

import com.swrobotics.bert.util.TalonFXBuilder;
import com.swrobotics.bert.util.TalonSRXBuilder;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import static com.swrobotics.bert.constants.Constants.*;
import static com.swrobotics.bert.constants.DriveConstants.*;

/**
 * A single swerve module.
 * Controls both wheel rotation and speed
 */
public class SwerveModule {
    private final TalonFX drive;
    private final TalonSRX turn;

    public SwerveModule(int driveID, int turnID, int cancoderID, double cancoderOffset) {
        drive = new TalonFXBuilder(driveID)
                .setCANBus(CANIVORE)
                .setPIDF(
                        DRIVE_KP,
                        DRIVE_KI,
                        DRIVE_KD,
                        DRIVE_KF
                )
                .build();

        drive.configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor);

        turn = new TalonSRXBuilder(turnID)
                .setPIDF(
                    TURN_KP,
                    TURN_KI,
                    TURN_KD,
                    TURN_KF
                )
                .setCanCoder(cancoderID, cancoderOffset)
                .build();
    }

    public SwerveModuleState getRealState() {
        return new SwerveModuleState(drive.getSelectedSensorVelocity() / DRIVE_SPEED_TO_NATIVE_VELOCITY, Rotation2d.fromDegrees(turn.getSelectedSensorPosition()));
    }

    public void update(SwerveModuleState state) {
        state = SwerveModuleState.optimize(state, Rotation2d.fromDegrees(turn.getSelectedSensorPosition()));

        turn.set(TalonSRXControlMode.Position, state.angle.getDegrees());
        drive.set(TalonFXControlMode.Velocity, state.speedMetersPerSecond * DRIVE_SPEED_TO_NATIVE_VELOCITY);
    }
}
