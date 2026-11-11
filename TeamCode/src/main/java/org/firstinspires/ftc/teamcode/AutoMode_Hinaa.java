package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="AutoMode_Hinaa", group="Robot")
public class AutoMode_Hinaa extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor frontleft   = null;
    private DcMotor frontright  = null;
    private DcMotor backleft    = null;
    private DcMotor backright   = null;

    private DcMotor intake;
    private DcMotor outtakeleft;
    private DcMotor outtakeright;

    private CRServo frontWheels;
    private CRServo backWheels;
    private ElapsedTime runtime = new ElapsedTime();

    // Robot constants & calculations
    static final double     COUNTS_PER_MOTOR_REV    = 383.6;    // eg: TETRIX / goBILDA Motor Encoder
    static final double     DRIVE_GEAR_REDUCTION    = 1.0;      // No External Gearing.
    static final double     WHEEL_DIAMETER_INCHES   = 4.0;      // For figuring circumference
    static final double     COUNTS_PER_INCH         = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * 3.1415);
    static final double     DRIVE_SPEED             = 0.6;
    static final double     TURN_SPEED              = 0.5;
    static final double     STRAFE_CORRECTION       = 1.2;      // Adjust to fine-tune strafe distance

    @Override
    public void runOpMode() {

        // Initialize drive system variables
        frontleft  = hardwareMap.get(DcMotor.class, "frontleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backleft   = hardwareMap.get(DcMotor.class, "backleft");
        backright  = hardwareMap.get(DcMotor.class, "backright");

        // Initialize mechanism hardware
        intake = hardwareMap.get(DcMotor.class, "intake");
        outtakeleft = hardwareMap.get(DcMotor.class, "outtakeleft");
        outtakeright = hardwareMap.get(DcMotor.class, "outtakeright");
        frontWheels = hardwareMap.get(CRServo.class, "frontWheels");
        backWheels = hardwareMap.get(CRServo.class, "backWheels");

        // Set motor directions
        frontleft.setDirection(DcMotor.Direction.REVERSE);
        backleft.setDirection(DcMotor.Direction.REVERSE);
        frontright.setDirection(DcMotor.Direction.FORWARD);
        backright.setDirection(DcMotor.Direction.FORWARD);

        // Outtake motor directions
        outtakeleft.setDirection(DcMotor.Direction.REVERSE);
        outtakeright.setDirection(DcMotor.Direction.FORWARD);

        // Set all powers to zero initially
        intake.setPower(0);
        outtakeleft.setPower(0);
        outtakeright.setPower(0);
        frontWheels.setPower(0.0);
        backWheels.setPower(0.0);

        // Reset and configure encoders
        frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        // Send telemetry message to indicate successful initialization
        telemetry.addLine("Initialized — waiting for start");
        telemetry.update();

        // Wait for the game to start (driver presses START)
        waitForStart();

        if (opModeIsActive()) {

            // Step 1: Drive Forward to target zone
            telemetry.addLine("Forward 40...");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 40, 40, 2.0);
            sleep(250);

            // Step 2: Shoot initial pieces
            shootThree();

            // Step 3: Move and turn toward elements
            telemetry.addLine("Forward 10...");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 10, 10, 2.0);

            telemetry.addLine("Turning Right...");
            telemetry.update();
            encoderDrive(TURN_SPEED, 31, -31, 2.0);

            telemetry.addLine("Strafe Right 5...");
            telemetry.update();
            strafeDrive(DRIVE_SPEED, 5, 3.0);

            // Step 4: Pick up next set
            pickUpThree();

            telemetry.addData("Path", "Complete");
            telemetry.update();
            sleep(1000);
        }
    }

    // Method to perform a relative move based on encoder counts
    public void encoderDrive(double speed,
                             double leftInches, double rightInches,
                             double timeoutS) {
        int newfrontleftTarget;
        int newfrontrightTarget;
        int newbackleftTarget;
        int newbackrightTarget;

        if (opModeIsActive()) {
            // Determine new target position
            newfrontleftTarget = frontleft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newfrontrightTarget = frontright.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);
            newbackleftTarget = backleft.getCurrentPosition() + (int)(leftInches * COUNTS_PER_INCH);
            newbackrightTarget = backright.getCurrentPosition() + (int)(rightInches * COUNTS_PER_INCH);

            frontleft.setTargetPosition(newfrontleftTarget);
            frontright.setTargetPosition(newfrontrightTarget);
            backleft.setTargetPosition(newbackleftTarget);
            backright.setTargetPosition(newbackrightTarget);

            // Turn On RUN_TO_POSITION
            frontleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontleft.setPower(Math.abs(speed));
            frontright.setPower(Math.abs(speed));
            backleft.setPower(Math.abs(speed));
            backright.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontleft.isBusy() || frontright.isBusy() ||
                            backleft.isBusy() || backright.isBusy())) {

                telemetry.addData("Running to", " %7d :%7d", newfrontleftTarget, newfrontrightTarget);
                telemetry.addData("Currently at", " at %7d :%7d",
                        frontleft.getCurrentPosition(), frontright.getCurrentPosition());
                telemetry.update();
            }

            // Stop all motion
            frontleft.setPower(0);
            frontright.setPower(0);
            backleft.setPower(0);
            backright.setPower(0);

            // Turn off RUN_TO_POSITION
            frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

            sleep(250);
        }
    }

    // Method to strafe using encoders with correction multiplier
    public void strafeDrive(double speed, double inches, double timeoutS) {
        int flTarget, frTarget, blTarget, brTarget;

        if (opModeIsActive()) {
            int moveCounts = (int)(inches * COUNTS_PER_INCH * STRAFE_CORRECTION);

            flTarget = frontleft.getCurrentPosition() + moveCounts;
            frTarget = frontright.getCurrentPosition() - moveCounts;
            blTarget = backleft.getCurrentPosition() - moveCounts;
            brTarget = backright.getCurrentPosition() + moveCounts;

            frontleft.setTargetPosition(flTarget);
            frontright.setTargetPosition(frTarget);
            backleft.setTargetPosition(blTarget);
            backright.setTargetPosition(brTarget);

            frontleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            frontright.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backleft.setMode(DcMotor.RunMode.RUN_TO_POSITION);
            backright.setMode(DcMotor.RunMode.RUN_TO_POSITION);

            runtime.reset();
            frontleft.setPower(Math.abs(speed));
            frontright.setPower(Math.abs(speed));
            backleft.setPower(Math.abs(speed));
            backright.setPower(Math.abs(speed));

            while (opModeIsActive() &&
                    (runtime.seconds() < timeoutS) &&
                    (frontleft.isBusy() || frontright.isBusy() || backleft.isBusy() || backright.isBusy())) {

                telemetry.addData("Strafing to", "FL:%d FR:%d", flTarget, frTarget);
                telemetry.update();
            }

            frontleft.setPower(0);
            frontright.setPower(0);
            backleft.setPower(0);
            backright.setPower(0);

            frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            sleep(250);
        }
    }

    public void pickUpThree() {
        intake.setPower(-1.0);
        telemetry.addLine("intake ON...");
        telemetry.update();

        telemetry.addLine("Forward 30...");
        telemetry.update();
        encoderDrive(DRIVE_SPEED, 30, 30, 2.0);

        frontWheels.setPower(0.25);
        backWheels.setPower(0.25);
        telemetry.addLine("rollers ON...");
        telemetry.update();
        sleep(250);

        telemetry.addLine("Forward 10...");
        telemetry.update();
        encoderDrive(DRIVE_SPEED, 10, 10, 2.0);
        sleep(250);

        frontWheels.setPower(0.0);
        backWheels.setPower(0.0);
        telemetry.addLine("rollers OFF...");
        telemetry.update();

        intake.setPower(0.0);
        telemetry.addLine("intake OFF...");
        telemetry.update();

        telemetry.addLine("Backward 60...");
        telemetry.update();
        encoderDrive(DRIVE_SPEED, -60, -60, 2.0);

        telemetry.addLine("Turning Left...");
        telemetry.update();
        encoderDrive(TURN_SPEED, -31, 31, 2.0);
    }

    public void shootThree() {
        outtakeleft.setPower(0.8);
        outtakeright.setPower(0.8);
        telemetry.addLine("outtake ON...");
        telemetry.update();

        sleep(1500);

        frontWheels.setPower(1.0);
        backWheels.setPower(1.0);
        telemetry.addLine("rollers ON...");
        telemetry.update();

        intake.setPower(-0.1);
        telemetry.addLine("intake ON...");
        telemetry.update();

        sleep(3500);

        outtakeleft.setPower(0.0);
        outtakeright.setPower(0.0);
        telemetry.addLine("outtake OFF...");
        telemetry.update();

        intake.setPower(0.0);
        telemetry.addLine("intake OFF...");
        telemetry.update();

        frontWheels.setPower(0.0);
        backWheels.setPower(0.0);
        telemetry.addLine("rollers OFF...");
        telemetry.update();
    }
}