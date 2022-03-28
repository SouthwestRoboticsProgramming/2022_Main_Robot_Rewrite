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
    private final Localization localization;

    public ShooterController(Input input, Hopper hopper, Flywheel flywheel, NewHood hood, Localization localization) {
        this.input = input;
        this.hopper = hopper;
        this.flywheel = flywheel;
        this.hood = hood;
        this.localization = localization;
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
        // More smackrels of math here
        return 1000000; // ONE MILLION RPM!!!!!
    }

    @Override
    public void robotPeriodic() {

        // boolean shootHigh = AIM_HIGH_GOAL.get();
        // double distance = 200; /* TODO: Bert */
        // double hoodAngle = calculateHood(distance, shootHigh);
        // double rpm = calculateRPM(distance, shootHigh, hoodAngle);

        // HOOD
        hood.setPosition(calculateHood(HOOD_POSITION.get(), AIM_HIGH_GOAL.get()));
        // hood.setPosition(calculateHood(localization.getDistanceToTarget(), AIM_HIGH_GOAL.get()));
        //flywheel.setFlywheelSpeed(calculateRPM(localization.getDistanceToTarget(), AIM_HIGH_GOAL.get()));
        flywheel.setFlywheelSpeed(FLYWHEEL_RPM.get()); //FIXME: Replace me

        if (input.getShoot()) {
            Scheduler.get().addCommand(new ShootCommand(hopper));
            hood.calibrate();
        }
    }
}

