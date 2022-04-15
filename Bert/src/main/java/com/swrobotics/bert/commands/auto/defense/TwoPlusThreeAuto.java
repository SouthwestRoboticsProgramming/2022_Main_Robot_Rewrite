package com.swrobotics.bert.commands.auto.defense;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.WaitCommand;
import com.swrobotics.bert.commands.intake.IntakeEjectCommand;
import com.swrobotics.bert.commands.intake.IntakeSetCommand;
import com.swrobotics.bert.commands.shooter.ShootCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.auto.Pathfinding;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;
import com.swrobotics.bert.subsystems.intake.Intake;
import com.swrobotics.bert.subsystems.shooter.Hopper;
import com.swrobotics.messenger.client.MessengerClient;

import static com.swrobotics.bert.constants.ball.BallLocationConstants.*;

public class TwoPlusThreeAuto extends CommandSequence {
    public TwoPlusTwoAuto(RobotContainer robot) {
        Intake intake = robot.intake;
        Localization loc = robot.localization;
        SwerveDriveController drive = robot.driveController;
        MessengerClient msg = robot.msg;
        Pathfinding path = robot.pathfinding;
        Hopper hopper = robot.hopper;
        Input input = robot.input;

        // Sequence:
        // 1. Turn intake on
        // 2. Turn towards and drive to Blue 1
        // 3. Turn towards the target
        // 4. Shoot blue 1 and stored ball
        // 5. Turn towards and drive to red 5
        // 6. Turn towards and drive to red 6
        // 7. Drive to hanger
        // 8. Dispense balls into corner
        // 9. Turn towards and drive to red 4
        // 10. Turn towards hub
        // 11. Eject ball towards hub
        
        /* Target Ball Commands */
        TargetAngleCommand targetThree = new TargetAngleCommand(drive, loc, () -> loc.getAngleToBall(BLUE_3).getDegrees(), 2);
        TargetAngleCommand targetRed5 = new TargetAngleCommand(drive, loc, () -> loc.getAngleToBall(RED_5).getDegrees(), 2);
        TargetAngleCommand targetRed6 = new TargetAngleCommand(drive, loc, () -> loc.getAngleToBall(RED_6).getDegrees(), 2);
        TargetAngleCommand targetRed4 = new TargetAngleCommand(drive, loc, () -> loc.getAngleToBall(RED_4).getDegrees(), 2);

        append(new IntakeSetCommand(intake, Intake.State.ON));
        append(new TurnToAngleCommand(drive, loc, () -> loc.getAngleToBall(BLUE_3).getDegrees()));
        append(targetThree);
        append(new DriveToPointCommand(msg, path, BLUE_3.getX(), BLUE_3.getY(), 5));
        append(() -> {
            targetThree.stop();
            return true;
        });
        append(new TurnTowardsTargetCommand(drive, loc));
        append(new WaitCommand(3));
        append(new ShootCommand(hopper, input)); // Stored ball
        append(new WaitCommand(3));
        append(new ShootCommand(hopper, input)); // Blue 1

        append(new TurnToAngleCommand(drive, loc, () -> loc.getAngleToBall(RED_5).getDegrees()));
        append(targetRed5);
        append(new DriveToPointCommand(msg, path, RED_5.getX(), RED_5.getY(), 6));
        append(() -> {
            targetRed5.stop();
            return true;
        });

        append(new TurnToAngleCommand(drive, loc, () -> loc.getAngleToBall(RED_5).getDegrees()));
        append(targetRed6);
        append(new DriveToPointCommand(msg, path, RED_6.getX(), RED_6.getY(), 6));
        append(() -> {
            targetRed6.stop();
            return true;
        });

        append(new DriveToPointCommand(msg, path, -2.581, -4.294, 5));
        append(new TurnToAngleCommand(drive, loc, () -> 180));
        append(new IntakeEjectCommand(intake, hopper));
        append(new IntakeEjectCommand(intake, hopper));

        append(new TurnToAngleCommand(drive, loc, () -> loc.getAngleToBall(RED_4).getDegrees()));
        append(targetRed4);
        append(new DriveToPointCommand(msg, path, RED_4.getX(), RED_4.getY(), 6));
        append(() -> {
            targetRed4.stop();
            return true;
        });

        append(new TurnTowardsTargetCommand(drive, loc));
        append(new IntajeEjectCommand(intake, hopper));


    }
}
