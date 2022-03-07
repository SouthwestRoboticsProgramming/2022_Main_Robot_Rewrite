package com.swrobotics.taskmanager.server;

public final class TaskManagerServerMain {
    public static void main(String[] args) {
        TaskManagerServer.get().getAPI().run();
    }

    private TaskManagerServerMain() {
        throw new AssertionError();
    }
}
