package com.swrobotics.bert.subsystems;

import com.swrobotics.bert.commands.auto.path.Point;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.drive.SwerveDriveController;
import com.swrobotics.bert.util.Utils;
import com.swrobotics.messenger.client.MessengerClient;
import edu.wpi.first.math.controller.PIDController;

import java.util.ArrayList;
import java.util.List;

import static com.swrobotics.bert.constants.AutonomousConstants.*;

public class Pathfinding implements Subsystem {
    private final SwerveDriveController drive;
    private final Localization loc;
    private final Input input;
    private final List<Point> path;
    private final PIDController pid;

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

        Point finalTarget = path.get(path.size() - 1);
        double finalX = finalTarget.getX() - locX;
        double finalY = finalTarget.getY() - locY;
        double finalDistance = Math.sqrt(finalX * finalX + finalY * finalY);

        double speed = Utils.clamp(-pid.calculate(finalDistance, 0), 0, 1);

        deltaX *= speed;
        deltaY *= speed;

        drive.drive(deltaX, deltaY);
        System.out.println("Deltas: " + deltaX + ", " + deltaY);
    }
}
