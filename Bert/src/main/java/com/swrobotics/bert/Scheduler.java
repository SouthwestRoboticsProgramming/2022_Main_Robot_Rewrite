package com.swrobotics.bert;

import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.subsystems.Subsystem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Scheduler {
    private static final Scheduler INSTANCE = new Scheduler();
    public static Scheduler get() {
        return INSTANCE;
    }

    private final List<Subsystem> subsystems;
    private final List<CommandTimer> commands;

    private Scheduler() {
        subsystems = new ArrayList<>();
        commands = new ArrayList<>();
    }

    public void addSubsystem(Subsystem subsystem) {
        subsystems.add(subsystem);
    }

    public void addCommand(Command command) {
        commands.add(new CommandTimer(command));
        command.init();
    }

    public void cancelCommand(Command command) {
        CommandTimer toRemove = null;
        for (CommandTimer timer : commands) {
            if (timer.command.equals(command)) {
                toRemove = timer;
            }
        }

        if (toRemove != null) {
            commands.remove(toRemove);
        }
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

                return command.run();
            }

            return false;
        }
    }

    private void updateCommands() {
        for (Iterator<CommandTimer> iterator = commands.iterator(); iterator.hasNext();) {
            CommandTimer timer = iterator.next();

            if (timer.update()) {
                iterator.remove();
                timer.command.end();
            }
        }
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

        updateCommands();
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

        updateCommands();
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

        updateCommands();
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

        updateCommands();
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

        updateCommands();
    }
}
