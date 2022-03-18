package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.climber.Climber;
import com.swrobotics.bert.subsystems.climber.ClimberController;

import static com.swrobotics.bert.constants.Constants.PERIODIC_PER_SECOND;

public final class ResetClimberCommand implements Command {
    private static ClimberController controller;

    private final Climber climber;
    private final Input input;

    private int timer;

    public ResetClimberCommand(Climber climber, Input input) {
        this.climber = climber;
        this.input = input;

        timer = 7 * PERIODIC_PER_SECOND;
    }

    @Override
    public void init() {
        if (controller != null) {
            Scheduler.get().removeSubsystem(controller);
            controller = null;
        }
    }

    @Override 
    public boolean run() {
        climber.manualMove(-0.1, -0.1);

        return --timer <= 0;
    }

    @Override
    public void end() {
        climber.zero();
        Scheduler.get().addSubsystem(controller = new ClimberController(input, climber));
    }
}
