package com.swrobotics.bert.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.auto.FollowPathCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;

import static com.swrobotics.bert.constants.DriveConstants.*;

public class SwerveDriveController implements Subsystem {
    private final AHRS gyro;
    private final SwerveDrive drive;
    private final Input input;
    private ChassisSpeeds chassis;

    private boolean isAuto = false;
    private double autoX = 0;
    private double autoY = 0;

    public SwerveDriveController(Input input, AHRS gyro, SwerveDrive drive) {
        this.input = input;
        this.gyro = gyro;
        chassis = new ChassisSpeeds();
        this.drive = drive;
    }

    @Override
    public void robotPeriodic() {
        
    }

    public void drive(double x, double y) {
        autoX = x;
        autoY = y;
        isAuto = true;
    }

    @Override
    public void teleopPeriodic() {
        double driveXControl, driveYControl;
        if (isAuto) {
            driveXControl = autoX;
            driveYControl = autoY;
        } else {
            driveXControl = input.getDriveX();
            driveYControl = input.getDriveY();
        }
        isAuto = false;

        double maxVelocity = MAX_VELOCITY.get();
        double maxTurnVelocity = MAX_TURN_VELOCITY.get();

        double fieldRelativeX = driveYControl * maxVelocity;
        double fieldRelativeY = -driveXControl * maxVelocity;
        double rotation = -input.getDriveRot() * maxTurnVelocity;
        Rotation2d gyroRotation = gyro.getRotation2d();

        if (fieldRelativeX == 0 && fieldRelativeY == 0 && rotation == 0) {
            drive.stop();
        } else {
            chassis = ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeX, fieldRelativeY, rotation, gyroRotation); // Away velocity, left velocity, counter-clockwise speed, counter-clockwise gyro
            drive.update(chassis);
        }
    }
}
