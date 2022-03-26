package com.swrobotics.bert.subsystems.camera;

import com.swrobotics.bert.subsystems.Subsystem;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

import static com.swrobotics.bert.constants.CameraConstants.*;

public final class Limelight implements Subsystem {

    private final NetworkTableEntry xAngle;
    private final NetworkTableEntry yAngle;
    private final NetworkTableEntry targetArea;

    private final NetworkTableEntry lightsOn;

    private double x;
    private double y;
    private double area;

    public Limelight() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        this.xAngle = table.getEntry("tx");
        this.yAngle = table.getEntry("ty");
        this.targetArea = table.getEntry("ta");

        this.lightsOn = table.getEntry("ledMode");
    }

    public double getXangle() {
        return x;
    }
   
    public double getRawYangle() {
        return y;
    }

    public double getRealYangle() {
        return y + LIMELIGHT_MOUNT_ANGLE;
    }
    
    public double getArea() {
        return area;
    }


    public double getDistance() {

        double heightDiff = TARGET_HEIGHT - LIMELIGHT_MOUNT_HEIGHT;
        double angle = LIMELIGHT_MOUNT_ANGLE + y;

        double distance = heightDiff / Math.tan(Math.toRadians(angle));
        return distance;
    }

    public void setLights(boolean on) {
        int value = 1;
        if (on) { value = 3;}
        lightsOn.setNumber(value);
    }



    @Override
    public void robotPeriodic() {
        x = xAngle.getDouble(0.0);
        y = yAngle.getDouble(0.0);
        area = targetArea.getDouble(0.0);
    }
}