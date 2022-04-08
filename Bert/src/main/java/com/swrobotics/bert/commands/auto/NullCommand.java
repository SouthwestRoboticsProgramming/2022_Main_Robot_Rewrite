package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.commands.Command;

public class NullCommand implements Command {
    @Override
    public void init() {
    }

    @Override
    public boolean run() {
        return true;
    }

    @Override
    public void end() {
    }
}
