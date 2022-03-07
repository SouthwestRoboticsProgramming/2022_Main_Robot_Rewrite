package com.swrobotics.bert.subsystems;

public class TestSubsystem implements Subsystem {
    @Override
    public void robotInit() {
        System.out.println("Robot init");
    }

    @Override
    public void robotPeriodic() {
        System.out.println("Robot periodic");
    }

    @Override
    public void disabledInit() {
        System.out.println("Disabled init");
    }

    @Override
    public void disabledPeriodic() {
        System.out.println("Disabled periodic");
    }

    @Override
    public void teleopInit() {
        System.out.println("Teleop init");
    }

    @Override
    public void teleopPeriodic() {
        System.out.println("Teleop periodic");
    }

    @Override
    public void autonomousInit() {
        System.out.println("Autonomous init");
    }

    @Override
    public void autonomousPeriodic() {
        System.out.println("Autonomous periodic");
    }

    @Override
    public void testInit() {
        System.out.println("Test init");
    }

    @Override
    public void testPeriodic() {
        System.out.println("Test periodic");
    }
}
