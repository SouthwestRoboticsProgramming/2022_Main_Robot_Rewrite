package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

import java.util.function.DoubleSupplier;

import static com.swrobotics.bert.constants.Constants.*;

public class TargetAngleCommand implements Command {
    private final SwerveDriveController drive;
    protected final Localization loc;
    private final DoubleSupplier supplier;
    private int timeoutSeconds;

    private boolean stop;

    public TargetAngleCommand(SwerveDriveController drive, Localization loc, DoubleSupplier supplier, int timoutSeconds) {
        this.drive = drive;
        this.loc = loc;
        this.supplier = supplier;
        this.timeoutSeconds = timeoutSeconds * PERIODIC_PER_SECOND;
        stop = false;
    }

    public void stop() {
        stop = true;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        drive.turnToAngle(supplier.getAsDouble());

        timeoutSeconds--;



        return timeoutSeconds <= 0 || stop;
    }

    @Override
    public void end() {

    }
}
