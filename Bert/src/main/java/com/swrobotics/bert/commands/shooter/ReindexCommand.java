package com.swrobotics.bert.commands.shooter;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.shooter.Hopper;

import static com.swrobotics.bert.constants.Constants.*;
import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class ReindexCommand implements Command {
    private final Hopper hopper;
    private int timer;

    public ReindexCommand(Hopper hopper) {
        this.hopper = hopper;

        timer = (int) (INDEX_REINDEX_TIME.get() * PERIODIC_PER_SECOND);
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        hopper.setIndexSpeed(INDEX_REINDEX_SPEED.get());
        // System.out.println("Timer: " + timer + " Ball Not Detected: " + !hopper.isBallDetected());
        timer--;

        return timer <= 0 && !hopper.isBallDetected();
    }

    @Override
    public void end() {
        hopper.setIndexSpeed(0);
    }
}
