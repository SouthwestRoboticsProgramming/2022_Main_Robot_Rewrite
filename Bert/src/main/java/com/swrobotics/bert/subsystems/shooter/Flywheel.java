package com.swrobotics.bert.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonFXBuilder;

import static com.swrobotics.bert.constants.ShooterConstants.*;
import static com.swrobotics.bert.constants.Constants.*;

public final class Flywheel implements Subsystem {
    private final TalonFX flywheel;

    public Flywheel() {
        flywheel = new TalonFXBuilder(FLYWHEEL_ID)
                .setCANBus(CANIVORE)
                .setPIDF(
                        FLYWHEEL_KP.get(),
                        FLYWHEEL_KI.get(),
                        FLYWHEEL_KD.get(),
                        FLYWHEEL_KF.get()
                )
                .build();


        FLYWHEEL_KP.onChange(this::updatePID);
        FLYWHEEL_KI.onChange(this::updatePID);
        FLYWHEEL_KD.onChange(this::updatePID);
        FLYWHEEL_KF.onChange(this::updatePID);

        flywheel.setNeutralMode(NeutralMode.Coast);
        flywheel.configVoltageCompSaturation(11);
    }

    private void updatePID() {
        flywheel.config_kP(0, FLYWHEEL_KP.get());
        flywheel.config_kI(0, FLYWHEEL_KI.get());
        flywheel.config_kD(0, FLYWHEEL_KD.get());
        flywheel.config_kF(0, FLYWHEEL_KF.get());
        flywheel.setIntegralAccumulator(0);
    }


    @Override
    public void robotPeriodic() {
        // flywheel.set(TalonFXControlMode.PercentOutput, 1);
        // System.out.println(flywheel.getTemperature());
        // System.out.println(flywheel.getIntegralAccumulator());

        ShuffleBoard.show("Flywheel Temp (C)", flywheel.getTemperature());
    }

    public void setFlywheelSpeed(double rpm) {
        // System.out.println("Flywheel: " + rpm + " Actual: " + flywheel.getSelectedSensorVelocity() / RPM_TO_FX_VELOCITY / FLYWHEEL_GEAR_RATIO);
        flywheel.set(TalonFXControlMode.Velocity, rpm * RPM_TO_FX_VELOCITY * FLYWHEEL_GEAR_RATIO);
//    System.out.println("Current: " + flywheel.getSelectedSensorVelocity() /
//    RPM_TO_FX_VELOCITY / FLYWHEEL_GEAR_RATIO + " Target: " + rpm);
    }
}
