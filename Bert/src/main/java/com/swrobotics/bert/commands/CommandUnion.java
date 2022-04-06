package com.swrobotics.bert.commands;

import java.util.Arrays;

public final class CommandUnion implements Command {
    private final Command[] cmds;
    private final boolean[] ended;

    public CommandUnion(Command... cmds) {
        this.cmds = cmds;
        ended = new boolean[cmds.length];
        Arrays.fill(ended, false);
    }

    @Override
    public void init() {
        System.out.println("Union starting");
        for (Command cmd : cmds) {
            cmd.init();
        }
    }

    @Override
    public boolean run() {
        boolean done = true;

        for (int i = 0; i < cmds.length; i++) {
            Command cmd = cmds[i];
            if (ended[i]) continue;
            
            boolean cmdDone = cmd.run();

            if (cmdDone) {
                System.out.println("Union member ended: " + cmd.getClass().getName());
                cmd.end();
                ended[i] = true;
            } else {
                done = false;
            }
        }

        return done;
    }

    @Override
    public void end() {
        System.out.println("Union ended");
        for (int i = 0; i < cmds.length; i++) {
            if (!ended[i]) {
                cmds[i].end();
            }
        }
    }
}
