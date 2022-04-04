package com.swrobotics.bert.subsystems.shooter;

import com.swrobotics.bert.subsystems.Subsystem;

import edu.wpi.first.wpilibj.DigitalInput;

import static com.swrobotics.bert.constants.ShooterConstants.*;

import com.swrobotics.bert.shuffle.ShuffleBoard;

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

        ShuffleBoard.showBoolean("Ball detected", detected);
    }

    public boolean isBallDetected() {
        return detected;
    }
}
