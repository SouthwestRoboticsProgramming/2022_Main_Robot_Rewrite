package com.swrobotics.bert;

import edu.wpi.first.wpilibj.TimedRobot;

public final class Robot extends TimedRobot {
    private static final Robot INSTANCE = new Robot();
    public static Robot get() { return INSTANCE; }

    private Robot() {}

    @Override
    public void robotInit() {
        System.out.println("Hello!");

        // All subsystems must be defined before this point
        Scheduler.get().robotInit();
    }

    // Don't add any more code to the init() and periodic() functions below; use a subsystem or command instead

    @Override
    public void robotPeriodic() {
        Scheduler.get().robotPeriodic();
    }

    @Override
    public void disabledInit() {
        Scheduler.get().disabledInit();
    }

    @Override
    public void disabledPeriodic() {
        Scheduler.get().disabledPeriodic();
    }

    @Override
    public void teleopInit() {
        Scheduler.get().teleopInit();
    }

    @Override
    public void teleopPeriodic() {
        Scheduler.get().teleopPeriodic();
    }

    @Override
    public void autonomousInit() {
        Scheduler.get().autonomousInit();
    }

    @Override
    public void autonomousPeriodic() {
        Scheduler.get().autonomousPeriodic();
    }

    @Override
    public void testInit() {
        Scheduler.get().testInit();
    }

    @Override
    public void testPeriodic() {
        Scheduler.get().testPeriodic();
    }
}
