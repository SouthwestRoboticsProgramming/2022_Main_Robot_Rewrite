package com.swrobotics.bert;

import com.swrobotics.bert.commands.MessengerReadCommand;
import com.swrobotics.bert.commands.taskmanager.TaskManagerSetupCommand;
import com.swrobotics.bert.profiler.Profiler;
import com.swrobotics.messenger.client.MessengerClient;
import com.swrobotics.taskmanager.api.TaskManagerAPI;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.RobotBase;

import java.io.IOException;

import static com.swrobotics.bert.constants.CommunicationConstants.*;
import static com.swrobotics.bert.constants.Constants.PERIODIC_PER_SECOND;

public final class Robot extends RobotBase {
    private static final Robot INSTANCE = new Robot();
    public static Robot get() {
        return INSTANCE;
    }

    private boolean running;

    private MessengerClient msg;
    private TaskManagerAPI raspberryPi;
    private TaskManagerAPI jetsonNano;

    private void init() {
        // Connect to Messenger
        while (msg == null) {
            try {
                msg = new MessengerClient(
                        MESSENGER_HOST,
                        MESSENGER_PORT,
                        MESSENGER_NAME
                );
            } catch (IOException e) {
                System.out.println("Messenger connection failed, trying again");
            }
        }
        Scheduler.get().addCommand(new MessengerReadCommand(msg));

        // Connect to TaskManager instances
        raspberryPi = new TaskManagerAPI(msg, RASPBERRY_PI_PREFIX);
        jetsonNano = new TaskManagerAPI(msg, JETSON_NANO_PREFIX);
        Scheduler.get().addCommand(new TaskManagerSetupCommand(raspberryPi, LIDAR_NAME, PATHFINDING_NAME));
        Scheduler.get().addCommand(new TaskManagerSetupCommand(jetsonNano, VISION_NAME));

        // Schedule subsystems here
        // Scheduler.get().addSubsystem(new TestSubsystem());
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
}
