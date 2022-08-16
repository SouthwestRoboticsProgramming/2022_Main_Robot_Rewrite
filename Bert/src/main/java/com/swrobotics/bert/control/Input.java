package com.swrobotics.bert.control;

import static com.swrobotics.bert.constants.InputConstants.*;

import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.Utils;
import com.team2129.lib.math.Angle;
import com.team2129.lib.math.Vec2d;
import com.team2129.lib.utils.InputUtils;

// Note: The Y axes on the sticks are backwards from what you would expect: up is negative
public final class Input implements Subsystem {
    private final Controller drive;
    private final Controller manipulator;

    public Input() {
        drive = new XboxController(DRIVE_CONTROLLER_ID);
        manipulator = new XboxController(MANIPULATOR_CONTROLLER_ID);
    }

    public void setRumble(boolean rumble) {
        drive.setRumble(rumble);
        manipulator.setRumble(rumble);
    }

    private static final double DEADBAND = 0.2;

    /* Drive */
    public Vec2d getDriveTranslation() {
        // Apply deadband
        double x = InputUtils.applyDeadband(drive.leftStickX.get(), DEADBAND);
        double y = -InputUtils.applyDeadband(drive.leftStickY.get(), DEADBAND);

        System.out.println(new Vec2d(x, y).mul(2));

        return new Vec2d(x, y).mul(2);
    }

    public Angle getDriveRotation() {
        return Angle.cwRad(InputUtils.applyDeadband(drive.rightStickX.get(), DEADBAND) * Math.PI);
    }

    public boolean getFieldRelative() {
        return !(drive.leftTrigger.get() > 1.0 - DEADBAND);
    }

    public boolean getSlowMode() {
        return drive.rightShoulder.isPressed();
    }

    
    
    /* Manipulator */
    public boolean getToggleIntake() {
        return manipulator.y.leadingEdge();
    }
    
    public boolean getShoot() {
        return manipulator.a.leadingEdge();
    }
    
    public boolean getEject() {
        return manipulator.start.leadingEdge();
    }

    public boolean getAim() { // Both drive an manipulator
        return drive.rightShoulder.isPressed() || manipulator.rightShoulder.isPressed();
    }

    public boolean getAimOverride() { // Both drive and manipulator
        return drive.select.leadingEdge() || manipulator.select.leadingEdge();
    }
    
    /* Climb */
    public boolean getClimberNextStep() {
        return manipulator.x.isPressed();
    }

    public boolean getClimberPreviousStep() {
        return manipulator.b.leadingEdge();
    }


    /* Temporary */
    public boolean getFollowPath() {
        return manipulator.start.isPressed();
    }


    
    private double deadzone(double amount) {
        if (Math.abs(amount) < JOYSTICK_DEAD_ZONE.get()) {
            return 0;
        }
        return Math.signum(amount) * Utils.map(Math.abs(amount), JOYSTICK_DEAD_ZONE.get(), 1, 0, 1);
    }


    @Override
    public void robotPeriodic() {
        drive.update();
        manipulator.update();
    }

    @Override
    public void disabledInit() {
        setRumble(false);
    }
}
