package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.Command;
import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.shuffle.TunableDouble;

public class WaitForAngle implements Command {
  private AHRS gyro;
  private TunableDouble setAngle;
  private double previousAngle = 0;

  public WaitForAngle(AHRS gyro, TunableDouble setAngle) {
    this.setAngle = setAngle;
    this.gyro = gyro;
 }

  @Override
  public void init() {}

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

  @Override
  public void end() {

  }

  private double getAngle() {
    return -gyro.getPitch();
  }

}
