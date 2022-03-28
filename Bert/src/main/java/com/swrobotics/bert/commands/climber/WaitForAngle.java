// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.Command;
import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.shuffle.TunableDouble;

public class WaitForAngle implements Command {
  private AHRS gyro;
  private TunableDouble setAngle;
  private double previousAngle = 0;
  /** Creates a new WaitForAngle. */
  public WaitForAngle(AHRS gyro, TunableDouble setAngle) {
    this.setAngle = setAngle;
    this.gyro = gyro;
    // Use addRequirements() here to declare subsystem dependencies.
  }

  // Called when the command is initially scheduled.
  @Override
  public void init() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public boolean run() {
    double newAngle = getAngle();
    boolean positiveRate = newAngle > previousAngle;
    if (positiveRate && newAngle > setAngle.get()) {
      return true;
    } else {
      return false;
    }
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end() {

  }

  private double getAngle() {
    return gyro.getPitch();
  }

}
