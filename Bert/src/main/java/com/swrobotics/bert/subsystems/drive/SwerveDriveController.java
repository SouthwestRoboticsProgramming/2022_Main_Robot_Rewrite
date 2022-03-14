package com.swrobotics.bert.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;
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

    public SwerveDriveController(Input input, AHRS gyro) {
        this.input = input;
        this.gyro = gyro;
        drive = new SwerveDrive(gyro);
        chassis = new ChassisSpeeds();

    }

    @Override
    public void robotPeriodic() {
        
    }

    @Override
    public void teleopPeriodic() {
        double fieldRelativeX = input.getDriveY() * MAX_VELOCITY;
        double fieldRelativeY = -input.getDriveX() * MAX_VELOCITY;
        double rotation = -input.getDriveRot() * MAX_TURN_VELOCITY;
        Rotation2d gyroRotation = gyro.getRotation2d();

        chassis = ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeX, fieldRelativeY, rotation, gyroRotation); // Away velocity, left velocity, counter-clockwise speed, counter-clockwise gyro
        drive.update(chassis);
    }
}
