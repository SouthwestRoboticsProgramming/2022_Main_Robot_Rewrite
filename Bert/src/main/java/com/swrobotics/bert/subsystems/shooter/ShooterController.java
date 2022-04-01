package com.swrobotics.bert.subsystems.shooter;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.shooter.ShootCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.util.Utils;

import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class ShooterController implements Subsystem {
    private final Input input;
    private final Hopper hopper;
    private final Flywheel flywheel;
    private final NewHood hood;
    private final Localization loc;
    private ShootCommand shoot;

    public ShooterController(Input input, Hopper hopper, Flywheel flywheel, NewHood hood, Localization loc) {
        this.input = input;
        this.hopper = hopper;
        this.flywheel = flywheel;
        this.hood = hood;
        this.loc = loc;

        shoot = null;
    }

    private double calculateHood(double distance, boolean highGoal) {
        // Mathy stuff here
        if (highGoal) {
            return Utils.map(distance, 1, 18, 0, 3); // Remap min - max distance to min - max hood
        } else {
            if (distance > 2000 /*Medium hood*/) { return 3; }
            if (distance > 1000 /*Low hood*/) { return 2; }
            if (distance > 500 /*Lower hood*/) { return 1; }
            return 0; /*Lowest hood*/
        }
    }

    private double calculateRPM(double distance, boolean highGoal) {
        if (highGoal) {
            if (distance > 16) { return 75 * distance + 1584;}
            return Math.max(98.4 * distance + 1584, 2058.75);
        } else {
            return -1000; // do something useless because TODO
        }
    }
//eat foos
    @Override
    public void robotPeriodic() {

        double distance = loc.getDistanceToTarget();
        distance -= 1; // Account for offset from center of hub
        distance *= 3.28024; // Convert meters to feet
        distance -= 1.29166666666666; // Account for robot center to front dist

        // System.out.println("Distance: " + distance);

        // System.out.println("Local angle: " + loc.getLocalAngleToTarget());

        // HOOD
        if (loc.isLookingAtTarget() || input.getAim() || input.getAimOverride()) {
//            System.out.println("I'm supposed to be shooting");
            hood.setPosition(calculateHood(distance, AIM_HIGH_GOAL.get()));
            flywheel.setFlywheelSpeed(calculateRPM(distance, AIM_HIGH_GOAL.get()));
            // System.out.println("Full speed");
        } else if (hopper.isBallDetected()){
            hood.calibrate();
            flywheel.setFlywheelSpeed(FLYWHEEL_IDLE_SPEED.get());
            // System.out.println("Idle speed");
        }else { // Don't spin the falcon when there is no ball
            hood.calibrate();
            flywheel.setFlywheelSpeed(0);
            // System.out.println("No speed");
        }

        if (input.getShoot() && (shoot == null || !Scheduler.get().isCommandRunning(shoot))) {
            Scheduler.get().addCommand(shoot = new ShootCommand(hopper, input));
        }
    }
}

