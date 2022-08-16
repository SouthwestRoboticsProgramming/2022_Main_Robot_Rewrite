package com.swrobotics.bert.good_drive.drive;

import com.kauailabs.navx.frc.AHRS;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Subsystem;
import com.team2129.lib.profile.Profiler;
import com.team2129.lib.schedule.Scheduler;
import com.team2129.lib.wpilib.AbstractRobot;
import com.team2129.lib.wpilib.RobotState;

@Deprecated
public class BadCode implements Subsystem {
    private final Drive drive;
    
    public BadCode(Input input, AHRS gyro) {
        drive = new Drive(input, gyro);

        Scheduler.get().addSubsystem(drive);
    }

    public void robotInit() {}
    public void robotPeriodic() {
        Profiler.beginMeasurements("i dont care about these measurements");
        Scheduler.get().periodicState(AbstractRobot.get().getCurrentState());
        Profiler.endMeasurements();
    }
    public void disabledInit() {Scheduler.get().initState(RobotState.DISABLED);}
    public void disabledPeriodic() {}
    public void teleopInit() {Scheduler.get().initState(RobotState.TELEOP);}
    public  void teleopPeriodic() {}
    public  void autonomousInit() {Scheduler.get().initState(RobotState.AUTONOMOUS);}
    public  void autonomousPeriodic() {}
    public  void testInit() {Scheduler.get().initState(RobotState.TEST);}
    public   void testPeriodic() {}
}
