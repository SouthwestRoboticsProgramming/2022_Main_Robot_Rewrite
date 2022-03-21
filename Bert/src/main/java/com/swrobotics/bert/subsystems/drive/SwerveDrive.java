package com.swrobotics.bert.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.subsystems.Subsystem;

import com.swrobotics.bert.util.Utils;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveDriveOdometry;
import edu.wpi.first.math.kinematics.SwerveModuleState;

import static com.swrobotics.bert.constants.DriveConstants.*;

public class SwerveDrive implements Subsystem {
    /*
     * Wheel Layout:
     * 
     * w1 ------- w2
     *  |    ^    |
     *  |    |    |
     *  |    |    |
     * w4 ------- w3
     */

    private final SwerveDriveKinematics kinematics;
    private final SwerveModule frontLeft, frontRight, backRight, backLeft;
    private final AHRS gyro;
    private final SwerveDriveOdometry odometry;

    public SwerveDrive(AHRS gyro) {

        this.gyro = gyro;

        // Constructs the spacing of the wheels
        double wheelOffset = 0.5 * WHEEL_SPACING;
        kinematics = new SwerveDriveKinematics(
            // Front Left
            new Translation2d(wheelOffset, wheelOffset),
            // Front Right
            new Translation2d(wheelOffset, -wheelOffset),
            //Back Right
            new Translation2d(-wheelOffset, -wheelOffset),
            // Back Left
            new Translation2d(-wheelOffset, wheelOffset)
        );

        // Module selector using shuffleboard
        int flID = FRONT_LEFT_MODULE.get() - 1;
        int frID = FRONT_RIGHT_MODULE.get() - 1;
        int brID = BACK_RIGHT_MODULE.get() - 1;
        int blID = BACK_LEFT_MODULE.get() - 1;

        // Info for all of the modules (Allows for hotswap)
        SwerveModuleInfo flInfo = SWERVE_INFO[flID];
        SwerveModuleInfo frInfo = SWERVE_INFO[frID];
        SwerveModuleInfo brInfo = SWERVE_INFO[brID];
        SwerveModuleInfo blInfo = SWERVE_INFO[blID];

        frontLeft = new SwerveModule(flInfo.getDriveID(), TURN_ID_FRONT_LEFT, flInfo.getCancoderID(), flInfo.getCancoderOffset() - 0);
        frontRight = new SwerveModule(frInfo.getDriveID(), TURN_ID_FRONT_RIGHT, frInfo.getCancoderID(), frInfo.getCancoderOffset() - 90);
        backRight = new SwerveModule(brInfo.getDriveID(), TURN_ID_BACK_RIGHT, brInfo.getCancoderID(), brInfo.getCancoderOffset() - 180);
        backLeft = new SwerveModule(blInfo.getDriveID(), TURN_ID_BACK_LEFT, blInfo.getCancoderID(), blInfo.getCancoderOffset() - 270);

        odometry = new SwerveDriveOdometry(kinematics, gyro.getRotation2d());
    }

    public ChassisSpeeds getRealChassisSpeeds() {
        return kinematics.toChassisSpeeds(frontLeft.getRealState(), frontRight.getRealState(), backRight.getRealState(), backLeft.getRealState());
    }

    public void calibrateOdometry(double x, double y) {
        Rotation2d gyroRotation = gyro.getRotation2d();

        Pose2d newPose = new Pose2d(
                y,
                -x,
                gyroRotation
        );

        odometry.resetPosition(newPose, gyroRotation);
    }

    public Pose2d getOdometryPose() {
        Pose2d odometryPose = odometry.getPoseMeters();

        Translation2d fieldPosition = new Translation2d(
                -odometryPose.getY(),
                odometryPose.getX()
        );

        return new Pose2d(fieldPosition, gyro.getRotation2d());
    }

    private double getDriveLerpFactor() {
        double avgError = (
                frontLeft.getError() +
                frontRight.getError() +
                backRight.getError() +
                backLeft.getError()
        ) / 4.0;

        double fullThreshold = DRIVE_FULL_THRESHOLD.get();
        double stopThreshold = DRIVE_STOP_THRESHOLD.get();

        double clampedError = Utils.clamp(avgError, fullThreshold, stopThreshold);

        return Utils.map(clampedError, stopThreshold, fullThreshold, 0, 1);
    }

    public void update(ChassisSpeeds chassis) {
        SwerveModuleState[] states = kinematics.toSwerveModuleStates(chassis);
        SwerveDriveKinematics.desaturateWheelSpeeds(states, MAX_ATTAINABLE_WHEEL_SPEED);

        double lerp = getDriveLerpFactor();
        for (SwerveModuleState state : states) {
            state.speedMetersPerSecond *= lerp;
        }

//        if (!frontLeft.isAtTargetAngle() ||
//            !frontRight.isAtTargetAngle() ||
//            !backRight.isAtTargetAngle() ||
//            !backLeft.isAtTargetAngle()) {
//
//            // Don't move
//            for (SwerveModuleState state : states) {
//                state.speedMetersPerSecond = 0;
//            }
//        }

        frontLeft.update(states[0]);
        frontRight.update(states[1]);
        backRight.update(states[2]);
        backLeft.update(states[3]);
    }

    public void stop() {
        frontLeft.stop();
        frontRight.stop();
        backRight.stop();
        backLeft.stop();
    }

    @Override
    public void robotInit() {
        gyro.calibrate();
        //gyro.setAngleAdjustment(180); // TODO: This doesn't do anything
    }

    @Override
    public void robotPeriodic() {
        SwerveModuleState[] realStates = {frontLeft.getRealState(), frontRight.getRealState(), backRight.getRealState(), backLeft.getRealState()};
        odometry.updateWithTime(System.currentTimeMillis() / 1000.0, gyro.getRotation2d(), realStates);

        // System.out.printf(
        //     "CANCoders: %3.3f %3.3f %3.3f %3.3f %n",
        //     frontLeft.getCANCoderAngle(),
        //     frontRight.getCANCoderAngle(),
        //     backRight.getCANCoderAngle(),
        //     backLeft.getCANCoderAngle()
        // );

        //System.out.print(frontLeft);
    }
}
