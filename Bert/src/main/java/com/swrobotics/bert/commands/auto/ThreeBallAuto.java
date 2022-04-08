package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.CommandUnion;
import com.swrobotics.bert.commands.WaitCommand;
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

public class ThreeBallAuto extends CommandSequence {
    public ThreeBallAuto(RobotContainer robot) {
        Intake intake = robot.intake;
        Localization loc = robot.localization;
        SwerveDriveController drive = robot.driveController;
        MessengerClient msg = robot.msg;
        Pathfinding path = robot.pathfinding;
        Hopper hopper = robot.hopper;
        Input input = robot.input;

        // Sequence:
        // 1. Turn intake on
        // 2. Turn towards and drive to Blue 3
        // 3. Turn towards the target
        // 4. Shoot blue 3 and stored ball
        // 5. Drive to a point to miss red ball
        // 6. Turn towards and drive to Blue 2
        // 7. Turn towards target
        // 8. Shoot Blue 2

        append(new IntakeSetCommand(intake, Intake.State.ON));
        append(new CommandUnion(
            new TurnToAngleCommand(drive, loc, () -> loc.getAngleToBall(BLUE_3).getDegrees()),
            new DriveToPointCommand(msg, path, BLUE_3.getX(), BLUE_3.getY(), 5)
        ));
        append(new TurnTowardsTargetCommand(drive, loc));
        append(new WaitCommand(1.2));
        append(new ShootCommand(hopper, input)); // Stored ball
        append(new WaitCommand(1.2));
        append(new ShootCommand(hopper, input)); // Blue 1
        append(new DriveToPointCommand(msg, path, 0, -4.8, 5));
        append(new CommandUnion(
            new TurnToAngleCommand(drive, loc, () -> loc.getAngleToBall(BLUE_2).getDegrees()),
            new DriveToPointCommand(msg, path, BLUE_2.getX(), BLUE_2.getY(), 5)
        ));
        append(new TurnTowardsTargetCommand(drive, loc));
        append(new WaitCommand(1.2));
        append(new ShootCommand(hopper, input));
    }
}
