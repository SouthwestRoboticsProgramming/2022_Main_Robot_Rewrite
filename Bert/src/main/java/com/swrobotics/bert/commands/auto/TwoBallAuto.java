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

public class TwoBallAuto extends CommandSequence {
    public TwoBallAuto(RobotContainer robot) {
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
        
        /* Target Ball Commands */
        TargetAngleCommand targetThree = new TargetAngleCommand(drive, loc, () -> loc.getAngleToBall(BLUE_3).getDegrees(), 2);

        append(new IntakeSetCommand(intake, Intake.State.ON));
        append(new TurnToAngleCommand(drive, loc, () -> loc.getAngleToBall(BLUE_3).getDegrees()));
        append(targetThree);
        append(new DriveToPointCommand(msg, path, BLUE_3.getX(), BLUE_3.getY(), 5));
        targetThree.stop();
        append(new TurnTowardsTargetCommand(drive, loc));
        append(new WaitCommand(3));
        append(new ShootCommand(hopper, input)); // Stored ball
        append(new WaitCommand(3));
        append(new ShootCommand(hopper, input)); // Blue 1
    }
}
