package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Auto_Blue", group = "Robot")
public class AutoMode_Hinaa extends LinearOpMode {

    // Robot constants & calculations
    static final double COUNTS_PER_MOTOR_REV = 383.6;
    static final double DRIVE_GEAR_REDUCTION = 1.0;     // No External Gearing.
    static final double WHEEL_DIAMETER_INCHES = 4.0;     // For figuring circumference
    static final double COUNTS_PER_INCH = (COUNTS_PER_MOTOR_REV * DRIVE_GEAR_REDUCTION) /
            (WHEEL_DIAMETER_INCHES * Math.PI);

    static final double DRIVE_SPEED = 0.6;
    static final double TURN_SPEED = 0.5;
    static final double STRAFE_CORRECTION = 1.08;        // Adjust to fine-tune strafe distance
    static final double COUNTS_PER_DEGREE = 0.87;        // STARTING VALUE – MUST TUNE

    // Outtake PIDF constants
    static final double OUTTAKE_P = 0.544;
    static final double OUTTAKE_I = 0.0;
    static final double OUTTAKE_D = 0.0;
    static final double OUTTAKE_F = 12.103;
    static final double OUTTAKE_VELOCITY = 1500; // ticks/sec

    // Declare OpMode members.
    private DcMotor frontleft = null;
    private DcMotor frontright = null;
    private DcMotor backleft = null;
    private DcMotor backright = null;
    private DcMotor intake;
    private DcMotorEx outtakeleft;
    private DcMotorEx outtakeright;
    private CRServo frontWheels;
    private CRServo backWheels;
    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // Initialize the drive system variables.
        frontleft = hardwareMap.get(DcMotor.class, "frontleft");
        frontright = hardwareMap.get(DcMotor.class, "frontright");
        backleft = hardwareMap.get(DcMotor.class, "backleft");
        backright = hardwareMap.get(DcMotor.class, "backright");

        // Initialize mechanism hardware
        intake = hardwareMap.get(DcMotor.class, "intake");
        outtakeleft = hardwareMap.get(DcMotorEx.class, "outtakeleft");
        outtakeright = hardwareMap.get(DcMotorEx.class, "outtakeright");
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

        // Configure Outtake PIDF coefficients
        PIDFCoefficients outtakePIDF =
                new PIDFCoefficients(OUTTAKE_P, OUTTAKE_I, OUTTAKE_D, OUTTAKE_F);

        outtakeleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        outtakeright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        outtakeleft.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, outtakePIDF);
        outtakeright.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, outtakePIDF);

        // Reset and configure encoders
        frontleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        frontright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backleft.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        backright.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        frontleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        frontright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backleft.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        backright.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        telemetry.addData("Starting at", "%7d :%7d",
                frontleft.getCurrentPosition(),
                frontright.getCurrentPosition(),
                backleft.getCurrentPosition(),
                backright.getCurrentPosition());
        telemetry.addLine("Initialized — waiting for start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            telemetry.addLine("Forward 47...");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 47, 47, 2.0);
            sleep(250);

            telemetry.addLine("Outtake spin-up...");
            telemetry.update();
            runOuttakeSynced(OUTTAKE_VELOCITY, 2.2);

            frontWheels.setPower(1.0);
            backWheels.setPower(1.0);
            telemetry.addLine("rollers ON...");
            telemetry.update();

            intake.setPower(-1.0);
            telemetry.addLine("intake ON...");
            telemetry.update();

            sleep(3500);

            outtakeleft.setVelocity(0);
            outtakeright.setVelocity(0);
            telemetry.addLine("outtake OFF...");
            telemetry.update();

            intake.setPower(0.0);
            telemetry.addLine("intake OFF...");
            telemetry.update();

            frontWheels.setPower(0.0);
            backWheels.setPower(0.0);
            telemetry.addLine("rollers OFF...");
            telemetry.update();

            telemetry.addLine("Turning Right 35");
            telemetry.update();
            turnRight(35);

            strafeDrive(5, -10, 2.0);

            intake.setPower(-1.0);
            telemetry.addLine("intake ON...");
            telemetry.update();

            frontWheels.setPower(0.25);
            telemetry.addLine("front rollers ON...");
            telemetry.update();

            telemetry.addLine("Forward 45...");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 45, 45, 3.0);
            sleep(250);

            frontWheels.setPower(0.0);
            backWheels.setPower(0.0);
            telemetry.addLine("rollers OFF...");
            telemetry.update();

            intake.setPower(0.0);
            telemetry.addLine("intake OFF...");
            telemetry.update();

            telemetry.addLine("Backward 43...");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, -43, -43, 2.0);

            telemetry.addLine("Turning Left 35");
            telemetry.update();
            turnLeft(35);

            telemetry.addLine("Outtake spin-up...");
            telemetry.update();
            runOuttakeSynced(OUTTAKE_VELOCITY, 2.2);

            intake.setPower(-1.0);
            telemetry.addLine("intake ON...");
            telemetry.update();
            frontWheels.setPower(1.0);
            backWheels.setPower(1.0);
            telemetry.addLine("rollers ON...");
            telemetry.update();

            sleep(3500);

            outtakeleft.setVelocity(0);
            outtakeright.setVelocity(0);
            telemetry.addLine("outtake OFF...");
            telemetry.update();

            intake.setPower(0.0);
            telemetry.addLine("intake OFF...");
            telemetry.update();

            frontWheels.setPower(0.0);
            backWheels.setPower(0.0);
            telemetry.addLine("rollers OFF...");
            telemetry.update();

            telemetry.addLine("Turning Right 35");
            telemetry.update();
            turnRight(35);

            strafeDrive(5, -20, 2.0);

            intake.setPower(-1.0);
            telemetry.addLine("intake ON...");
            telemetry.update();

            frontWheels.setPower(0.25);
            telemetry.addLine("front rollers ON...");
            telemetry.update();

            telemetry.addLine("Forward 40...");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, 40, 40, 2.0);
            sleep(250);

            frontWheels.setPower(0.0);
            backWheels.setPower(0.0);
            telemetry.addLine("rollers OFF...");
            telemetry.update();

            intake.setPower(0.0);
            telemetry.addLine("intake OFF...");
            telemetry.update();

            telemetry.addLine("Backward 40...");
            telemetry.update();
            encoderDrive(DRIVE_SPEED, -40, -40, 2.0);

            strafeDrive(5, 20, 2.0);

            telemetry.addLine("Turning Left 35");
            telemetry.update();
            turnLeft(35);

            telemetry.addLine("Outtake spin-up...");
            telemetry.update();
            runOuttakeSynced(OUTTAKE_VELOCITY, 2.2);

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
            newfrontleftTarget = frontleft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newfrontrightTarget = frontright.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);
            newbackleftTarget = backleft.getCurrentPosition() + (int) (leftInches * COUNTS_PER_INCH);
            newbackrightTarget = backright.getCurrentPosition() + (int) (rightInches * COUNTS_PER_INCH);

            frontleft.setTargetPosition(newfrontleftTarget);
            frontright.setTargetPosition(newfrontrightTarget);
            backleft.setTargetPosition(newbackleftTarget);
            backright.setTargetPosition(newbackrightTarget);

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

    public void strafeDrive(double speed, double inches, double timeoutS) {
        int flTarget, frTarget, blTarget, brTarget;

        if (opModeIsActive()) {
            int moveCounts = (int) (inches * COUNTS_PER_INCH * STRAFE_CORRECTION);

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

    public void turnRight(double degrees) {
        encoderDrive(
                TURN_SPEED,
                degrees * COUNTS_PER_DEGREE,
                -degrees * COUNTS_PER_DEGREE,
                2.0
        );
    }

    public void turnLeft(double degrees) {
        encoderDrive(
                TURN_SPEED,
                -degrees * COUNTS_PER_DEGREE,
                degrees * COUNTS_PER_DEGREE,
                2.0
        );
    }

    public void runOuttakeSynced(double targetVelocity, double durationSeconds) {
        ElapsedTime timer = new ElapsedTime();
        timer.reset();

        while (opModeIsActive() && timer.seconds() < durationSeconds) {
            outtakeleft.setVelocity(targetVelocity);
            outtakeright.setVelocity(targetVelocity);

            double leftVel = outtakeleft.getVelocity();
            double rightVel = outtakeright.getVelocity();

            double avgVel = (leftVel + rightVel) / 2.0;

            if (Math.abs(avgVel) > Math.abs(targetVelocity)) {
                avgVel = targetVelocity;
            }

            outtakeleft.setVelocity(avgVel);
            outtakeright.setVelocity(avgVel);

            telemetry.addData("Outtake Target", targetVelocity);
            telemetry.addData("Left Vel", "%.1f", leftVel);
            telemetry.addData("Right Vel", "%.1f", rightVel);
            telemetry.addData("Velocity Δ", "%.1f", rightVel - leftVel);
            telemetry.update();
        }
    }
}