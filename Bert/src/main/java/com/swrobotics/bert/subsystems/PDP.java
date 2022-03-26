package com.swrobotics.bert.subsystems;

import edu.wpi.first.wpilibj.PowerDistribution;

import static com.swrobotics.bert.constants.Constants.*;
import static com.swrobotics.bert.constants.Settings.*;

public final class PDP implements Subsystem {
    private final PowerDistribution pdp = new PowerDistribution(PDP_ID, PowerDistribution.ModuleType.kRev);

    @Override
    public void robotInit() {
        setLights(RING_LIGHTS.get());
        RING_LIGHTS.onChange(() -> setLights(RING_LIGHTS.get()));
    }

    public void setLights(boolean lights) {
        pdp.setSwitchableChannel(lights);
    }
}
