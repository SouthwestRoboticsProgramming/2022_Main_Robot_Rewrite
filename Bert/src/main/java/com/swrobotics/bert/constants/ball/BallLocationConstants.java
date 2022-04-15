package com.swrobotics.bert.constants.ball;

public final class BallLocationConstants {

    /*
     *
     * +---------------------------------------+
     * |    |           \ B4 R1             R7\|
     * |----+         R6 \     R2              |
     * |             B3   \     B5             |
     * |                   .                   |
     * |             R5     \   R3             |
     * |              B2     \ B6         +----|
     * |\B7             B1 R4 \           |    |
     * +---------------------------------------+ 
     *              [Scoring table]
     */

    public static final BallLocation BLUE_1 = new BallLocation(3.881, -0.569);
    public static final BallLocation BLUE_2 = new BallLocation(2.281, -3.106);
    public static final BallLocation BLUE_3 = new BallLocation(-2.094, -3.231);
    public static final BallLocation BLUE_4 = new BallLocation(-3.819, -0.718);
    public static final BallLocation BLUE_5 = new BallLocation(-0.844, 3.931);
    public static final BallLocation BLUE_6 = new BallLocation(3.219, 2.369);
    public static final BallLocation BLUE_7 = new BallLocation(3.044, -7.169);

    public static final BallLocation RED_1 = new BallLocation(-3.869, 0.756);
    public static final BallLocation RED_2 = new BallLocation(-2.256, 3.306);
    public static final BallLocation RED_3 = new BallLocation(2.094, 3.419);
    public static final BallLocation RED_4 = new BallLocation(3.831, 0.956);
    public static final BallLocation RED_5 = new BallLocation(0.869, -3.744);
    public static final BallLocation RED_6 = new BallLocation(-3.206, -2.184);
    public static final BallLocation RED_7 = new BallLocation(-3.019, 7.344);


    private BallLocationConstants() {
        throw new AssertionError();
    }
}
