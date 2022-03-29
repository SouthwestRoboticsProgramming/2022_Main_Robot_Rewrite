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
            return Math.max(98.4 * distance + 1584, 2058.75);
        } else {
            return -1000; // do something useless because TODO
        }
    }

    @Override
    public void robotPeriodic() {

        // boolean shootHigh = AIM_HIGH_GOAL.get();
        // double distance = 200; /* TODO: Bert */
        // double hoodAngle = calculateHood(distance, shootHigh);
        // double rpm = calculateRPM(distance, shootHigh, hoodAngle);

        double distance = loc.getDistanceToTarget(); // TODO

        // HOOD
        hood.setPosition(calculateHood(distance, AIM_HIGH_GOAL.get()));
        flywheel.setFlywheelSpeed(calculateRPM(distance, AIM_HIGH_GOAL.get()));

        if (input.getShoot() && (shoot == null || !Scheduler.get().isCommandRunning(shoot))) {
            Scheduler.get().addCommand(shoot = new ShootCommand(hopper));
            hood.calibrate();
        }
    }
}

