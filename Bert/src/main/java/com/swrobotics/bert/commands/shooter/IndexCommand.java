package com.swrobotics.bert.commands.shooter;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.shooter.Hopper;

import static com.swrobotics.bert.constants.Constants.*;

public final class IndexCommand implements Command {
    private final Hopper hopper;
    private final double speed;
    private int timer;

    public IndexCommand(Hopper hopper, double speed, double time) {
        this.hopper = hopper;
        this.speed = speed;
        timer = (int) (time * PERIODIC_PER_SECOND);
    }

    @Override
    public void init() {
    }

    @Override
    public boolean run() {
        hopper.setIndexSpeed(speed);

        return --timer <= 0;
    }

    @Override
    public void end() {
        hopper.setIndexSpeed(0);
    }
}
