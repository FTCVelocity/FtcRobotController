package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "TeleOpMode", group = "OpMode")
public class TeleOpMode_Hinaa extends LinearOpMode {

    // Outtake Constants & PIDF tuning values
    static final double OUTTAKE_P = 0.544;
    static final double OUTTAKE_I = 0.0;
    static final double OUTTAKE_D = 0.0;
    static final double OUTTAKE_F = 12.103;
    static final double OUTTAKE_VELOCITY = 1500; // ticks/sec (Low Goal)

    static final double OUTTAKE_P2 = 20.0;
    static final double OUTTAKE_I2 = 0.0;
    static final double OUTTAKE_D2 = 0.0;
    static final double OUTTAKE_F2 = 41.0;
    static final double OUTTAKE_VELOCITY2 = 2700; // ticks/sec (High Goal)

    // Declare OpMode members.
    private final ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontleft = null;
    private DcMotor backleft = null;
    private DcMotor frontright = null;
    private DcMotor backright = null;
    private DcMotor intake;
    private DcMotorEx outtakeleft;
    private DcMotorEx outtakeright;
    private CRServo frontWheels;
    private CRServo backWheels;

    @Override
    public void runOpMode() {

        // Initialize the hardware variables.
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        backleft = hardwareMap.get(DcMotor.class, "backleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backright = hardwareMap.get(DcMotor.class, "backright");

        intake = hardwareMap.get(DcMotor.class, "intake");

        outtakeleft = hardwareMap.get(DcMotorEx.class, "outtakeleft");
        outtakeright = hardwareMap.get(DcMotorEx.class, "outtakeright");

        frontWheels = hardwareMap.get(CRServo.class, "frontWheels");
        backWheels = hardwareMap.get(CRServo.class, "backWheels");

        // Set outtake directions
        outtakeleft.setDirection(DcMotor.Direction.FORWARD);
        outtakeright.setDirection(DcMotor.Direction.REVERSE);

        // Set drive motor directions
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.FORWARD);
        backright.setDirection(DcMotor.Direction.FORWARD);

        // Set zero power behavior to BRAKE
        frontleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backleft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backright.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Stop motors initially
        intake.setPower(0);
        outtakeleft.setPower(0);
        outtakeright.setPower(0);
        frontWheels.setPower(0.0);
        backWheels.setPower(0.0);

        // Configure Outtake PIDF Coefficients
        PIDFCoefficients outtakePIDF =
                new PIDFCoefficients(OUTTAKE_P, OUTTAKE_I, OUTTAKE_D, OUTTAKE_F);

        outtakeleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        outtakeleft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, outtakePIDF);
        outtakeright.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, outtakePIDF);

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();
        runtime.reset();

        // Run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            double max;

            // POV Mode uses left joystick to go forward/backward & strafe, and right joystick to rotate.
            double axial = -gamepad1.left_stick_y;
            double lateral = gamepad1.left_stick_x;
            double yaw = gamepad1.right_stick_x;

            // Combine joystick requests for mecanum drive wheel power.
            double frontleftPower = axial + lateral + yaw;
            double frontrightPower = axial - lateral - yaw;
            double backleftPower = axial - lateral + yaw;
            double backrightPower = axial + lateral - yaw;

            // Normalize wheel power so no value exceeds 1.0
            max = Math.max(Math.abs(frontleftPower), Math.abs(frontrightPower));
            max = Math.max(max, Math.abs(backleftPower));
            max = Math.max(max, Math.abs(backrightPower));

            if (max > 1.0) {
                frontleftPower /= max;
                frontrightPower /= max;
                backleftPower /= max;
                backrightPower /= max;
            }

            // Intake control — use right trigger for intake in, left trigger for reverse
            double rawPower = gamepad2.right_trigger - gamepad2.left_trigger;
            double intakePower = rawPower * 0.7;
            intake.setPower(intakePower);

            // Outtake velocity selection
            double outtakeTargetVelocity = 0;

            // LOW GOAL (A / B)
            if (gamepad2.a) {
                outtakeTargetVelocity = OUTTAKE_VELOCITY;
            } else if (gamepad2.b) {
                outtakeTargetVelocity = -OUTTAKE_VELOCITY;
            }

            // HIGH GOAL (X / Y - overrides low)
            if (gamepad2.x) {
                outtakeTargetVelocity = OUTTAKE_VELOCITY2;
            } else if (gamepad2.y) {
                outtakeTargetVelocity = -OUTTAKE_VELOCITY2;
            }

            // Apply target velocity
            outtakeleft.setVelocity(outtakeTargetVelocity);
            outtakeright.setVelocity(outtakeTargetVelocity);

            // Read actual velocities for synchronization
            double leftVel = outtakeleft.getVelocity();
            double rightVel = outtakeright.getVelocity();

            // 🔑 Average to sync motors smoothly
            double avgVel = (leftVel + rightVel) / 2.0;

            if (Math.abs(avgVel) > Math.abs(outtakeTargetVelocity)) {
                avgVel = outtakeTargetVelocity;
            }

            // Apply synced velocity back to motors
            outtakeleft.setVelocity(avgVel);
            outtakeright.setVelocity(avgVel);

            // Front Roller control (CRServo) via Gamepad 2
            if (gamepad2.dpad_up) {
                frontWheels.setPower(1.0);   // forward
            } else if (gamepad2.dpad_down) {
                frontWheels.setPower(-1.0);  // reverse
            } else if (gamepad2.dpad_left || gamepad2.dpad_right) {
                frontWheels.setPower(0.0);   // stop
            }

            // Back Roller control (CRServo) via Gamepad 1
            if (gamepad1.dpad_up) {
                backWheels.setPower(1.0);   // forward
            } else if (gamepad1.dpad_down) {
                backWheels.setPower(-1.0);  // reverse
            } else if (gamepad1.dpad_left || gamepad1.dpad_right) {
                backWheels.setPower(0.0);   // stop
            }

            // Send calculated power to drive wheels
            frontleft.setPower(frontleftPower);
            frontright.setPower(frontrightPower);
            backleft.setPower(backleftPower);
            backright.setPower(backrightPower);

            // Show telemetry data
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", frontleftPower, frontrightPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", backleftPower, backrightPower);
            telemetry.addData("Front Wheels Pos", frontWheels.getPower());
            telemetry.addData("Back Wheels Pos", backWheels.getPower());
            telemetry.addData("Intake Power", intakePower);
            telemetry.addData("Outtake Target", outtakeTargetVelocity);
            telemetry.addData("Left Vel", "%.1f", leftVel);
            telemetry.addData("Right Vel", "%.1f", rightVel);
            telemetry.addData("Velocity Δ (R-L)", "%.1f", rightVel - leftVel);
            telemetry.update();
        }
    }
}