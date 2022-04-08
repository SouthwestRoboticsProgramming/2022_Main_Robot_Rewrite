package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;

import java.util.function.DoubleSupplier;

public class TurnToAngleCommand implements Command {
    private final SwerveDriveController drive;
    protected final Localization loc;
    private final DoubleSupplier supplier;

    public TurnToAngleCommand(SwerveDriveController drive, Localization loc, DoubleSupplier supplier) {
        this.drive = drive;
        this.loc = loc;
        this.supplier = supplier;
    }

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        drive.turnToAngle(supplier.getAsDouble());

        boolean atAngle = drive.isAtTargetAngle();
        // System.out.println("At angle: " + atAngle + " target: " + angle + " current: " + drive.getAutoAngle());
        return atAngle;
    }

    @Override
    public void end() {

    }
}
