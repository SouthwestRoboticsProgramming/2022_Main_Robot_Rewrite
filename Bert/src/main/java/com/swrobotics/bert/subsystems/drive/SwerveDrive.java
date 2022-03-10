package com.swrobotics.bert.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.subsystems.Subsystem;

import edu.wpi.first.math.geometry.Pose2d;
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
     private final double wheelOffset;
     private final SwerveModule frontLeft, frontRight, backRight, backLeft;
     private final AHRS gyro;
     private final SwerveDriveOdometry odometry;

     public SwerveDrive(AHRS gyro){

        this.gyro = gyro;

        // Constructs the spacing of the wheels
         wheelOffset = 0.5 * WHEEL_SPACING;
         kinematics = new SwerveDriveKinematics(
             // Front Left
             new Translation2d(-wheelOffset,wheelOffset),
            // Front Right
            new Translation2d(wheelOffset, wheelOffset),
            //Back Right
            new Translation2d(wheelOffset, -wheelOffset),
            // Back Left
            new Translation2d(-wheelOffset, -wheelOffset)
         );

        // Module selector using shuffleboard
         int flID = (int) ShuffleBoard.frontLeftModule.getDouble(1) - 1;
         int frID = (int) ShuffleBoard.frontLeftModule.getDouble(2) - 1;
         int brID = (int) ShuffleBoard.frontLeftModule.getDouble(3) - 1;
         int blID = (int) ShuffleBoard.frontLeftModule.getDouble(4) - 1;

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

     public Pose2d getOdometryPose() {
         return odometry.getPoseMeters(); // NOTE: X and Y are probaly counter intuative
     }

     public void update(ChassisSpeeds chassis) {
         SwerveModuleState[] states = kinematics.toSwerveModuleStates(chassis);

         // FIXME: Ryan check that this does what I think it should
         SwerveDriveKinematics.desaturateWheelSpeeds(states, MAX_ATTAINABLE_WHEEL_SPEED);

         frontLeft.update(states[0]);
         frontRight.update(states[1]);
         backRight.update(states[2]);
         backLeft.update(states[3]);
     }

     @Override
     public void robotInit() {
         gyro.calibrate();
         gyro.setAngleAdjustment(-90);
     }

     @Override
     public void robotPeriodic() {
         SwerveModuleState[] realStates = {frontLeft.getRealState(), frontRight.getRealState(), backRight.getRealState(), backLeft.getRealState()};
         odometry.updateWithTime(System.currentTimeMillis(), gyro.getRotation2d(),realStates);
     }
}
