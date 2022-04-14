
package com.swrobotics.bert.util;

import com.ctre.phoenix.ErrorCode;
import edu.wpi.first.wpilibj.DriverStation;

public final class CtreUtils {
    /* Tests to see if a configuration worked */
    private CtreUtils() {
    }

    public static void checkCtreError(ErrorCode errorCode, String message) {
        if (errorCode != ErrorCode.OK) {
            DriverStation.reportError(String.format("%s: %s", message, errorCode.toString()), false);
            // Credit to Swerve Drive Specialties
        }
    }
}