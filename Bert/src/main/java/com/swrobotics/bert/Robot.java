package com.swrobotics.bert;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.commands.MessengerReadCommand;
import com.swrobotics.bert.commands.PublishLocalizationCommand;
import com.swrobotics.bert.commands.taskmanager.TaskManagerSetupCommand;
import com.swrobotics.bert.constants.Settings;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.profiler.ProfileNode;
import com.swrobotics.bert.profiler.Profiler;
import com.swrobotics.bert.shuffle.ShuffleBoard;
import com.swrobotics.bert.subsystems.Lights;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.PDP;
import com.swrobotics.bert.subsystems.auto.Autonomous;
import com.swrobotics.bert.subsystems.auto.Pathfinding;
import com.swrobotics.bert.subsystems.camera.CameraTurret;
import com.swrobotics.bert.subsystems.camera.CameraTurretController;
import com.swrobotics.bert.subsystems.camera.Cameras;
import com.swrobotics.bert.subsystems.camera.Limelight;
import com.swrobotics.bert.subsystems.climber.Climber;
import com.swrobotics.bert.subsystems.drive.SwerveDrive;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;
import com.swrobotics.bert.subsystems.intake.Intake;
import com.swrobotics.bert.subsystems.intake.IntakeController;
import com.swrobotics.bert.subsystems.shooter.*;
import com.swrobotics.messenger.client.MessengerClient;
import com.swrobotics.taskmanager.api.TaskManagerAPI;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.SPI;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.List;

import static com.swrobotics.bert.constants.CommunicationConstants.*;
import static com.swrobotics.bert.constants.Constants.PERIODIC_PER_SECOND;

public final class Robot extends RobotBase {
    private static final Robot INSTANCE = new Robot();

    public static Robot get() {
        return INSTANCE;
    }

    private boolean running;

    private MessengerClient msg = null;
    private TaskManagerAPI raspberryPi;
    private TaskManagerAPI jetsonNano;

    public boolean isMessengerConnected() {
        return msg != null;
    }

