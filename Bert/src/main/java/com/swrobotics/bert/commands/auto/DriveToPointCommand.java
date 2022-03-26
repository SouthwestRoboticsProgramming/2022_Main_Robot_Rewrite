package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.auto.Pathfinding;
import com.swrobotics.messenger.client.MessengerClient;

public final class DriveToPointCommand implements Command {
    private final MessengerClient msg;
    private final Pathfinding pathfinder;
    private final double x;
    private final double y;

    public DriveToPointCommand(MessengerClient msg, Pathfinding pathfinder, double x, double y) {
        this.msg = msg;
        this.pathfinder = pathfinder;
        this.x = x;
        this.y = y;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        msg.builder("Pathfinder:SetTarget")
                .addDouble(x)
                .addDouble(y)
                .send();

        return pathfinder.isAtPathTarget();
    }

    @Override
    public void end() {

    }
}
