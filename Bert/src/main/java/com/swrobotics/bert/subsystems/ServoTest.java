package com.swrobotics.bert.subsystems;

import com.swrobotics.bert.control.Input;
import edu.wpi.first.wpilibj.Servo;

public class ServoTest implements Subsystem {
    private final Input input;
    private final Servo servo;

    public ServoTest(Input input, int servoID) {
        this.input = input;
        servo = new Servo(servoID);
    }

    @Override
    public void teleopPeriodic() {
        servo.setAngle(input.getServoAngle());
    }
}