    private void init() {
        // Connect to Messenger
        int attempts;
        for (attempts = 0; attempts < MESSENGER_CONNECT_MAX_ATTEMPTS && msg == null; attempts++) {
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

        ShuffleBoard.show("Messenger connected", msg != null);
        ShuffleBoard.show("Messenger attempts", attempts);

        if (msg != null) {
            Scheduler.get().addCommand(new MessengerReadCommand(msg));

            // Connect to TaskManager instances
            raspberryPi = new TaskManagerAPI(msg, RASPBERRY_PI_PREFIX);
            // jetsonNano = new TaskManagerAPI(msg, JETSON_NANO_PREFIX);
            Scheduler.get().addCommand(new TaskManagerSetupCommand(raspberryPi, LIDAR_NAME, PATHFINDING_NAME));
            // Scheduler.get().addCommand(new TaskManagerSetupCommand(jetsonNano, VISION_NAME));
        }

        AHRS gyro = new AHRS(SPI.Port.kMXP, (byte) 200);

        Input input = new Input();
        SwerveDrive swerveDrive = new SwerveDrive(gyro);
        SwerveDriveController swerveDriveController = new SwerveDriveController(input, gyro, swerveDrive);
        CameraTurret cameraTurret = new CameraTurret();
        CameraTurretController cameraTurretController = new CameraTurretController(input, cameraTurret);
        Cameras cameras = new Cameras();
        Limelight limelight = new Limelight();
        Localization localization = new Localization(gyro, swerveDrive, limelight, msg);
        Intake intake = new Intake();
        IntakeController intakeController = new IntakeController(input, intake);
        BallDetector ballDetector = new BallDetector();
        Hopper hopper = new Hopper(ballDetector, input);
        Flywheel flywheel = new Flywheel();
        NewHood hood = new NewHood();
       ShooterController shooterController = new ShooterController(input, hopper, flywheel, hood, localization);
    //    Climber climber = new Climber(input, gyro);
        // Don't add ClimberController here, it is added after reset
        Lights lights = new Lights();
        PDP pdp = new PDP();

        Scheduler.get().addSubsystem(input);
        Scheduler.get().addSubsystem(swerveDrive);
        Scheduler.get().addSubsystem(swerveDriveController);
        Scheduler.get().addSubsystem(cameraTurret);
        Scheduler.get().addSubsystem(cameraTurretController);
        Scheduler.get().addSubsystem(cameras);
        Scheduler.get().addSubsystem(localization);
        Scheduler.get().addSubsystem(intake);
        Scheduler.get().addSubsystem(intakeController);
        Scheduler.get().addSubsystem(ballDetector);
        Scheduler.get().addSubsystem(hopper);
        Scheduler.get().addSubsystem(flywheel);
        Scheduler.get().addSubsystem(hood);
        Scheduler.get().addSubsystem(shooterController);
        // Scheduler.get().addSubsystem(climber);
        Scheduler.get().addSubsystem(limelight);
        Scheduler.get().addSubsystem(lights);
        Scheduler.get().addSubsystem(pdp);

        Pathfinding pathfinding = null;
        if (msg != null) {
            Scheduler.get().addCommand(new PublishLocalizationCommand(msg, localization));
            pathfinding = new Pathfinding(swerveDriveController, localization, input, msg);
            Scheduler.get().addSubsystem(pathfinding);
        }

        Autonomous auto = new Autonomous(swerveDriveController, msg, pathfinding);
        Scheduler.get().addSubsystem(auto);
    }

    @Override
    public void startCompetition() {
        running = true;

        init();
        Scheduler.get().robotInit();
        System.out.println("****** Robot program startup complete ******");

        HAL.observeUserProgramStarting();

        long lastTime = System.nanoTime();
        double secondsPerPeriodic = 1.0 / PERIODIC_PER_SECOND;
        double unprocessedTime = 0;

        RobotState lastState = RobotState.DISABLED;
        while (running && !Thread.currentThread().isInterrupted()) {
            long currentTime = System.nanoTime();
            unprocessedTime += (currentTime - lastTime) / 1_000_000_000.0;
            lastTime = currentTime;

            while (unprocessedTime > secondsPerPeriodic) {
                unprocessedTime -= secondsPerPeriodic;

                Profiler.get().beginMeasurements("Root");

                Profiler.get().push("Robot periodic");
                {
                    Scheduler.get().robotPeriodic();
                }
                Profiler.get().pop();

                RobotState state = getCurrentState();

                Profiler.get().push("State initialization");
                {
                    if (state != lastState) {
                        switch (state) {
                            case DISABLED:
                                Scheduler.get().disabledInit();
                                break;
                            case TELEOP:
                                Scheduler.get().teleopInit();
                                break;
                            case AUTONOMOUS:
                                Scheduler.get().autonomousInit();
                                break;
                            case TEST:
                                Scheduler.get().testInit();
                                break;
                        }
                    }
                    lastState = state;
                }
                Profiler.get().pop();

                Profiler.get().push("State periodic");
                {
                    switch (state) {
                        case DISABLED:
                            Scheduler.get().disabledPeriodic();
                            break;
                        case TELEOP:
                            Scheduler.get().teleopPeriodic();
                            break;
                        case AUTONOMOUS:
                            Scheduler.get().autonomousPeriodic();
                            break;
                        case TEST:
                            Scheduler.get().testPeriodic();
                            break;
                    }
                }
                Profiler.get().pop();

                Profiler.get().endMeasurements();
                if (Settings.DUMP_PROFILE_DATA.get()) {
                    ByteArrayOutputStream b = new ByteArrayOutputStream();
                    DataOutputStream out = new DataOutputStream(b);

                    try {
                        dumpProfileData(Profiler.get().getData(), out);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    msg.sendMessage("RoboRIO:ProfileData", b.toByteArray());
                }
            }
        }
    }

    @Override
    public void endCompetition() {
        running = false;
    }

    private RobotState getCurrentState() {
        if (isDisabled()) return RobotState.DISABLED;
        if (isAutonomous()) return RobotState.AUTONOMOUS;
        if (isTeleop()) return RobotState.TELEOP;
        if (isTest()) return RobotState.TEST;

        throw new IllegalStateException("Illegal robot state");
    }

    private void dumpProfileData(ProfileNode node, DataOutputStream out) throws IOException {
        out.writeUTF(node.getName());
        out.writeLong(node.getElapsedTimeNanoseconds());

        List<ProfileNode> children = node.getChildren();
        out.writeInt(children.size());
        for (ProfileNode child : children) {
            dumpProfileData(child, out);
        }
    }
}

//lemons are good
