package com.swrobotics.bert;

import com.swrobotics.bert.subsystems.Subsystem;

import java.util.ArrayList;
import java.util.List;

public final class Scheduler {
    private static final Scheduler INSTANCE = new Scheduler();
    public static Scheduler get() {
        return INSTANCE;
    }

    private final List<Subsystem> subsystems;

    private Scheduler() {
        subsystems = new ArrayList<>();
    }

    public void addSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    public void robotInit() {
        for (Subsystem system : subsystems) {
            system.robotInit();
        }
    }

    public void robotPeriodic() {
        for (Subsystem system : subsystems) {
            system.robotPeriodic();
        }
    }

    public void disabledInit() {
        for (Subsystem system : subsystems) {
            system.disabledInit();
        }
    }

    public void disabledPeriodic() {
        for (Subsystem system : subsystems) {
            system.disabledPeriodic();
        }
    }

    public void teleopInit() {
        for (Subsystem system : subsystems) {
            system.teleopInit();
        }
    }

    public void teleopPeriodic() {
        for (Subsystem system : subsystems) {
            system.teleopPeriodic();
        }
    }

    public void autonomousInit() {
        for (Subsystem system : subsystems) {
            system.autonomousInit();
        }
    }

    public void autonomousPeriodic() {
        for (Subsystem system : subsystems) {
            system.autonomousPeriodic();
        }
    }

    public void testInit() {
        for (Subsystem system : subsystems) {
            system.testInit();
        }
    }

    public void testPeriodic() {
        for (Subsystem system : subsystems) {
            system.testPeriodic();
        }
    }
}
