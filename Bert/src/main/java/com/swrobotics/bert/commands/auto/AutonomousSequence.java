package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.subsystems.auto.Pathfinding;
import com.swrobotics.messenger.client.MessengerClient;

public final class AutonomousSequence extends CommandSequence {
    public AutonomousSequence(MessengerClient msg, Pathfinding path) {
        append(new DriveToPointCommand(msg, path, 0, -5));
    }
}
