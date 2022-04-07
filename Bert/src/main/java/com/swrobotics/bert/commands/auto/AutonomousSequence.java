package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;

import static com.swrobotics.bert.constants.AutonomousConstants.*;

public final class AutonomousSequence extends CommandSequence {
    public AutonomousSequence(RobotContainer robot) {
        switch (AUTO_MODE.get()) {
            case DO_NOTHING:
                break;
            case BACK_UP:
                append(new BackupAutonomousSequence(robot));
                break;
            case TWO_BALL:
                append(new TwoBallAuto(robot));
                break;
        }
    }
}
