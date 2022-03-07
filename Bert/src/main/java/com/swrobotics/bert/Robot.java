package com.swrobotics.bert;

import com.swrobotics.bert.profiler.Profiler;
import com.swrobotics.bert.subsystems.TestSubsystem;
import edu.wpi.first.hal.HAL;
import edu.wpi.first.wpilibj.RobotBase;

public final class Robot extends RobotBase {
    private static final Robot INSTANCE = new Robot();
    public static Robot get() {
        return INSTANCE;
    }

    private boolean running;

    private Robot() {}

    private void initSubsystems() {
        // Schedule subsystems here
        Scheduler.get().addSubsystem(new TestSubsystem());
    }

    @Override
    public void startCompetition() {
        running = true;

        System.out.println("************** Robot program starting **************");
        initSubsystems();
        Scheduler.get().robotInit();
        System.out.println("********** Robot program startup complete **********");

        HAL.observeUserProgramStarting();

        RobotState lastState = RobotState.DISABLED;
        while (running && !Thread.currentThread().isInterrupted()) {
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
