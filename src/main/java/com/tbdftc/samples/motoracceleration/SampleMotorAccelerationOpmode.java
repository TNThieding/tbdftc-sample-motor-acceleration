package com.tbdftc.samples.motoracceleration;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@TeleOp(name="Sample: Motor Acceleration", group="Sample")
public class SampleMotorAccelerationOpmode extends OpMode {

    //Constant variables.
    private static final double SLOW_DRIVE_SCALAR = 0.2;
    private static final double STICK_DIGITAL_THRESHOLD = 0.25;
    private static final double TURNING_SCALAR = 0.875;

    //Robot hardware variables.
    private Drivetrain drivetrain;

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        //Note that axes on analog sticks are reversed so that up is positive rather than negative.

        //Drivetrain at slower speed.
        if(isSlowDriveActivated(gamepad1) && !isManualOverrideEnabled(gamepad1)) {
            drivetrain.setMinimumMotorPower(0.05);

            if((-gamepad1.left_stick_y < 0) == (-gamepad1.right_stick_y) < 0) drivetrain.setPower(-gamepad1.left_stick_y * SLOW_DRIVE_SCALAR, -gamepad1.right_stick_y * SLOW_DRIVE_SCALAR);
            else drivetrain.setPower(-gamepad1.left_stick_y * SLOW_DRIVE_SCALAR * TURNING_SCALAR, -gamepad1.right_stick_y * SLOW_DRIVE_SCALAR * TURNING_SCALAR);
        }
        //Drivetrain at regular speed.
        else {
            drivetrain.setMinimumMotorPower(0.3);

            if((-gamepad1.left_stick_y < 0) == (-gamepad1.right_stick_y) < 0) drivetrain.setPower(-gamepad1.left_stick_y, -gamepad1.right_stick_y);
            else drivetrain.setPower(-gamepad1.left_stick_y * TURNING_SCALAR, -gamepad1.right_stick_y * TURNING_SCALAR);
        }
    }

    @Override
    public void stop() {
        drivetrain.stop();
    }

    private boolean isManualOverrideEnabled(Gamepad aGamepad) {
        return (aGamepad.b);
    }

    private boolean isSlowDriveActivated(Gamepad aGamepad) {
        return (aGamepad.left_trigger > STICK_DIGITAL_THRESHOLD || aGamepad.right_trigger > STICK_DIGITAL_THRESHOLD);
    }

}