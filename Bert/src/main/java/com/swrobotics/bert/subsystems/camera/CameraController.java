package com.swrobotics.bert.subsystems.camera;

import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.PDP;
import com.swrobotics.bert.subsystems.Subsystem;

public class CameraController implements Subsystem{
    private final Limelight limelight;
    private final PDP pdp;
    private final Localization loc;
    private final Input input;

    public CameraController(Limelight limelight, Localization loc, Input input, PDP pdp) {
        this.limelight = limelight;
        this.loc = loc;
        this.input = input;
        this.pdp = pdp;
    }

    @Override
    public void robotPeriodic() {
        if (loc.isLookingAtTarget() || input.getAimOverride()) {
            limelight.setLights(true);
            pdp.setLights(true);
        } else {
            limelight.setLights(false);
            pdp.setLights(false);
        }
    }

    @Override
    public void disabledInit() {
        limelight.setLights(false);
        pdp.setLights(false);
    }



}
