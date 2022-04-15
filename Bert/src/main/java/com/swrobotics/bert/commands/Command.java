package com.swrobotics.bert.commands;

public interface Command {
    default void init() {};

    // Return whether the command is done
    boolean run();

    default void end() {};

    default int getInterval() {
        return 1;
    }
}
