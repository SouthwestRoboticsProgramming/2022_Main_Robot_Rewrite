package com.swrobotics.bert.control;

import static com.swrobotics.bert.constants.InputConstants.*;

import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.Utils;

// Note: The Y axes on the sticks are backwards from what you would expect: up is negative
public final class Input implements Subsystem {
    private final Controller drive;
    private final Controller manipulator;

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

    public boolean getAim() {
        return manipulator.rightShoulder.isPressed() || drive.rightShoulder.isPressed();
    }

    public boolean getSlowMode() {
        return drive.leftShoulder.isPressed();
    }

    /* Manipulator */
    public boolean getToggleIntake() {
        return manipulator.y.leadingEdge();

    }

    public boolean getEject() {
        return manipulator.start.leadingEdge();
    }

    public double getTeleDistance() {
        return Utils.clamp(deadzone(manipulator.leftStickY.get()), 0, 1);
    }

    public double getRotAngle() {
        return Utils.map(deadzone(manipulator.leftStickX.get()), -1, 1, 60, 120);
    }

    public boolean getClimberManualOverride() {
        return manipulator.select.isPressed();
    }

    public double getTeleManual() {
        return deadzone(manipulator.leftStickY.get());
    }

    public double getRotManual() {
        return deadzone(manipulator.leftStickX.get()) * 0.25;
    }

    public boolean getShoot() {
        return manipulator.a.leadingEdge();
    }

    public boolean getClimberNextStep() {
        return manipulator.x.isPressed();
    }

    public boolean getClimberPreviousStep() {
        return manipulator.b.leadingEdge();
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

    public boolean getFollowPath() {
        return manipulator.start.isPressed();
    }

    @Override
    public void robotPeriodic() {
        drive.update();
        manipulator.update();
    }
}
