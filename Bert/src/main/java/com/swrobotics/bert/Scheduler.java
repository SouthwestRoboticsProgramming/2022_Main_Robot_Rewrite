package com.swrobotics.bert;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.profiler.Profiler;
import com.swrobotics.bert.subsystems.Subsystem;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

public final class Scheduler {
    private static final Scheduler INSTANCE = new Scheduler();
    public static Scheduler get() {
        return INSTANCE;
    }

    private final Queue<Subsystem> subsystems;
    private final Queue<CommandTimer> commands;

    private final Set<Subsystem> subsystemsToRemove;

    private Scheduler() {
        subsystems = new ConcurrentLinkedQueue<>();
        commands = new ConcurrentLinkedQueue<>();

        subsystemsToRemove = new HashSet<>();
    }

    public void addSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    public boolean hasSubsystem(Subsystem subsystem) {
        return subsystems.contains(subsystem);
    }

    public void removeSubsystem(Subsystem subsystem) {
        subsystemsToRemove.add(subsystem);
    }

    public void addCommand(Command command) {
        commands.add(new CommandTimer(command));
        try {
            command.init();
        } catch (Throwable t) {
            System.err.println("Exception initializing command " + command.getClass().getName());
            t.printStackTrace();
        }
    }

    public void cancelCommand(Command command) {
        CommandTimer toRemove = null;
        for (CommandTimer timer : commands) {
            if (timer.command.equals(command)) {
                toRemove = timer;
            }
        }

        if (toRemove != null) {
            command.end();
            commands.remove(toRemove);
        }
    }

    public boolean isCommandRunning(Command command) {
        for (CommandTimer timer : commands) {
            if (timer.command.equals(command)) {
                return true;
            }
        }

        return false;
    }

    private static class CommandTimer {
        private final Command command;
        private int timer;

        public CommandTimer(Command command) {
            this.command = command;
            timer = command.getInterval();
        }

        public boolean update() {
            timer--;
            if (timer <= 0) {
                timer = command.getInterval();

                try {
                    return command.run();
                } catch (Throwable t) {
                    System.err.println("Exception ticking command " + command.getClass().getName());
                    t.printStackTrace();
                }
            }

            return false;
        }
    }

    private void updateCommands() {
        subsystems.removeAll(subsystemsToRemove);
        subsystemsToRemove.clear();

        for (Iterator<CommandTimer> iterator = commands.iterator(); iterator.hasNext();) {
            CommandTimer timer = iterator.next();
            Profiler.get().push(timer.command.getClass().getSimpleName());

            if (timer.update()) {
                iterator.remove();
                timer.command.end();
            }

            Profiler.get().pop();
        }
    }

    public void robotInit() {
        for (Subsystem system : subsystems) {
            try {
                system.robotInit();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
        }
    }

    public void robotPeriodic() {
        for (Subsystem system : subsystems) {
            Profiler.get().push(system.getClass().getSimpleName());
            try {
                system.robotPeriodic();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
            Profiler.get().pop();
        }

        updateCommands();
    }

    public void disabledInit() {
        for (Subsystem system : subsystems) {
            try {
                system.disabledInit();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
        }
    }

    public void disabledPeriodic() {
        for (Subsystem system : subsystems) {
            Profiler.get().push(system.getClass().getSimpleName());
            try {
                system.disabledPeriodic();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
            Profiler.get().pop();
        }
    }

    public void teleopInit() {
        for (Subsystem system : subsystems) {
            try {
                system.teleopInit();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
        }
    }

    public void teleopPeriodic() {
        for (Subsystem system : subsystems) {
            Profiler.get().push(system.getClass().getSimpleName());
            try {
                system.teleopPeriodic();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
            Profiler.get().pop();
        }
    }

    public void autonomousInit() {
        for (Subsystem system : subsystems) {
            try {
                system.autonomousInit();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
        }
    }

    public void autonomousPeriodic() {
        for (Subsystem system : subsystems) {
            Profiler.get().push(system.getClass().getSimpleName());
            try {
                system.autonomousPeriodic();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
            Profiler.get().pop();
        }
    }

    public void testInit() {
        for (Subsystem system : subsystems) {
            try {
                system.testInit();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
        }
    }

    public void testPeriodic() {
        for (Subsystem system : subsystems) {
            Profiler.get().push(system.getClass().getSimpleName());
            try {
                system.testPeriodic();
            } catch (Throwable t) {
                System.err.println("Exception in system " + system.getClass().getName());
                t.printStackTrace();
            }
            Profiler.get().pop();
        }
    }
}
