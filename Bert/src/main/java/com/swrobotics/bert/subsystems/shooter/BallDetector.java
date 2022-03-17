package com.swrobotics.bert.subsystems.shooter;

import com.swrobotics.bert.subsystems.Subsystem;

import edu.wpi.first.wpilibj.DigitalInput;

import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class BallDetector implements Subsystem {
    private final DigitalInput input;
    private boolean detected;

    public BallDetector() {
        input = new DigitalInput(BALL_DETECTOR_ID);
        detected = false;
    }

    @Override
    public void robotPeriodic() {
        detected = !input.get();
    }

    public boolean isBallDetected() {
        return detected;
    }
}
