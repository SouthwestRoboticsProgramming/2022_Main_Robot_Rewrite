package com.swrobotics.taskmanager.server;

import org.zeroturnaround.exec.stream.LogOutputStream;

public final class OutputLogger extends LogOutputStream {
    private final Task task;
    private final OutputType type;

    public OutputLogger(Task task, OutputType type) {
        this.task = task;
        this.type = type;
    }

    @Override
    protected void processLine(String line) {
        TaskManagerServer.get().getAPI().sendTaskOutput(task.getName(), type, line);
    }
}
