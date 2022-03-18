package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.climber.Climber;
import com.swrobotics.bert.subsystems.climber.ClimberState;

public final class ClimberStep implements Command {
    private final Climber climber;
    private final ClimberState state;

    public ClimberStep(Climber climber, double telePercent, double rotAngle, boolean loaded) {
        this.climber = climber;
        state = new ClimberState(telePercent, rotAngle, loaded);
    }

    @Override
    public void init() {
        
    }

    @Override
    public boolean run() {
        climber.setTargetState(state);

        return false;
    }

    @Override
    public void end() {
        
    }
}
