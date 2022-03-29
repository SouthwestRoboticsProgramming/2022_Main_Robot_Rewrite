package com.swrobotics.bert.subsystems.intake;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.intake.IntakeEjectCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.subsystems.shooter.Hopper;

public final class IntakeController implements Subsystem {
    private final Input input;
    private final Intake intake;
    private final Hopper hopper;

    public IntakeController(Input input, Intake intake, Hopper hopper) {
        this.input = input;
        this.intake = intake;
        this.hopper = hopper;
    }

    @Override
    public void teleopPeriodic() {
        if (input.getToggleIntake()) {
            // intake.setState(intake.getState() != Intake.State.OFF ? Intake.State.ON : Intake.State.OFF);
            if (intake.getState() == Intake.State.OFF) { 
                intake.setState(Intake.State.ON);} else {
                    intake.setState(Intake.State.OFF);
                }
        }

        if (input.getEject()) {
            Scheduler.get().addCommand(new IntakeEjectCommand(intake, hopper));
        }
    }
}
