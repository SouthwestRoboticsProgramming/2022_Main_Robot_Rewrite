package com.swrobotics.bert.subsystems.camera;

import com.swrobotics.bert.subsystems.Subsystem;
import edu.wpi.first.wpilibj.Servo;

import static com.swrobotics.bert.constants.CameraTurretConstants.*;

public class CameraTurret implements Subsystem {
    private final Servo servo;
    private double targetAngle;
    private double predictedAngle;

    public CameraTurret() {
        servo = new Servo(SERVO_ID);

        targetAngle = 90;
    }

    public void turnTo(double angle) {
        servo.setAngle(angle);
        targetAngle = angle;
    }

    @Override
    public void robotPeriodic() {
        if (Math.abs(targetAngle - predictedAngle) > SERVO_TURN_PER_PERIODIC) {
            predictedAngle += Math.signum(targetAngle - predictedAngle) * SERVO_TURN_PER_PERIODIC;
        } else {
            predictedAngle = targetAngle;
        }

        System.out.println("Servo at " + predictedAngle);
    }
}
