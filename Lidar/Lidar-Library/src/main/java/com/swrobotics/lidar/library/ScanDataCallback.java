package com.swrobotics.lidar.library;

@FunctionalInterface
public interface ScanDataCallback {
    void call(ScanEntry entry);
}
