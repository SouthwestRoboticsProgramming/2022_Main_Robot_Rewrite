package com.swrobotics.bert.subsystems.shooter;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.shooter.ShootCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;

public final class ShooterController implements Subsystem {
    private final Input input;
    private final Hopper hopper;

    public ShooterController(Input input, Hopper hopper) {
        this.input = input;
        this.hopper = hopper;
    }

    @Override
    public void robotPeriodic() {
        if (input.getShoot()) {
            Scheduler.get().addCommand(new ShootCommand(hopper));
        }
    }
}
