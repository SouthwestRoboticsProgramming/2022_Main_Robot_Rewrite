package com.swrobotics.bert.subsystems.climber;

import com.swrobotics.bert.Scheduler;
import com.swrobotics.bert.commands.climber.ClimberSequence;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;
import com.kauailabs.navx.frc.AHRS;

public final class ClimberController implements Subsystem {
    private final Input input;
    private final Climber climber;
    private final AHRS gyro;

    public ClimberController(Input input, Climber climber, AHRS gyro) {
        this.input = input;
        this.climber = climber;
        this.gyro = gyro;
    }

    @Override
    public void teleopInit() {
        Scheduler sch = Scheduler.get();
        if (hasStartedCommand) {
            sch.cancelCommand(command);
            sch.removeSubsystem(this);
            System.out.println("Canceled the climber sequence command");
        }
    }

    boolean hasStartedCommand = false;
    ClimberSequence command = null;
    @Override
    public void teleopPeriodic() {
        if (!hasStartedCommand) {
            Scheduler.get().addCommand(command = new ClimberSequence(climber, input, gyro));
            hasStartedCommand = true;
        }

        // if (input.getClimberManualOverride()) {
        //     // climber.manualMove(input.getTeleManual(), input.getRotManual());
        // } else {
        //     // double telescopingDistance = input.getTeleDistance();
        //     // double rotatingAngle = input.getRotAngle();
        //     // boolean loaded = false;

        //     // ClimberState state = new ClimberState(telescopingDistance, rotatingAngle, loaded);

        //     // climber.setTargetState(state);
        // }
    }
}
