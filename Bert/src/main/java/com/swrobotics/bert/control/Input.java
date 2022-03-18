package com.swrobotics.bert.control;

import static com.swrobotics.bert.constants.InputConstants.*;

import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.Utils;

// Note: The Y axes on the sticks are backwards from what you would expect: up is negative
public class Input implements Subsystem {
    private final XboxController drive;
    private final XboxController manipulator;

    public Input() {
        drive = new XboxController(DRIVE_CONTROLLER_ID);
        manipulator = new XboxController(MANIPULATOR_CONTROLLER_ID);
    }

    /* Drive */
    public double getDriveX() {
        return deadzone(drive.leftStickX.get());
    }

    public double getDriveY() {
        return deadzone(drive.leftStickY.get());
    }

    public double getDriveRot() {
        return deadzone(drive.rightStickX.get());
    }


    private double deadzone(double amount) {
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE.get()) {
            return 0;
        }
        return Math.signum(amount) * Utils.map(Math.abs(amount), JOYSTICK_DEAD_ZONE.get(), 1, 0, 1);
    }

    /* Manipulator */
    public boolean getToggleIntake() {
        return manipulator.y.leadingEdge();
    }

    public double getTeleDistance() {
        return Utils.clamp(manipulator.leftStickY.get(), 0, 1);
    }

    public double getRotAngle() {
        return 90;
    }

    /* Temporary things */
    public int getServoAngle() {
        if (manipulator.x.isPressed())
            return 0;
        else if (manipulator.b.isPressed())
            return 180;
        else
            return 90;
    }

    @Override
    public void robotPeriodic() {
        drive.update();
        manipulator.update();
    }
}
