package com.swrobotics.bert.util;

import com.ctre.phoenix.sensors.CANCoder;
import edu.wpi.first.math.geometry.Rotation2d;

public class AbsoluteEncoder {
    private final CANCoder encoder;

    public AbsoluteEncoder(CANCoder encoder) {
        this.encoder = encoder;
    }

    public Rotation2d getAbsoluteAngle() {
        double angle = Math.toRadians(encoder.getAbsolutePosition());
        angle %= 2.0 * Math.PI;
        if (angle < 0.0) {
            angle += 2.0 * Math.PI;
        }

        return new Rotation2d(angle);
    }
}