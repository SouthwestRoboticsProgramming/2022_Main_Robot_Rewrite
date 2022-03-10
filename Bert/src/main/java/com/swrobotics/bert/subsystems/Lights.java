package com.swrobotics.bert.subsystems;

import edu.wpi.first.wpilibj.motorcontrol.Spark;

import static com.swrobotics.bert.constants.LightConstants.*;

public class Lights implements Subsystem {
    private final Spark lights;

    public Lights() {
        lights = new Spark(CONTROLLER_ID);
    }

    @Override
    public void robotPeriodic() {
        lights.set(PURPLE_SOLID);
    }
}
