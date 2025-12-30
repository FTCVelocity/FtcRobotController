package org.firstinspires.ftc.teamcode.hardware;

import static org.firstinspires.ftc.teamcode.configuration.Settings.Launcher.FAR_VELOCITY;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Launcher.MAX_SPEED_ERROR;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Launcher.TICKS_PER_REVOLUTION;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;

public class Launcher {
	private static final double VELOCITY_ALPHA = 0.15; // EMA smoothing factor (0-1), lower = more smoothing
	private final DcMotorEx rightMotor;
	private final DcMotorEx leftMotor;
	public double targetTPS = 0;
	private LauncherState state = LauncherState.IDLE;
	private double averagedRightRPM = 0;
	private double averagedLeftRPM = 0;
	
	public Launcher(
			DcMotorEx launcherRight,
			DcMotorEx launcherLeft) {
		
		this.rightMotor = launcherRight;
		this.leftMotor = launcherLeft;
		
		rightMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		leftMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
		
		leftMotor.setDirection(DcMotor.Direction.REVERSE);
		rightMotor.setDirection(DcMotor.Direction.FORWARD);
		
		setRPM(FAR_VELOCITY);
	}
	
	public static double rpmToTicksPerSec(double rpm) {
		return rpm * TICKS_PER_REVOLUTION / 60.0;
	}
	
	/**
	 * Converts motor velocity in ticks per second to RPM.
	 * Use this when reading motor velocity and displaying as RPM.
	 *
	 * @param ticksPerSec velocity in ticks per second
	 * @return revolutions per minute
	 */
	public static double ticksPerSecToRPM(double ticksPerSec) {
		return ticksPerSec / TICKS_PER_REVOLUTION * 60.0;
	}
	
	public final void update() {
		// Update time-averaged velocity readings using exponential moving average
		// This smooths out noise from instantaneous velocity readings
		double currentRightRPM = ticksPerSecToRPM(rightMotor.getVelocity());
		double currentLeftRPM = ticksPerSecToRPM(leftMotor.getVelocity());
		
		// Apply exponential moving average: new = alpha * current + (1 - alpha) * old
		// This naturally handles initialization (if averaged is 0, it will gradually
		// build up)
		averagedRightRPM = VELOCITY_ALPHA * currentRightRPM + (1 - VELOCITY_ALPHA) * averagedRightRPM;
		averagedLeftRPM = VELOCITY_ALPHA * currentLeftRPM + (1 - VELOCITY_ALPHA) * averagedLeftRPM;
	}
	
	public void spinUp() {
		state = LauncherState.ACTIVE;
		leftMotor.setVelocity(targetTPS);
		rightMotor.setVelocity(targetTPS);
	}
	
	public void spinDown() {
		state = LauncherState.IDLE;
		leftMotor.setVelocity(0);
		rightMotor.setVelocity(0);
	}
	
	/**
	 * Gets the current RPM using time-averaged readings from both motors.
	 * Returns the average of the two motors' smoothed velocity readings.
	 */
	public double getRPM() {
		return (averagedRightRPM + averagedLeftRPM) / 2.0;
	}
	
	public void setRPM(double rpm) {
		targetTPS = rpmToTicksPerSec(rpm);
	}
	
	/**
	 * Checks if the launcher is at the target speed.
	 * Uses time-averaged velocity readings and checks both motors separately
	 * to ensure both are within tolerance, preventing false positives from
	 * averaging out errors between motors.
	 */
	public boolean isAtSpeed() {
		if (state != LauncherState.ACTIVE) {
			return false;
		}
		
		double targetRPM = ticksPerSecToRPM(targetTPS);
		
		// Check both motors separately - both must be within tolerance
		double rightError = Math.abs(targetRPM - averagedRightRPM);
		double leftError = Math.abs(targetRPM - averagedLeftRPM);
		
		return rightError < MAX_SPEED_ERROR && leftError < MAX_SPEED_ERROR;
	}
	
	public enum LauncherState {
		IDLE,
		ACTIVE
	}
}