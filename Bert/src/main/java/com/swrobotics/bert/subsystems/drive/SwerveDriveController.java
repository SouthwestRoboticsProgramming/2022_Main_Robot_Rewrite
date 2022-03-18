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

    public SwerveDriveController(Input input, AHRS gyro, SwerveDrive drive) {
        this.input = input;
        this.gyro = gyro;
        chassis = new ChassisSpeeds();
        this.drive = drive;
    }

    @Override
    public void robotPeriodic() {
        
    }

    @Override
    public void teleopPeriodic() {
        double maxVelocity = MAX_VELOCITY.get();
        double maxTurnVelocity = MAX_TURN_VELOCITY.get();

        double fieldRelativeX = input.getDriveY() * maxVelocity;
        double fieldRelativeY = -input.getDriveX() * maxVelocity;
        double rotation = -input.getDriveRot() * maxTurnVelocity;
        Rotation2d gyroRotation = gyro.getRotation2d();

        // if (fieldRelativeX == 0 && fieldRelativeY == 0 && rotation == 0) {
        //     drive.stop();
        // } else {
            chassis = ChassisSpeeds.fromFieldRelativeSpeeds(fieldRelativeX, fieldRelativeY, rotation, gyroRotation); // Away velocity, left velocity, counter-clockwise speed, counter-clockwise gyro
            drive.update(chassis);
        // }
    }
}
