package com.swrobotics.bert.subsystems.shooter;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.TalonFXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.TalonFXBuilder;

import edu.wpi.first.math.controller.BangBangController;
import edu.wpi.first.math.controller.SimpleMotorFeedforward;

import static com.swrobotics.bert.constants.ShooterConstants.*;
import static com.swrobotics.bert.constants.Constants.*;

public final class Flywheel implements Subsystem {
    private final TalonFX flywheel;
    private final BangBangController bangBang;
    private SimpleMotorFeedforward feedforward;

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

        FLYWHEEL_KS.onChange(this::updateFeedforward);
        FLYWHEEL_KV.onChange(this::updateFeedforward);
        FLYWHEEL_KA.onChange(this::updateFeedforward);

        flywheel.setNeutralMode(NeutralMode.Coast);
        flywheel.configVoltageCompSaturation(11);

        bangBang = new BangBangController();
        feedforward = new SimpleMotorFeedforward(FLYWHEEL_KS.get(), FLYWHEEL_KV.get(),FLYWHEEL_KA.get());

    }

    private void updatePID() {
        flywheel.config_kP(0, FLYWHEEL_KP.get());
        flywheel.config_kI(0, FLYWHEEL_KI.get());
        flywheel.config_kD(0, FLYWHEEL_KD.get());
        flywheel.config_kF(0, FLYWHEEL_KF.get());
        flywheel.setIntegralAccumulator(0);
    }

    private void updateFeedforward() {
        feedforward = new SimpleMotorFeedforward(FLYWHEEL_KS.get(), FLYWHEEL_KV.get(), FLYWHEEL_KA.get());
    }
    
    public double getRPM() {
        return flywheel.getSelectedSensorVelocity() / RPM_TO_FX_VELOCITY / FLYWHEEL_GEAR_RATIO;
    }
    
    public void setFlywheelSpeed(double rpm) {
        double feed = feedforward.calculate(rpm);
        double bang = bangBang.calculate(getRPM(),rpm);
        
        flywheel.set(TalonFXControlMode.PercentOutput, bang + 0.9 * feed);
        
        System.out.println("Flywheel: " + rpm + " Actual: " + getRPM());
        
        // flywheel.set(TalonFXControlMode.Velocity, rpm * RPM_TO_FX_VELOCITY * FLYWHEEL_GEAR_RATIO);
        //    System.out.println("Current: " + flywheel.getSelectedSensorVelocity() /
        //    RPM_TO_FX_VELOCITY / FLYWHEEL_GEAR_RATIO + " Target: " + rpm);
    }

    @Override
    public void robotPeriodic() {
        ShuffleBoard.show("Flywheel Temp (C)", flywheel.getTemperature());
    }
}
