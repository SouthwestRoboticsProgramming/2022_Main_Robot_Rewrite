package com.swrobotics.bert.commands.shooter;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.WaitCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.shooter.Hopper;

import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class ShootCommand extends CommandSequence {
    private final Hopper hopper;
    private final Input input;
    private int repeatCount;

    public ShootCommand(Hopper hopper, Input input) {
        this(hopper, input, 0);
    }

    private ShootCommand(Hopper hopper, Input input, int repeatCount) {
        this.hopper = hopper;
        this.input = input;
        this.repeatCount = repeatCount;

        // Kick the ball back a bit
        append(new IndexCommand(
                hopper,
                INDEX_KICKBACK_SPEED.get(),
                INDEX_KICKBACK_TIME.get()
        ));

        // Push the ball into the shooter
        append(new IndexCommand(
                hopper,
                INDEX_SHOOT_SPEED.get(),
                INDEX_SHOOT_TIME.get()
        ));

        append(new WaitCommand(0.5));

        append(new ReindexCommand(
                hopper
        ));
    }

    @Override
    public boolean run() {
        if (input.getShoot())
            repeatCount++;

        return super.run();
    }

    @Override
    public void end() {
        super.end();

        if (repeatCount > 0) {
            // now do it again
            Scheduler.get().addCommand(new ShootCommand(hopper, input, repeatCount - 1));
        }
    }
}
