package com.swrobotics.lidar.library;

public final class NoSuchLidarPortException extends RuntimeException {
    private static final long serialVersionUID = 143921412354326L;

    public NoSuchLidarPortException(String message) {
        super(message);
    }
}
