package com.swrobotics.bert.subsystems.shooter;

import com.swrobotics.bert.subsystems.Subsystem;
import edu.wpi.first.wpilibj.Encoder;

import static com.swrobotics.bert.constants.ShooterConstants.*;

public final class NewHood implements Subsystem {
    private final Encoder encoder;

    public NewHood() {
        encoder = new Encoder(HOOD_ENCODER_ID_1, HOOD_ENCODER_ID_2);
    }

    @Override
    public void robotPeriodic() {
//        System.out.println(encoder.getDistance());
    }
}
