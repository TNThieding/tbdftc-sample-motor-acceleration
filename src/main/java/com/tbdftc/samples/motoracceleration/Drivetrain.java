package com.tbdftc.samples.motoracceleration;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorController;
import com.qualcomm.robotcore.hardware.HardwareMap;

import static com.qualcomm.robotcore.hardware.DcMotorSimple.Direction.*;

public class Drivetrain {

    private static final int ENCODER_PORT_1 = 1;
    private static final int ENCODER_PORT_2 = 2;
    private static final double WHEEL_ACCEL_SPEED_PER_SECOND_STRAIGHT = 0.8;
    private static final double WHEEL_DECEL_SPEED_PER_SECOND_STRAIGHT = 15;
    private static final double WHEEL_ACCEL_SPEED_PER_SECOND_TURNING = 15;
    private static final double WHEEL_DECEL_SPEED_PER_SECOND_TURNING = 15;
    private static final double WHEEL_MINIMUM_POWER = 0.3; //Allows for deadband compensation.
    private static final double WHEEL_MAXIMUM_POWER = 1.0;

    private DcMotorAccelerationThread wheelAccelerationThread;
    private DcMotorController wheelControllerLeft;
    private DcMotorController wheelControllerRight;
    private DcMotorAccelerated wheelLeftFront;
    private DcMotorAccelerated wheelLeftRear;
    private DcMotorAccelerated wheelRightFront;
    private DcMotorAccelerated wheelRightRear;

    public Drivetrain(HardwareMap aHardwareMap) {
        wheelControllerLeft = aHardwareMap.dcMotorController.get("wheelsLeft");
        wheelControllerRight = aHardwareMap.dcMotorController.get("wheelsRight");
        wheelLeftFront = new DcMotorAccelerated(aHardwareMap.dcMotor.get("wheelLeftFront"), WHEEL_ACCEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_DECEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_MINIMUM_POWER, WHEEL_MAXIMUM_POWER);
        wheelLeftRear = new DcMotorAccelerated(aHardwareMap.dcMotor.get("wheelLeftRear"), WHEEL_ACCEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_DECEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_MINIMUM_POWER, WHEEL_MAXIMUM_POWER);
        wheelRightFront = new DcMotorAccelerated(aHardwareMap.dcMotor.get("wheelRightFront"), WHEEL_ACCEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_DECEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_MINIMUM_POWER, WHEEL_MAXIMUM_POWER);
        wheelRightRear = new DcMotorAccelerated(aHardwareMap.dcMotor.get("wheelRightRear"), WHEEL_ACCEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_DECEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_MINIMUM_POWER, WHEEL_MAXIMUM_POWER);

        wheelRightFront.setDirection(REVERSE);
        wheelRightRear.setDirection(REVERSE);
        runWithoutEncoderPid();

        wheelAccelerationThread = new DcMotorAccelerationThread();
        wheelAccelerationThread.addMotor(wheelLeftFront);
        wheelAccelerationThread.addMotor(wheelLeftRear);
        wheelAccelerationThread.addMotor(wheelRightFront);
        wheelAccelerationThread.addMotor(wheelRightRear);
        wheelAccelerationThread.start();
    }

    public void brake() {
        //Use this method for instantly stopping in the middle of an opmode.
        //DO NOT use this in the stop() method of an opmode.
        wheelLeftFront.stopMotorHard();
        wheelLeftRear.stopMotorHard();
        wheelRightFront.stopMotorHard();
        wheelRightRear.stopMotorHard();
    }

    public void runWithoutEncoderPid() {
        wheelControllerLeft.setMotorMode(ENCODER_PORT_1, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wheelControllerLeft.setMotorMode(ENCODER_PORT_2, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wheelControllerRight.setMotorMode(ENCODER_PORT_1, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        wheelControllerRight.setMotorMode(ENCODER_PORT_2, DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    public void setPower(double aLeftPower, double aRightPower) {
        if((aLeftPower < 0) == (aRightPower < 0)) setAccelerationRate(WHEEL_ACCEL_SPEED_PER_SECOND_STRAIGHT, WHEEL_DECEL_SPEED_PER_SECOND_STRAIGHT);
        else setAccelerationRate(WHEEL_ACCEL_SPEED_PER_SECOND_TURNING, WHEEL_DECEL_SPEED_PER_SECOND_TURNING);

        wheelLeftFront.setTargetPower(aLeftPower);
        wheelLeftRear.setTargetPower(aLeftPower);
        wheelRightFront.setTargetPower(aRightPower);
        wheelRightRear.setTargetPower(aRightPower);
    }

    public void setPowerWithoutAcceleration(double aLeftPower, double aRightPower) {
        wheelLeftFront.setDirectPower(aLeftPower);
        wheelLeftRear.setDirectPower(aLeftPower);
        wheelRightFront.setDirectPower(aRightPower);
        wheelRightRear.setDirectPower(aRightPower);
    }

    private void setAccelerationRate(double anAcceleration, double aDeceleration){
        wheelLeftFront.setAccelerationRates(anAcceleration, aDeceleration);
        wheelLeftRear.setAccelerationRates(anAcceleration, aDeceleration);
        wheelRightFront.setAccelerationRates(anAcceleration, aDeceleration);
        wheelRightRear.setAccelerationRates(anAcceleration, aDeceleration);
    }

    public void setMinimumMotorPower(double aMinimumPower) {
        wheelLeftFront.setMinPower(aMinimumPower);
        wheelLeftRear.setMinPower(aMinimumPower);
        wheelRightFront.setMinPower(aMinimumPower);
        wheelRightRear.setMinPower(aMinimumPower);
    }

    public void stop() {
        //Use this method for ending the thread and stopping the motors at the end of an opmode.
        //DO NOT use this in the middle of an opmode as it kills the thread.
        wheelAccelerationThread.stop();
    }

}
