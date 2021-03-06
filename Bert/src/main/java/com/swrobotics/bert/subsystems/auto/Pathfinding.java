package com.swrobotics.bert.subsystems.auto;

import com.swrobotics.bert.RobotContainer;
import com.swrobotics.bert.subsystems.Localization;
import com.swrobotics.bert.subsystems.Subsystem;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;
import com.swrobotics.bert.util.Utils;
import com.swrobotics.messenger.client.MessengerClient;
import edu.wpi.first.math.controller.PIDController;

import java.util.ArrayList;
import java.util.List;

import static com.swrobotics.bert.constants.AutonomousConstants.*;

public final class Pathfinding implements Subsystem {
    private final RobotContainer robot;
    private final List<Point> path;
    private final PIDController pid;

    public Pathfinding(RobotContainer robot) {
        this.robot = robot;
        path = new ArrayList<>();

        robot.msg.makeHandler()
                .listen("Pathfinder:Path")
                .setHandler((type, in) -> {
                    boolean pathValid = in.readBoolean();
                    path.clear();

                    if (pathValid) {
                        int count = in.readInt();
                        for (int i = 0; i < count; i++) {
                            double x = in.readDouble();
                            double y = in.readDouble();

                            path.add(new Point(-y, x)); // TODO: Move conversion to pathfinder task
                        }
                    }
                });

        pid = new PIDController(
                PATH_KP.get(),
                PATH_KI.get(),
                PATH_KD.get()
        );

        PATH_KP.onChange(this::updatePID);
        PATH_KI.onChange(this::updatePID);
        PATH_KD.onChange(this::updatePID);
    }

    private void updatePID() {
        pid.setPID(
                PATH_KP.get(),
                PATH_KI.get(),
                PATH_KD.get()
        );
    }

    public boolean isAtPathTarget() {
        if (path.size() < 1) return false; // No path

        Localization loc = robot.localization;

        Point target = path.get(path.size() - 1);
        double deltaX = target.getX() - loc.getFieldX();
        double deltaY = target.getY() - loc.getFieldY();
        double distanceSq = deltaX * deltaX + deltaY * deltaY;

        double tolerance = TARGET_THRESHOLD_DIST.get();
        return distanceSq <= tolerance * tolerance;
    }

    @Override
    public void teleopPeriodic() {
        // if (robot.input.getFollowPath()) {
        //     autonomousPeriodic();
        // }
    }

    @Override
    public void autonomousPeriodic() {
        if (path.size() < 2) return;

        Localization loc = robot.localization;
        SwerveDriveController drive = robot.driveController;

        double locX = loc.getFieldX();
        double locY = loc.getFieldY();

        Point target = path.get(1);

        double deltaX = target.getX() - locX;
        double deltaY = target.getY() - locY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX /= distance;
        deltaY /= distance;

        Point finalTarget = path.get(path.size() - 1);
        double finalX = finalTarget.getX() - locX;
        double finalY = finalTarget.getY() - locY;
        double finalDistance = Math.sqrt(finalX * finalX + finalY * finalY);

        double speed = Utils.clamp(-pid.calculate(finalDistance, 0), 0, 1);

        deltaX *= speed;
        deltaY *= speed;

        drive.drive(deltaX, deltaY);
        // System.out.println("Deltas: " + deltaX + ", " + deltaY);
    }
}
