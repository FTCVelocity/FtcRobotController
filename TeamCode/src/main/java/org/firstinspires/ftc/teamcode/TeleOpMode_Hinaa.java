package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="TeleOpMode_Hinaa", group="OpMode")
public class TeleOpMode_Hinaa extends LinearOpMode {

    // Declare OpMode members for each of the 4 motors and mechanisms.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor frontleft = null;
    private DcMotor backleft = null;
    private DcMotor frontright = null;
    private DcMotor backright = null;
    private DcMotor intake;
    private DcMotor outtakeleft;
    private DcMotor outtakeright;
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
        outtakeleft = hardwareMap.get(DcMotor.class, "outtakeleft");
        outtakeright = hardwareMap.get(DcMotor.class, "outtakeright");
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

        // Stop all motors initially
        intake.setPower(0);
        outtakeleft.setPower(0);
        outtakeright.setPower(0);
        frontWheels.setPower(0.0);
        backWheels.setPower(0.0);

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

            // Outtake control — press 'A' to run both outtakes, 'B' to reverse
            double outtakePower = 0;
            if (gamepad2.a) {
                outtakePower = 1.0; // forward
            } else if (gamepad2.b) {
                outtakePower = -1.0; // reverse
            }

            outtakeleft.setPower(outtakePower);
            outtakeright.setPower(outtakePower);

            // Roller control (CRServo) via D-pad
            if (gamepad1.dpad_up) {
                frontWheels.setPower(1.0);   // forward
                backWheels.setPower(1.0);
            }
            else if (gamepad1.dpad_down) {
                frontWheels.setPower(-1.0);  // reverse
                backWheels.setPower(-1.0);
            }
            else if (gamepad1.dpad_left || gamepad1.dpad_right) {
                frontWheels.setPower(0.0);   // stop
                backWheels.setPower(0.0);
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
            telemetry.addData("Outtake Power", outtakePower);
            telemetry.update();
        }
    }
}