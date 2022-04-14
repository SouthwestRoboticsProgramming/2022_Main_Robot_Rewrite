package com.swrobotics.bert.util;

import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.CANCoderStatusFrame;
import com.ctre.phoenix.sensors.CANCoderConfiguration;
import com.ctre.phoenix.sensors.SensorInitializationStrategy;
import com.swrobotcs.bert.util.CtreUtils;

public final class CANCoderBuilder {
    public enum Direction {
        CLOCKWISE,
        COUNTER_CLOCKWISE
    }

    private final int canID;
    private int updatePeriodMilliseconds = 10;
    private Direction direction = Direction.COUNTER_CLOCKWISE;
    private double offsetDegrees;

    public CANCoderBuilder(int canID) {
        this.canID = canID;
    }

    public CANCoderBuilder setUpdatePeriod(int periodMiliseconds) {
        updatePeriodMilliseconds = periodMiliseconds;
        return this;
    }

    public CANCoderBuilder setDirection(Direction direction) {
        this.direction  = direction;
        return this;
    }

    public CANCoderBuilder setOffsetDegrees(double degrees) {
        offsetDegrees = degrees;
        return this;
    }

    public CANCoder build() {
        CANCoderConfiguration config = new CANCoderConfiguration();
        config.initializationStrategy = SensorInitializationStrategy.BootToAbsolutePosition;
        config.absoluteSensorRange = AbsoluteSensorRange.Unsigned_0_to_360;
        config.magnetOffsetDegrees = offsetDegrees;
        config.sensorDirection = direction == Direction.CLOCKWISE;

        CANCoder encoder = new CANCoder(canID);

        CtreUtils.checkCtreError(encoder.configAllSettings(config, 250), "Failed to configure CANCoder ID: " + canID);

        CtreUtils.checkCtreError(encoder.setStatusFramePeriod(CANCoderStatusFrame.SensorData, periodMilliseconds, 250), "Failed to configure CANCoder ID: " + canID + " update rate");
    }
}