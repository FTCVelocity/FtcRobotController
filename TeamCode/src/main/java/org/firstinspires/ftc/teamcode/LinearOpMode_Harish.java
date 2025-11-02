package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

public class LinearOpMode_Harish extends com.qualcomm.robotcore.eventloop.opmode.LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor fLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        DcMotor fRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        DcMotor bLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        DcMotor bRightMotor = hardwareMap.get(DcMotor.class, "backRight");

        // Positive power, move forward
        fLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        // Coast to stop slowly, BRAKE will stop it immediately
        fLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        //RUN_USING_ENCODER will measure motor power using TICKS
        fLeftMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        //Wait for start/play button to be pressed on the driver hub
        waitForStart();

        double drive, turn, strafe = 0;

        while(opModeIsActive()){
            fLeftMotor.setPower(1); // Full power with direction set initially
        }
    }
}
