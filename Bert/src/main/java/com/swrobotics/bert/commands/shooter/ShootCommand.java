package com.swrobotics.bert.commands.shooter;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.subsystems.shooter.Hopper;

import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class ShootCommand extends CommandSequence {
    public ShootCommand(Hopper hopper) {
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
    }
}
