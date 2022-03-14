package com.swrobotics.bert.control;

import edu.wpi.first.wpilibj.XboxController;

import static com.swrobotics.bert.constants.InputConstants.*;

import com.swrobotics.bert.util.Utils;

public class Input {
    private final XboxController drive;
    private final XboxController manipulator;

    public Input() {
        drive = new XboxController(DRIVE_CONTROLLER_ID);
        manipulator = new XboxController(MANIPULATOR_CONTROLLER_ID);
    }

    /* Drive */
    public double getDriveX() {
        return deadzone(drive.getLeftX());
    }

    public double getDriveY() {
        return deadzone(drive.getLeftY());
    }

    public double getDriveRot() {
        return deadzone(drive.getRightX());
    }




    private double deadzone(double amount) {
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE) {
            return 0;
        }
        return Math.signum(amount) * Utils.map(Math.abs(amount), JOYSTICK_DEAD_ZONE, 1, 0, 1);
    }

    /* Manipulator */
    public boolean getToggleIntake() {
        return manipulator.getYButtonPressed();
    }

    /* Temporary things */
    public int getServoAngle() {
        if (manipulator.getXButton())
            return 0;
        else if (manipulator.getBButton())
            return 180;
        else
            return 90;
    }
}
