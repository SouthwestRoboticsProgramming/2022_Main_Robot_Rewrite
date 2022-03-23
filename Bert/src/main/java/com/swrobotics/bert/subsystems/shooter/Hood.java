package com.swrobotics.bert.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonSRXBuilder;
import com.swrobotics.bert.util.Utils;

import static com.swrobotics.bert.constants.ShooterConstants.*; 

public class Hood implements Subsystem {
  private final TalonSRX hood;

  public Hood() {
    hood = new TalonSRXBuilder(HOOD_ID)
      .setPIDF(
          HOOD_KP.get(),
          HOOD_KI.get(),
          HOOD_KD.get(),
          HOOD_KF.get()
      )
      .build();

      hood.configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder);


      
      HOOD_KP.onChange(this::updatePID);
      HOOD_KI.onChange(this::updatePID);
      HOOD_KD.onChange(this::updatePID);
      HOOD_KF.onChange(this::updatePID);
  }

  private void updatePID() {
      hood.config_kP(0, HOOD_KP.get());
      hood.config_kI(0, HOOD_KI.get());
      hood.config_kD(0, HOOD_KD.get());
      hood.config_kF(0, HOOD_KF.get());
  }
  
  public void setPosition(double position) {
    position = Utils.clamp(position, 0, 3);

    if (position == 0) {
      zero();
      return;
    }
    hood.set(TalonSRXControlMode.Position, position / 4.0 * HOOD_HIGHEST_TICKS.get());
  }

  public void zero() {
    // FIXME Ryan do the thing that was in the old code

    hood.setSelectedSensorPosition(-10); // *Suggested* Make this negative so that 0 position isn't constantly touching the limit switch
  }

}
