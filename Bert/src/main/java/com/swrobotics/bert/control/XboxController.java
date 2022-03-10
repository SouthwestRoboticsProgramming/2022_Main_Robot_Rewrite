package com.swrobotics.bert.control;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.GenericHID.RumbleType;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public final class XboxController {
    public static class Button {
        private final JoystickButton button;

        private boolean pressed;
        private boolean last;

        public Button(Joystick stick, int buttonID) {
            button = new JoystickButton(stick, buttonID);
        }

        public boolean isPressed() {
            return pressed;
        }

        public boolean leadingEdge() {
            return pressed && !last;
        }

        public boolean fallingEdge() {
            return !pressed && last;
        }

        protected void update() {
            last = pressed;
            pressed = button.get();
        }
    }

    public static class DpadButton {
        private final Joystick stick;
        private final int a, b, c;

        private boolean pressed;
        private boolean last;

        public DpadButton(Joystick stick, int a, int b, int c) {
            this.stick = stick;
            this.a = a;
            this.b = b;
            this.c = c;
        }

        public boolean isPressed() {
            return pressed;
        }

        public boolean leadingEdge() {
            return pressed && !last;
        }

        public boolean fallingEdge() {
            return !pressed && last;
        }

        protected void update() {
            last = pressed;

            int pov = stick.getPOV();
            pressed = pov == a || pov == b || pov == c;
        }
    }

    public static class Axis {
        private final Joystick stick;
        private final int axisID;
        private final boolean inverted;

        private double value;

        public Axis(Joystick stick, int axisID, boolean inverted) {
            this.stick = stick;
            this.axisID = axisID;
            this.inverted = inverted;
        }

        public double get() {
            return value;
        }

        void update() {
            if (inverted) {
                value = -stick.getRawAxis(axisID);
            } else {
                value = stick.getRawAxis(axisID);
            }
        }
    }

    private static final int ID_A = 1;
    private static final int ID_B = 2;
    private static final int ID_X = 3;
    private static final int ID_Y = 4;
    private static final int ID_LEFT_SHOULDER = 5;
    private static final int ID_RIGHT_SHOULDER = 6;
    private static final int ID_SELECT = 7;
    private static final int ID_START = 8;
    private static final int ID_LEFT_STICK = 9;
    private static final int ID_RIGHT_STICK = 10;

    private static final int AXIS_LEFT_STICK_X = 0;
    private static final int AXIS_LEFT_STICK_Y = 1;
    private static final int AXIS_LEFT_TRIGGER = 2;
    private static final int AXIS_RIGHT_TRIGGER = 3;
    private static final int AXIS_RIGHT_STICK_X = 4;
    private static final int AXIS_RIGHT_STICK_Y = 5;

    public final Button a, b, x, y;
    public final Button leftShoulder, rightShoulder;
    public final Button select, start;    
    public final Button leftStickIn, rightStickIn;

    public final DpadButton dpadUp, dpadDown, dpadLeft, dpadRight;

    public final Axis leftStickX, leftStickY;
    public final Axis rightStickX, rightStickY;
    public final Axis leftTrigger, rightTrigger;

    public XboxController(int id) {
        Joystick stick = new Joystick(id);

        a             = new Button(stick, ID_A);
        b             = new Button(stick, ID_B);
        x             = new Button(stick, ID_X);
        y             = new Button(stick, ID_Y);
        leftShoulder  = new Button(stick, ID_LEFT_SHOULDER);
        rightShoulder = new Button(stick, ID_RIGHT_SHOULDER);
        select        = new Button(stick, ID_SELECT);
        start         = new Button(stick, ID_START);
        leftStickIn   = new Button(stick, ID_LEFT_STICK);
        rightStickIn  = new Button(stick, ID_RIGHT_STICK);

        dpadUp    = new DpadButton(stick,   0,  45, 315);
        dpadDown  = new DpadButton(stick, 135, 180, 225);
        dpadLeft  = new DpadButton(stick, 225, 270, 315);
        dpadRight = new DpadButton(stick,  45,  90, 135);

        leftStickX   = new Axis(stick, AXIS_LEFT_STICK_X, false);
        leftStickY   = new Axis(stick, AXIS_LEFT_STICK_Y, true);
        rightStickX  = new Axis(stick, AXIS_RIGHT_STICK_X, false);
        rightStickY  = new Axis(stick, AXIS_RIGHT_STICK_Y, true);
        leftTrigger  = new Axis(stick, AXIS_LEFT_TRIGGER, false);
        rightTrigger = new Axis(stick, AXIS_RIGHT_TRIGGER, false);
    }

    public void update() {
        a.update();
        b.update();
        x.update();
        y.update();
        leftShoulder.update();
        rightShoulder.update();
        select.update();
        start.update();
        dpadUp.update();
        dpadDown.update();
        dpadLeft.update();
        dpadRight.update();
        leftStickIn.update();
        rightStickIn.update();

        leftStickX.update();
        leftStickY.update();
        rightStickX.update();
        rightStickY.update();
        leftTrigger.update();
        rightTrigger.update();
    }
}
