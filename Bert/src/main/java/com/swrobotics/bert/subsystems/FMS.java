package com.swrobotics.bert.subsystems;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public final class FMS implements Subsystem {
    private final NetworkTableEntry isRed;

    public FMS() {
        NetworkTable fms = NetworkTableInstance.getDefault().getTable("FMSInfo");
        isRed = fms.getEntry("IsRedAlliance");
    }

    public boolean isRedAlliance() {
        return isRed.getBoolean(false);
    }



}
