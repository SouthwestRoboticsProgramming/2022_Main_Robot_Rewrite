package com.swrobotics.bert.subsystems.drive;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.subsystems.Subsystem;

import edu.wpi.first.math.kinematics.ChassisSpeeds;

public class SwerveDriveController implements Subsystem {
    
    private final SwerveDrive drive;
    private final ChassisSpeeds chassis;

    public SwerveDriveController(AHRS gyro) {
        drive = new SwerveDrive(gyro);
        chassis = new ChassisSpeeds();

    }

    @Override
    public void robotPeriodic() {
        
    }
}
