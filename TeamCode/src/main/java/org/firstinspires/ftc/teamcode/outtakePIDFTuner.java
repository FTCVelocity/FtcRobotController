package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;


@TeleOp
public class outtakePIDFTuner extends OpMode {
    public DcMotorEx outtakeright;
    public DcMotorEx outtakeleft;

    public double highVelocity = 2700;
    public double lowVelocity = 1500;
    double curTargetVelocity = highVelocity;

    double F = 0;
    double P = 0;

    double[] stepSizes = {10.0, 1.0, 0.1, 0.001, 0.0001};

    int stepIndex = 1;


    @Override
    public void init() {
        outtakeright = hardwareMap.get(DcMotorEx.class, "outtakeright");
        outtakeleft = hardwareMap.get(DcMotorEx.class, "outtakeleft");

        outtakeright.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        outtakeleft.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        outtakeright.setDirection(DcMotorSimple.Direction.FORWARD);
        outtakeleft.setDirection(DcMotorSimple.Direction.REVERSE);

        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        outtakeright.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        outtakeleft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        telemetry.addLine("Init complete");


    }

    @Override
    public void loop() {
        // get all our gamepad commands
        // set target velocity
        //update telemetry
        if (gamepad1.yWasPressed()) {
            if (curTargetVelocity == highVelocity) {
                curTargetVelocity = lowVelocity;
            } else {
                curTargetVelocity = highVelocity;
            }
        }
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;

        }
        if (gamepad1.dpadLeftWasPressed()) {
            F -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadRightWasPressed()) {
            F += stepSizes[stepIndex];
        }
        if (gamepad1.dpadDownWasPressed()) {
            P -= stepSizes[stepIndex];
        }
        if (gamepad1.dpadUpWasPressed()) {
            P += stepSizes[stepIndex];
        }

        //set now PIDF coefficients
        PIDFCoefficients pidfCoefficients = new PIDFCoefficients(P, 0, 0, F);
        outtakeright.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);
        outtakeleft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfCoefficients);

        // set initial target velocity
        outtakeright.setVelocity(curTargetVelocity);
        outtakeleft.setVelocity(curTargetVelocity);

        // read actual velocities
        double rightVelocity = outtakeright.getVelocity();
        double leftVelocity = outtakeleft.getVelocity();

        // 🔑 FORCE SYNCHRONIZATION (add this)
        double avgVelocity = (rightVelocity + leftVelocity) / 2.0;
        outtakeright.setVelocity(avgVelocity);
        outtakeleft.setVelocity(avgVelocity);

        // recompute errors after syncing
        double righterror = curTargetVelocity - rightVelocity;
        double lefterror = curTargetVelocity - leftVelocity;
        double velocityDelta = rightVelocity - leftVelocity;

        avgVelocity = Math.min(avgVelocity, curTargetVelocity);

        // Telemetry
        telemetry.addData("Target Velocity", curTargetVelocity);
        telemetry.addData("Right Velocity", "%.2f", rightVelocity);
        telemetry.addData("Left Velocity", "%.2f", leftVelocity);
        telemetry.addData("Right Error", "%.2f", righterror);
        telemetry.addData("Left Error", "%.2f", lefterror);
        telemetry.addLine("--------------------------------");
        telemetry.addData("Tuning P", "%.4f (D-Pad Up/Down)", P);
        telemetry.addData("Tuning F", "%.4f (D-Pad Left/Right)", F);
        telemetry.addData("Step Size", "%.4f (B Button)", stepSizes[stepIndex]);
    }
}
