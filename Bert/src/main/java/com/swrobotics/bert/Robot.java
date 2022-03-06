package com.swrobotics.bert;

import edu.wpi.first.wpilibj.TimedRobot;

public final class Robot extends TimedRobot {
    private static final Robot INSTANCE = new Robot();
    public static Robot get() { return INSTANCE; }

    private Robot() {}

    @Override
    public void robotInit() {
        System.out.println("Hello!");
    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void disabledInit() {
        System.out.println("In Disabled");
    }

    @Override
    public void disabledPeriodic() {

    }

    @Override
    public void teleopInit() {
        System.out.println("In TeleOp");
    }

    @Override
    public void teleopPeriodic() {

    }

    @Override
    public void autonomousInit() {
        System.out.println("In Auto");
    }

    @Override
    public void autonomousPeriodic() {

    }

    @Override
    public void testInit() {
        System.out.println("In Test");
    }

    @Override
    public void testPeriodic() {

    }
}
