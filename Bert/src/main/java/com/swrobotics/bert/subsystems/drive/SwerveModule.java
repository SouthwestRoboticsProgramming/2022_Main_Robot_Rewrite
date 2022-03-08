package com.swrobotics.bert.subsystems.drive;

import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.sensors.CANCoder;
import com.swrobotics.bert.subsystems.Subsystem;

import static com.swrobotics.bert.constants.Constants.*;

/**
 * A single swerve module
 */
public class SwerveModule implements Subsystem {
  private final TalonFX drive;
  private final CANCoder can;  
  private final TalonSRX turn;

  public SwerveModule(int driveID, int turnID, int canCoderID, double canCoderOffset) {
    drive = new TalonFX(driveID, CANIVORE);
    turn = new TalonSRX(turnID);
    can = new CANCoder(canCoderID);

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
