package com.swrobotics.bert.subsystems.camera;

import com.swrobotics.bert.subsystems.Subsystem;
import static com.swrobotics.bert.constants.CameraConstants.*;

// TODO Mason: implement this
// No.
public class Cameras implements Subsystem {
    // Whether the hub measurements are accurate (i.e. hub is visible and not touching edge of frame)
    public boolean hubMeasurementsValid() {
        return false;
    }

    // Meters
    public double getHubDistance() {
        return -1;
    }

    // Degrees, counterclockwise positive, forward is zero
    public double getHubAngle() {




        return 0;
    }
}
