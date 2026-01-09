package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.teamcode.configuration.Settings.Positions.STORED_POSE;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.END_IN;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.END_OUT;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.END_STOP;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.INTAKE_SPEED;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.configuration.Settings;
import org.firstinspires.ftc.teamcode.configuration.Sides;
import org.firstinspires.ftc.teamcode.hardware.Launcher;
import org.firstinspires.ftc.teamcode.hardware.System;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name = "TeleOp", group = "OpMode")
public class TeleOp extends LinearOpMode {
	private final ElapsedTime runtime = new ElapsedTime();
	private System system;
	
	@Override
	public void runOpMode() {
		system = new System(hardwareMap);
		
		telemetry.addData("Status", "Initialized");
		telemetry.update();
		
		waitForStart();
		system.follower.setStartingPose(STORED_POSE);
		system.follower.startTeleOpDrive();
		runtime.reset();
		
		// run until the end of the match (driver presses STOP)
		while (opModeIsActive()) {
			system.update();
			// POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
			double axial = -gamepad1.left_stick_y;  // Note: pushing stick forward gives negative value
			double lateral = gamepad1.left_stick_x;
			double yaw = -gamepad1.right_stick_x;
			if (gamepad1.a || gamepad1.b || gamepad1.x || gamepad1.y) {
				if (gamepad1.aWasPressed()) {
					system.follower.holdPoint(Settings.Positions.SIDE == Sides.BLUE ? Settings.Positions.Launch.close : Settings.Positions.Launch.close.mirror());
				}
				if (gamepad1.bWasPressed()) {
					system.follower.holdPoint(Settings.Positions.SIDE == Sides.BLUE ? Settings.Positions.Launch.far : Settings.Positions.Launch.far.mirror());
				}
				if (gamepad1.xWasPressed()) {
					system.follower.holdPoint(Settings.Positions.SIDE == Sides.BLUE ? Settings.Positions.human.HUMAN : Settings.Positions.human.HUMAN.mirror());
				}
				if (gamepad1.yWasPressed()) {
					system.follower.holdPoint(Settings.Positions.SIDE == Sides.BLUE ? Settings.Positions.Endgame.PARK : Settings.Positions.Endgame.PARK.mirror());
				}
			} else {
				if (!system.follower.isTeleopDrive()) {
					system.follower.startTeleOpDrive();
				}
				system.follower.setTeleOpDrive(axial, lateral, yaw);
			}
			// Intake control — use right trigger for intake in, left trigger for reverse
			system.rollers.intake.setPower(INTAKE_SPEED * (gamepad2.right_trigger - gamepad2.left_trigger));
			
			// Outtake control — press 'A' to run both outtakes, 'B' to reverse
			// LOW GOAL
			if (gamepad2.a) {
				system.launcher.setRPM(Settings.Launcher.CLOSE_VELOCITY);
			} else if (gamepad2.b) {
				system.launcher.setRPM(-Settings.Launcher.CLOSE_VELOCITY);
			}
			
			// HIGH GOAL (overrides low)
			if (gamepad2.x) {
				system.launcher.setRPM(Settings.Launcher.FAR_VELOCITY);
			} else if (gamepad2.y) {
				system.launcher.setRPM(Settings.Launcher.FAR_VELOCITY);
			}
			
			if (gamepad2.a || gamepad2.b || gamepad2.x || gamepad2.y) {
				system.launcher.spinUp();
			} else {
				system.launcher.spinDown();
			}
			
			// Roller control (CRServo)
			if (gamepad2.dpad_up) {
				system.rollers.middle.setPower(END_IN);   // forward
				
			} else if (gamepad2.dpad_down) {
				system.rollers.middle.setPower(END_OUT);  // reverse
				
			} else if (gamepad2.dpad_left || gamepad2.dpad_right) {
				system.rollers.middle.setPower(END_STOP);   // stop
				
			}
			if (gamepad1.dpad_up) {
				// forward
				system.rollers.end.setPower(END_IN);
			} else if (gamepad1.dpad_down) {
				// reverse
				system.rollers.end.setPower(END_OUT);
			} else if (gamepad1.dpad_left || gamepad1.dpad_right) {
				// stop
				system.rollers.end.setPower(END_STOP);
			}
			
			// Show the elapsed game time and wheel power.
			telemetry.addData("Pose", system.follower.getPose());
			telemetry.addData("Middle Wheels Power", system.rollers.middle.getPower());
			telemetry.addData("End Wheels Power", system.rollers.end.getPower());
			telemetry.addData("Intake Power", system.rollers.intake.getPower());
			telemetry.addData("Outtake Target", Launcher.ticksPerSecToRPM(system.launcher.targetTPS));
			telemetry.addData("Outtake Actual", system.launcher.getRPM());
			telemetry.addData("Status", "Run Time: " + runtime);
			Drawing.drawDebug(system.follower);
			Drawing.update();
			telemetry.update();
		}
	}
}
