package com.swrobotics.bert.constants.ball;

public final class BallLocationConstants {

    /*
     *
     * +---------------------------------------+
     * |    |           \ B6 R3             R7\|
     * |----+         R4 \     R2              |
     * |             B1   \     B5             |
     * |                   .                   |
     * |             R5     \   R1             |
     * |              B2     \ B4         +----|
     * |\B7             B3 R6 \           |    |
     * +---------------------------------------+ 
     *              [Scoring table]
     */

    public static final BallLocation BLUE_1 = new BallLocation(0, 0);
    public static final BallLocation BLUE_2 = new BallLocation(0, 0);
    public static final BallLocation BLUE_3 = new BallLocation(0, 0);
    public static final BallLocation BLUE_4 = new BallLocation(0, 0);
    public static final BallLocation BLUE_5 = new BallLocation(0, 0);
    public static final BallLocation BLUE_6 = new BallLocation(0, 0);
    public static final BallLocation BLUE_7 = new BallLocation(0, 0);
    public static final BallLocation RED_1 = new BallLocation(0, 0);
    public static final BallLocation RED_2 = new BallLocation(0, 0);
    public static final BallLocation RED_3 = new BallLocation(0, 0);
    public static final BallLocation RED_4 = new BallLocation(0, 0);
    public static final BallLocation RED_5 = new BallLocation(0, 0);
    public static final BallLocation RED_6 = new BallLocation(0, 0);
    public static final BallLocation RED_7 = new BallLocation(0, 0);


    private BallLocationConstants() {
        throw new AssertionError();
    }
}
