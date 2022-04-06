package com.swrobotics.bert.commands.climber;

import com.swrobotics.bert.Robot;
import com.swrobotics.bert.commands.Command;
import com.swrobotics.bert.control.Input;
import com.swrobotics.bert.subsystems.climber.Climber;
import com.swrobotics.bert.subsystems.climber.rotating.RotatingArms;
import com.swrobotics.bert.subsystems.climber.telescoping.TelescopingArms;

import static com.swrobotics.bert.constants.ClimberConstants.*;

import com.kauailabs.navx.frc.AHRS;

public final class FullClimb implements Command {
    private enum ClimbStep {BASE_1, 
                            ARMS_UP_1_5, 
                            PULL_UP_2,
                            LOCK_IN_3,
                            HANDOFF_4,
                            SWING_4_5,
                            EXTEND_5,
                            PRESSURE_6}
    private ClimbStep climbStep = ClimbStep.BASE_1;
    // 1:   Base
    // 1.5: Arms up
    // 2:   Pull up
    // 3:   Lock in
    // 4:   Handoff
    // 4.5: Wait to swing past 3
    // 5:   Extend to 3
    // 6:   Pressure on 3
    // CALL 2!
    private double teleSetpoint=0, rotSetpoint=90;
    private Boolean loaded = false;
    private double previousAngle = 0, newAngle = 0;
    private AHRS gyro;

    private TelescopingArms telescopingArms;
    private RotatingArms rotatingArms;
    private Input input;
    
    public FullClimb(Climber climber, Input input, AHRS gyro) {
        this.telescopingArms = climber.getTeles();
        this.rotatingArms = climber.getRots();  
        this.input = input;
        this.gyro = gyro;
    }

    private void switchToStep(ClimbStep step) {
        climbStep = step;
        System.out.println("Switching to step: " + step.name());
    }

    private void updateGyro() {     previousAngle = newAngle;
                                    newAngle = getGyro();
                                    System.out.println("FullClimb.updateGyro() - angle: " + newAngle);}
    private double getGyro() {      return -gyro.getPitch();}
    private boolean positiveRate() {return newAngle >= previousAngle;}
    private boolean pastBar(double barAngle) {return newAngle > barAngle;}

    @Override
    public void init() {

    }

    @Override
    public boolean run() {
        updateGyro();
        switch (climbStep) {
            case BASE_1:
                //TELE & ROT
                telescopingArms.setTargetDistancePercent(CLIMB_STEP_1_TELE.get());
                telescopingArms.setLoaded(false);
                rotatingArms.setTargetAngleDegrees(CLIMB_STEP_1_ROT.get());
                if (input.getClimberNextStep()) {
                    switchToStep(ClimbStep.ARMS_UP_1_5);
                }
              break;
            case ARMS_UP_1_5:
                //TELE
                telescopingArms.setTargetDistancePercent(CLIMB_STEP_1_5_TELE.get());
                telescopingArms.setLoaded(false);
                rotatingArms.setTargetAngleDegrees(CLIMB_STEP_1_ROT.get());
                // teleSetpoint = CLIMB_STEP_1_5_TELE.get();
                // rotSetpoint = CLIMB_STEP_1_ROT.get();
                // loaded = false;
                if (telescopingArms.isInTolarence() && input.getClimberNextStep()) {switchToStep(ClimbStep.PULL_UP_2);}
                if (input.getClimberPreviousStep()) {switchToStep(ClimbStep.BASE_1);}
                break;
            case PULL_UP_2:
                //TELE
                telescopingArms.setTargetDistancePercent(CLIMB_STEP_2_TELE.get());
                telescopingArms.setLoaded(true);
                rotatingArms.setTargetAngleDegrees(CLIMB_STEP_1_ROT.get());
                // teleSetpoint = CLIMB_STEP_2_TELE.get();
                // rotSetpoint = CLIMB_STEP_1_ROT.get();
                // loaded = true;
                if (telescopingArms.isInTolarence() && input.getClimberNextStep()) {switchToStep(ClimbStep.LOCK_IN_3);}
                if (input.getClimberPreviousStep()) {switchToStep(ClimbStep.ARMS_UP_1_5);}
                break;
            case LOCK_IN_3:
                //ROT
                telescopingArms.setTargetDistancePercent(CLIMB_STEP_2_TELE.get());
                telescopingArms.setLoaded(true);
                rotatingArms.setTargetAngleDegrees(CLIMB_STEP_3_ROT.get());
                // teleSetpoint = CLIMB_STEP_2_TELE.get();
                // rotSetpoint = CLIMB_STEP_3_ROT.get();
                // loaded = true;
                if (rotatingArms.isInTolerance() && input.getClimberNextStep()) {switchToStep(ClimbStep.HANDOFF_4);}
                if (input.getClimberPreviousStep()) {switchToStep(ClimbStep.PULL_UP_2);}
                break;
            case HANDOFF_4:
                //TELE & ROT & ANGLE
                telescopingArms.setTargetDistancePercent(CLIMB_STEP_4_TELE.get());
                telescopingArms.setLoaded(false);
                rotatingArms.setTargetAngleDegrees(CLIMB_STEP_4_ROT.get());
                // teleSetpoint = CLIMB_STEP_4_TELE.get();
                // rotSetpoint = CLIMB_STEP_4_ROT.get();
                // loaded = false;
                System.out.println("FullClimb.run() - PR:" + positiveRate() + "      PB:" + pastBar(CLIMB_STEP_4_5_GYRO.get()));
                if (rotatingArms.isInTolerance() && positiveRate() && pastBar(CLIMB_STEP_4_5_GYRO.get()) && input.getClimberNextStep()) {switchToStep(ClimbStep.EXTEND_5);}
                if (input.getClimberPreviousStep()) {switchToStep(ClimbStep.LOCK_IN_3);}
              break;
            case EXTEND_5:
                //TELE
                telescopingArms.setTargetDistancePercent(CLIMB_STEP_5_TELE.get());
                telescopingArms.setLoaded(false);
                rotatingArms.setTargetAngleDegrees(CLIMB_STEP_4_ROT.get());
                // teleSetpoint = CLIMB_STEP_5_TELE.get();
                // rotSetpoint = CLIMB_STEP_4_ROT.get();
                // loaded = false;
                if (telescopingArms.isInTolarence() && input.getClimberNextStep()) {switchToStep(ClimbStep.PRESSURE_6);}
                if (input.getClimberPreviousStep()) {switchToStep(ClimbStep.HANDOFF_4);}
              break;
            case PRESSURE_6:
                //ROT
                telescopingArms.setTargetDistancePercent(CLIMB_STEP_5_TELE.get());
                telescopingArms.setLoaded(false);
                rotatingArms.setTargetAngleDegrees(CLIMB_STEP_6_ROT.get());
                // teleSetpoint = CLIMB_STEP_5_TELE.get();
                // rotSetpoint = CLIMB_STEP_6_ROT.get();
                // loaded = false;
                if (rotatingArms.isInTolerance() && input.getClimberNextStep()) {switchToStep(ClimbStep.PULL_UP_2);}
                if (input.getClimberPreviousStep()) {switchToStep(ClimbStep.EXTEND_5);}
              break;
          }
          // telescopingArms.setTargetDistancePercent(teleSetpoint);
          // telescopingArms.setLoaded(loaded);
          // rotatingArms.setTargetAngleDegrees(rotSetpoint);
          if (Robot.get().isDisabled()) {
            return true;
          } else {
            return false;
          }
    }

    @Override
    public void end() {
        System.out.println("ClimberStep.end()");
        rotatingArms.stop();
        telescopingArms.stop();

    }
}
