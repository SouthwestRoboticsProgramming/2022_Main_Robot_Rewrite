package com.swrobotics.bert.commands.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.commands.CommandSequence;
import com.swrobotics.bert.commands.auto.six.SixBallOne;

public final class AutonomousSequence extends CommandSequence {
    public AutonomousSequence(RobotContainer robot) {
        append(new TwoBallAuto(robot));
    }
}
