package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.shuffle.TunableDouble;
import com.swrobotics.bert.subsystems.climber.Climber;
import com.swrobotics.bert.subsystems.climber.ClimberState;

public final class ClimberStep implements Command {
    private final Climber climber;
    // private final ClimberState state;
    private final TunableDouble telePercent, rotAngle;
    private final boolean loaded;

    public ClimberStep(Climber climber, TunableDouble telePercent, TunableDouble rotAngle, boolean loaded) {
        this.climber = climber;
        this.telePercent = telePercent;
        this.rotAngle = rotAngle;
        this.loaded = loaded;
        // state = new ClimberState(telePercent, rotAngle, loaded);
    }

    @Override
    public void init() {
        
    }

    @Override
    public boolean run() {
        climber.setTargetState(new ClimberState(telePercent.get(), rotAngle.get(), loaded));

        return false;
    }

    @Override
    public void end() {
        System.out.println("ClimberStep.end()");
        
    }
}
