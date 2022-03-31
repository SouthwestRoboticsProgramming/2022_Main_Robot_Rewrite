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

    private LimelightOutputFilter xFilter;
    private LimelightOutputFilter yFilter;

    public Limelight() {
        NetworkTable table = NetworkTableInstance.getDefault().getTable("limelight");
        this.xAngle = table.getEntry("tx");
        this.yAngle = table.getEntry("ty");
        this.targetArea = table.getEntry("ta");

        this.lightsOn = table.getEntry("ledMode");

        setLights(LIMELIGHT_LIGHTS.get());
        LIMELIGHT_LIGHTS.onChange(() -> setLights(LIMELIGHT_LIGHTS.get()));
    
        int span = 5;
        xFilter = new LimelightOutputFilter(span);
        yFilter = new LimelightOutputFilter(span);
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

    public boolean isAccurate() {
        if (x == 0.0 || y == 0.0) {return false;} // If it can't find a target
        if (getDistance() < 0 || getDistance() > 20) {return false;} // If the distance is negative or massive
        return true;
    }

    public void setLights(boolean on) {
        int value = 1;
        if (on) { value = 3;}
        lightsOn.setNumber(value);
        LIMELIGHT_LIGHTS.set(on);
    }



    @Override
    public void robotPeriodic() {
        x = xFilter.filter(xAngle.getDouble(0.0));
        y = yFilter.filter(yAngle.getDouble(0.0));
        area = targetArea.getDouble(0.0);
    }
}