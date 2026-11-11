package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Intake and Outtake", group = "Autonomous")
public class Auto_InAndOuttake_Hinaa extends LinearOpMode {

    private DcMotor intake;
    private DcMotor outtakeleft;
    private DcMotor outtakeright;

    @Override
    public void runOpMode() {
        // ---- Initialize Hardware ----
        intake = hardwareMap.get(DcMotor.class, "intake");
        outtakeleft = hardwareMap.get(DcMotor.class, "outtakeleft");
        outtakeright = hardwareMap.get(DcMotor.class, "outtakeright");

        // Make the two outtake motors spin in coordination
        outtakeleft.setDirection(DcMotor.Direction.FORWARD);
        outtakeright.setDirection(DcMotor.Direction.REVERSE);
        intake.setDirection(DcMotor.Direction.FORWARD);

        // Set all powers to zero initially
        intake.setPower(0);
        outtakeleft.setPower(0);
        outtakeright.setPower(0);

        telemetry.addLine("Initialized — waiting for start");
        telemetry.update();

        waitForStart();

        if (opModeIsActive()) {
            // Step 1: Run intake to pick up pixel / artifact
            intake.setPower(1.0);
            telemetry.addLine("Status: Running intake...");
            telemetry.update();
            sleep(2000);  // Run intake for 2 seconds

            // Step 2: Stop intake and pause briefly
            intake.setPower(0);
            telemetry.addLine("Status: Intake stopped");
            telemetry.update();
            sleep(500);

            // Step 3: Run outtake to shoot/score
            outtakeleft.setPower(1.0);
            outtakeright.setPower(1.0);
            telemetry.addLine("Status: Shooting artifact...");
            telemetry.update();
            sleep(3000);  // Spin shooters for 3 seconds

            // Step 4: Stop all motors and wrap up
            intake.setPower(0);
            outtakeleft.setPower(0);
            outtakeright.setPower(0);

            telemetry.addLine("Status: Auto Complete");
            telemetry.update();
        }
    }
}