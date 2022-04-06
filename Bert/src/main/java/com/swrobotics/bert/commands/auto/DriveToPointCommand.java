package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.auto.Pathfinding;
import com.swrobotics.messenger.client.MessengerClient;

import static com.swrobotics.bert.constants.Constants.*;

public final class DriveToPointCommand implements Command {
    private final MessengerClient msg;
    private final Pathfinding pathfinder;
    private final double x;
    private final double y;

    private int timeout;
    private double timeoutSeconds;

    public DriveToPointCommand(MessengerClient msg, Pathfinding pathfinder, double x, double y, double timeoutSeconds) {
        this.msg = msg;
        this.pathfinder = pathfinder;
        this.x = x;
        this.y = y;
        this.timeoutSeconds = timeoutSeconds;
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

        if (!pathfinder.isAtPathTarget()) {timeout++;}
        if (timeout / (double) PERIODIC_PER_SECOND >= timeoutSeconds) {return true;} //  Prevents whole auto failing because path can't be created
        return pathfinder.isAtPathTarget();
    }

    @Override
    public void end() {
        
    }
}
