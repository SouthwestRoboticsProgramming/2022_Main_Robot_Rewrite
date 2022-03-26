package com.swrobotics.bert.subsystems;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.subsystems.camera.CameraTurret;
import com.swrobotics.bert.subsystems.camera.Cameras;
import com.swrobotics.bert.subsystems.camera.Limelight;
import com.swrobotics.bert.subsystems.drive.SwerveDrive;
import com.swrobotics.bert.util.Utils;
import com.swrobotics.messenger.client.MessengerClient;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;

public final class Localization implements Subsystem {
    private final AHRS gyro;
    private final SwerveDrive drive;
    private final Cameras cameras;
    private final Limelight limelight;
    private final CameraTurret turret;
    private double fieldX, fieldY;

    public Localization(AHRS gyro, SwerveDrive drive, Cameras cameras, Limelight limelight, CameraTurret turret, MessengerClient msg) {
        this.gyro = gyro;
        this.drive = drive;
        this.cameras = cameras;
        this.limelight = limelight;
        this.turret = turret;

        // TODO: Remove this once we get absolute localization with cameras
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

    @Override
    public void robotPeriodic() {
        if (true) { // FIXME

            double visionAngle = limelight.getXangle();
            double visionDist = limelight.getDistance();
            double gyroAngle = gyro.getRotation2d().getDegrees();

            double angleDiff = visionAngle - gyroAngle;

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
