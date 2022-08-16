package com.swrobotics.bert;

import com.swrobotics.bert.commands.MessengerReadCommand;
import com.swrobotics.bert.commands.PublishLocalizationCommand;
import com.swrobotics.bert.commands.taskmanager.TaskManagerSetupCommand;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.good_drive.drive.BadCode;
import com.swrobotics.bert.subsystems.Lights;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.PDP;
import com.swrobotics.bert.subsystems.camera.CameraController;
import com.swrobotics.bert.subsystems.camera.Limelight;
import com.swrobotics.bert.subsystems.climber.Climber;
import com.swrobotics.bert.subsystems.intake.Intake;
import com.swrobotics.bert.subsystems.intake.IntakeController;
import com.swrobotics.bert.subsystems.shooter.BallDetector;
import com.swrobotics.bert.subsystems.shooter.Flywheel;
import com.swrobotics.bert.subsystems.shooter.Hopper;
import com.swrobotics.bert.subsystems.shooter.NewHood;
import com.swrobotics.bert.subsystems.shooter.ShooterController;
import com.swrobotics.messenger.client.MessengerClient;
import com.swrobotics.taskmanager.api.TaskManagerAPI;

import edu.wpi.first.wpilibj.SPI;

import static com.swrobotics.bert.constants.CommunicationConstants.*;
import static com.swrobotics.bert.constants.SubsystemConstants.*;

import java.io.IOException;

import com.kauailabs.navx.frc.AHRS;

public final class RobotContainer {
    // Messenger
    public final MessengerClient msg;
    public final TaskManagerAPI raspberryPi;

    // Sensors
    public final AHRS gyro;

    public final Input input;
    public final Limelight limelight;
    public final Localization localization;
    public final CameraController cameraController;
    public final BallDetector ballDetector;
    public final Hopper hopper;
    public final Flywheel flywheel;
    public final NewHood hood;
    public final Intake intake;
    public final IntakeController intakeController;
    public final ShooterController shooterController;
    public final Climber climber;
    public final Lights lights;
    public final PDP pdp;
    public final BadCode badCode;

    // Messenger-dependent subsystems

    public RobotContainer() {
        msg = null;
        raspberryPi = null;

        // Sensors
        {
            gyro = new AHRS(SPI.Port.kMXP, (byte) 200);
        }

        // Subsystems
        {
            input = new Input();
            sleep();
            badCode = new BadCode(input, gyro);
            limelight = new Limelight();
            sleep();
            localization = new Localization(gyro, limelight, msg, input);
            sleep();
            pdp = new PDP();
            sleep();
            cameraController = new CameraController(limelight, localization, input, pdp);
            sleep();
            ballDetector = new BallDetector();
            sleep();
            hopper = new Hopper(ballDetector, input);
            sleep();
            flywheel = new Flywheel();
            sleep();
            hood = new NewHood();
            sleep();
            intake = new Intake(input);
            sleep();
            intakeController = new IntakeController(input, intake, hopper);
            sleep();
            shooterController = new ShooterController(input, hopper, flywheel, hood, localization);
            sleep();
            climber = new Climber(input, gyro);
            sleep();
            lights = new Lights();
            sleep();
        }


        // Switches
        {
            new SubsystemSwitch(input,             ENABLE_INPUT);
            new SubsystemSwitch(limelight,         ENABLE_LIMELIGHT);
            new SubsystemSwitch(localization,      ENABLE_LOCALIZATION);
            new SubsystemSwitch(cameraController,  ENABLE_CAMERA_CONTROLLER);
            new SubsystemSwitch(ballDetector,      ENABLE_BALL_DETECTOR);
            new SubsystemSwitch(hopper,            ENABLE_HOPPER);
            new SubsystemSwitch(flywheel,          ENABLE_FLYWHEEL);
            new SubsystemSwitch(hood,              ENABLE_HOOD);
            new SubsystemSwitch(intake,            ENABLE_INTAKE);
            new SubsystemSwitch(intakeController,  ENABLE_INTAKE_CONTROLLER);
            new SubsystemSwitch(shooterController, ENABLE_SHOOTER_CONTROLLER);
            new SubsystemSwitch(climber,           ENABLE_CLIMBER);
            new SubsystemSwitch(lights,            ENABLE_LIGHTS);
            new SubsystemSwitch(pdp,               ENABLE_PDP);
            new SubsystemSwitch(badCode, ENABLE_DRIVE);

        }
    }

    private void sleep() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private MessengerClient connectToMessenger() {
        MessengerClient msg = null;

        for (int i = 0; i < MESSENGER_CONNECT_MAX_ATTEMPTS && msg == null; i++) {
            try {
                msg = new MessengerClient(
                    MESSENGER_HOST,
                    MESSENGER_PORT,
                    MESSENGER_NAME
                );
            } catch (IOException e) {
                System.out.println("Messenger connection failed, trying again");
            }

            try {
                Thread.sleep(MESSENGER_CONNECT_RETRY_INTERVAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return msg;
    }
}
