package com.swrobotics.bert.subsystems.shooter;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.shooter.ShootCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.shuffle.TemporaryDouble;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.shuffle.TuneGroup;
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
            double hoodPos = Utils.map(distance, 1, 18, 0, 3); // Remap min - max distance to min - max hood
            if (distance < 10) { hoodPos -= 0.5; }
            return hoodPos;

        } else {
            if (distance > 2000 /*Medium hood*/) { return 3; }
            if (distance > 1000 /*Low hood*/) { return 2; }
            if (distance > 500 /*Lower hood*/) { return 1; }
            return 0; /*Lowest hood*/
        }
    }

    // Keys are distance in feet according to distance parameter
    // Values are RPM
    private final double[] RPM_TABLE_KEYS   = {2.95, 5.4,  7.3,  9.58};
    private final double[] RPM_TABLE_VALUES = {2275, 2450, 2590, 2680};

    TunableDouble RPM_TUNE = new TuneGroup("tests", ShuffleBoard.shooterTab).getDouble("RPM", 0);
    private double calculateRPM(double distance, boolean highGoal) {
        System.out.println("Distance: " + distance);
        return RPM_TUNE.get();
    }
    // private double calculateRPM(double distance, boolean highGoal) {
    //     double lowerKey = 0, higherKey = 0;
    //     double lowerValue = 0, higherValue = 0;
    //     boolean lowerSet = false, higherSet = false;

    //     // Find where the distance fits into the table
    //     for (int i = 0; i < RPM_TABLE_KEYS.length; i++) {
    //         double key = RPM_TABLE_KEYS[i];
    //         double value = RPM_TABLE_VALUES[i];

    //         if (distance > key) {
    //             lowerKey = key;
    //             lowerValue = value;
    //             lowerSet = true;
    //         } else if (distance < key) {
    //             higherKey = key;
    //             higherValue = value;
    //             higherSet = true;
    //             break;
    //         } else if (distance == key) {
    //             lowerKey = higherKey = key;
    //             lowerValue = higherValue = value;
    //             lowerSet = higherSet = true;
    //             break;
    //         }
    //     }

    //     double rpm;
    //     if (!lowerSet) {
    //         // If closer than the closest point, clamp to closest point
    //         // TODO: Shoot to low goal
    //         rpm = RPM_TABLE_VALUES[0];
    //     } else if (!higherSet) {
    //         // If farther than the farthest point, approximate using average slope of the table

    //         double lastKey = RPM_TABLE_KEYS[RPM_TABLE_KEYS.length - 1];
    //         double lastVal = RPM_TABLE_VALUES[RPM_TABLE_VALUES.length - 1];

    //         double rise = RPM_TABLE_VALUES[0] - lastVal;
    //         double run = RPM_TABLE_KEYS[0] - lastKey;
    //         double slope = rise / run;

    //         rpm = lastVal + slope * (distance - lastKey);
    //     } else {
    //         // If value is within the table, lerp between neighboring entries
    //         rpm = Utils.map(distance, lowerKey, higherKey, lowerValue, higherValue);
    //     }

    //     return rpm;
    // }
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

