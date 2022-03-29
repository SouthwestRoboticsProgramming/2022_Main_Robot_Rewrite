package com.swrobotics.bert.subsystems.camera;

import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.Subsystem;

public class CameraController implements Subsystem{
    private final Limelight limelight;
    private final Localization loc;
    private final Input input;

    public CameraController(Limelight limelight, Localization loc, Input input) {
        this.limelight = limelight;
        this.loc = loc;
        this.input = input;
    }

    @Override
    public void robotPeriodic() {
        if (loc.isLookingAtTarget() || input.getAimOverride()) {
            limelight.setLights(true);
        }
    }



}
