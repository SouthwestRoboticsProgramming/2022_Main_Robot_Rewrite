package com.swrobotics.bert.subsystems;

public interface Subsystem {
    default void robotInit() {}
    default void robotPeriodic() {}
    default void disabledInit() {}
    default void disabledPeriodic() {}
    default void teleopInit() {}
    default void teleopPeriodic() {}
    default void autonomousInit() {}
    default void autonomousPeriodic() {}
    default void testInit() {}
    default void testPeriodic() {}
}
