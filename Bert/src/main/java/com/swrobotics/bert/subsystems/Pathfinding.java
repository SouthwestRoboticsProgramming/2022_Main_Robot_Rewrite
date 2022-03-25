package com.swrobotics.bert.subsystems;

import com.swrobotics.bert.commands.auto.path.Point;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;
import com.swrobotics.messenger.client.MessengerClient;

import java.util.ArrayList;
import java.util.List;

public class Pathfinding implements Subsystem {
    private final SwerveDriveController drive;
    private final Localization loc;
    private final Input input;
    private final List<Point> path;

    public Pathfinding(SwerveDriveController drive, Localization loc, Input input, MessengerClient msg) {
        this.drive = drive;
        this.loc = loc;
        this.input = input;
        path = new ArrayList<>();

        msg.makeHandler()
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
    }

    @Override
    public void robotPeriodic() {
        if (!input.getFollowPath() || path.size() < 2)
            return;

        double locX = loc.getFieldX();
        double locY = loc.getFieldY();

        Point target = path.get(1);

        double deltaX = target.getX() - locX;
        double deltaY = target.getY() - locY;
        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        deltaX /= distance;
        deltaY /= distance;

        double speed = 1;

        deltaX *= speed;
        deltaY *= speed;

        drive.drive(deltaX, deltaY);
        System.out.println("Deltas: " + deltaX + ", " + deltaY);
    }
}
