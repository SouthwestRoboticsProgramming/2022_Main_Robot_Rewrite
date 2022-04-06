package com.swrobotics.bert.commands.auto.six;

import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.WaitCommand;
import com.swrobotics.bert.commands.auto.DriveToPointCommand;
import com.swrobotics.bert.commands.auto.TurnToAngleCommand;
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

import com.swrobotics.bert.RobotContainer;

public final class SixBallOne extends CommandSequence {
    public SixBallOne(RobotContainer robot) {
        Intake intake = robot.intake;
        Localization loc = robot.localization;
        SwerveDriveController drive = robot.driveController;
        MessengerClient msg = robot.msg;
        Pathfinding path = robot.pathfinding;
        Hopper hopper = robot.hopper;
        Input input = robot.input;

        append(new IntakeSetCommand(intake, Intake.State.ON));                                // Intake on
        append(new TurnToAngleCommand(drive, loc, loc.getAngleToBall(BLUE_3).getDegrees()));  // Turn to ball 3
        append(new DriveToPointCommand(msg, path, BLUE_3.getX(),BLUE_3.getY(), 5.0));         // Drive to ball 3
        append(new WaitCommand(0.2));
        append(new TurnToAngleCommand(drive, loc, loc.getAngleToTarget().getDegrees()));      // Turn to target
        append(new ShootCommand(hopper, input));                                              // Shoot
        append(new ShootCommand(hopper, input));                                              // Shoot
        append(new TurnToAngleCommand(drive, loc, loc.getAngleToBall(BLUE_7).getDegrees()));  // Turn to terminal ball
        append(new DriveToPointCommand(msg, path, BLUE_7.getX(), BLUE_7.getY(), 5.0));        // Drive to terminal ball
        append(new WaitCommand(3.0));                                                         // Wait for human player to feed in ball
        append(new DriveToPointCommand(msg, path, BLUE_2.getX() - 0.5, BLUE_2.getY(), 5.0));  // Drive next to ball 2
        append(new TurnToAngleCommand(drive, loc, loc.getAngleToTarget().getDegrees()));      // Turn to target
        append(new ShootCommand(hopper, input));                                              // Shoot
        append(new ShootCommand(hopper, input));                                              // Shoot
        append(new TurnToAngleCommand(drive, loc, loc.getAngleToBall(BLUE_2).getDegrees()));  // Turn to ball 2
        append(new DriveToPointCommand(msg, path, BLUE_2.getX(), BLUE_2.getY(), 5.0));        // Pick up ball 2
        append(new TurnToAngleCommand(drive, loc, loc.getAngleToBall(BLUE_1).getDegrees()));  // Turn to ball 1
        append(new DriveToPointCommand(msg, path, BLUE_1.getX() - 1, BLUE_1.getY(), 5.0));        // Pick up ball 1
        append(new TurnToAngleCommand(drive, loc, loc.getAngleToTarget().getDegrees()));      // Turn to target
        append(new ShootCommand(hopper, input));                                              // Shoot
        append(new ShootCommand(hopper, input));                                              // Shoot
    }
}
