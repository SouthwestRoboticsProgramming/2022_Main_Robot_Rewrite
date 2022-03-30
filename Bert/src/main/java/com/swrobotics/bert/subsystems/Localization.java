package com.swrobotics.bert.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.camera.Limelight;
import com.swrobotics.bert.subsystems.drive.SwerveDrive;
import com.swrobotics.bert.util.Utils;
import com.swrobotics.messenger.client.MessengerClient;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public final class Localization implements Subsystem {
    private final AHRS gyro;
    private final SwerveDrive drive;
    private final Limelight limelight;
    private final Input input;
    private double fieldX, fieldY;

    public Localization(AHRS gyro, SwerveDrive drive, Limelight limelight, MessengerClient msg, Input input) {
        this.gyro = gyro;
        this.drive = drive;
        this.limelight = limelight;
        this.input = input;

        if (msg != null) {
            msg.makeHandler()
                    .listen("Config:SetLocation")
                    .setHandler((type, in) -> {
                        fieldX = in.readDouble(); // x in meters
                        fieldY = in.readDouble(); // y in meters
                        drive.calibrateOdometry(fieldX, fieldY);

                        // gyro not settable yet
                    });
        }
    }

    public double getFieldX() {
        return fieldX;
    }

    public double getFieldY() {
        return fieldY;
    }

    public Rotation2d getFieldRot() {
        return gyro.getRotation2d();
    }

    public double getDistanceToTarget() {
        double robotX = drive.getOdometryPose().getX();
        double robotY = drive.getOdometryPose().getY();

        /* Distance Formula */
        double distance = Math.sqrt(robotX * robotX + robotY * robotY);
        return distance;
    }

    public Rotation2d getAngleToTarget() {
        double robotX = drive.getOdometryPose().getX();
        double robotY = drive.getOdometryPose().getY();

        double angleRadians = Math.atan2(-robotY, -robotX);

        return new Rotation2d(angleRadians);
    }

    public Rotation2d getLocalAngleToTarget() {
        double gyroAngle = gyro.getAngle();
        double angleToTarget = getAngleToTarget().getDegrees();

        double diff = -gyroAngle - angleToTarget + 90;
        double normalized = Utils.normalizeRadians(Math.toRadians(diff));


        // System.out.println("Gyro: " + gyroAngle + " Angle to target " + angleToTarget + " Diff (normalized): " + Math.toDegrees(normalized));
        return new Rotation2d(normalized);
    }

    public boolean isLookingAtTarget() {
        return Math.abs(getLocalAngleToTarget().getDegrees()) < 100;
    }

    @Override
    public void robotPeriodic() {
        // System.out.println("Y angle: " + limelight.getRealYangle() + " Distance: " + limelight.getDistance());
        if ((isLookingAtTarget() || input.getAimOverride()) && limelight.isAccurate()) { // If the limelight finds a target and is actually pointing at the target
            double visionAngle = limelight.getXangle();
            double visionDist = limelight.getDistance();
            double gyroAngle = gyro.getRotation2d().getDegrees();

            double angleDiff = gyroAngle - visionAngle;
            angleDiff -= 90;
            angleDiff = Math.toRadians(angleDiff);

            visionDist += 1.355852 / 2.0; // hub radius
            fieldX = visionDist * Math.cos(angleDiff);
            fieldY = visionDist * Math.sin(angleDiff);

            drive.calibrateOdometry(fieldX, fieldY);
        } else {
            Pose2d odometry = drive.getOdometryPose();

            fieldX = odometry.getX();
            fieldY = odometry.getY();
        }
    }
}
