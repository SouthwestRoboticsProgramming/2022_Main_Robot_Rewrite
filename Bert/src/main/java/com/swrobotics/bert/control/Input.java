package com.swrobotics.bert.control;

import edu.wpi.first.wpilibj.XboxController;

import static com.swrobotics.bert.constants.InputConstants.*;
public class Input {
    private final XboxController drive;
    private final XboxController manipulator;

    public Input() {
        drive = new XboxController(DRIVE_CONTROLLER_ID);
        manipulator = new XboxController(MANIPULATOR_CONTROLLER_ID);
    }

    /* Drive */
    public double getDriveX() {
        return drive.getLeftX();
    }

    public double getDriveY() {
        return drive.getLeftY();
    }

    public double getDriveRot() {
        return drive.getRightX();
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
