package com.swrobotics.bert.util;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.RemoteSensorSource;
import com.ctre.phoenix.motorcontrol.can.SlotConfiguration;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.TalonSRXConfiguration;

public final class TalonSRXBuilder {

    private final int canID;
    private final TalonSRXConfiguration config;

    private boolean inverted = false;

    public TalonSRXBuilder(int canID) {
        this.canID = canID;

        config = new TalonSRXConfiguration();
        config.neutralDeadband = 0.001;
        config.openloopRamp = 0.5;   // Seconds to ramp from 0% to 100%
        config.closedloopRamp = 0.5; // Seconds to ramp from 0% to 100%
    }

    public TalonSRXBuilder setPIDF(double kP, double kI, double kD, double kF) {
        SlotConfiguration slot = config.slot0;

        slot.kP = kP;
        slot.kI = kI;
        slot.kD = kD;
        slot.kF = kF;
        slot.closedLoopPeakOutput = 1;

        return this;
    }

    // TODO: Add sensor config for other sensors
    public TalonSRXBuilder setCanCoder (int cancoderID) {
        config.primaryPID.selectedFeedbackSensor = FeedbackDevice.RemoteSensor0;
        config.remoteFilter0.remoteSensorDeviceID = cancoderID;
        config.remoteFilter0.remoteSensorSource = RemoteSensorSource.CANCoder;

        return this;
    }

    public TalonSRXBuilder setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public TalonSRX build() {
        TalonSRX srx = new TalonSRX(canID);

        srx.configAllSettings(config);
        srx.setInverted(inverted);

        return srx;
    }
}
