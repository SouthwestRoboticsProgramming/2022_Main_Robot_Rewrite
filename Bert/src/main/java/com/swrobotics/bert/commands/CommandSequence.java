package com.swrobotics.bert.commands;

import java.util.ArrayList;
import java.util.List;

public abstract class CommandSequence implements Command {
    private final List<Command> cmds;
    private int index;

    public CommandSequence() {
        cmds = new ArrayList<>();
    }

    protected void append(Command cmd) {
        cmds.add(cmd);
    }

    @Override
    public void init() {
        if (cmds.size() == 0) {
            throw new IllegalStateException("Empty CommandSequence");
        }

        index = 0;
        cmds.get(index).init();
    }

    @Override
    public boolean run() {
        if (cmds.get(index).run()) {
            cmds.get(index).end();
            index++;

            if (running()) {
                cmds.get(index).init();
            }
        }

        return !running();
    }

    @Override
    public void end() {
        if (running()) {
            cmds.get(index).end();
        }
    }

    private boolean running() {
        return index < cmds.size();
    }
}
