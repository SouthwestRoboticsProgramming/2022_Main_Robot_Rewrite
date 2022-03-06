package com.swrobotics.bert;

import edu.wpi.first.wpilibj.RobotBase;

public final class Main {
    public static void main(String[] args) {
        RobotBase.startRobot(Robot::get);
    }

    private Main() {
        throw new AssertionError();
    }
}
