package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.auto.defense.TwoPlusThreeAuto;
import com.swrobotics.bert.commands.auto.defense.TwoPlusTwoAuto;

import static com.swrobotics.bert.constants.AutonomousConstants.*;

public final class AutonomousSequence extends CommandSequence {
    public AutonomousSequence(RobotContainer robot) {
        switch (AUTO_MODE.get()) {
            case DO_NOTHING:
                append(new NullCommand());
                break;
            case BACK_UP:
                append(new BackupAutonomousSequence(robot));
                break;
            case TWO_BALL:
                append(new TwoBallAuto(robot));
                break;
            case THREE_BALL:
                append(new ThreeBallAuto(robot));
                break;
            case TWO_PLUS_TWO:
                append(new TwoPlusTwoAuto(robot));
                break;
            case TWO_PLUS_THREE:
                append(new TwoPlusThreeAuto(robot));
                break;
            case TAXI:
                append(new TaxiSequence(robot));
                break;
            case ONE_BALL:
                append(new OneBallAuto(robot));
                break;
        }
    }
}
