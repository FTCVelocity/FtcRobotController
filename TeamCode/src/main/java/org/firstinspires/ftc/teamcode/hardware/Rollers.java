package org.firstinspires.ftc.teamcode.hardware;

import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.END_IN;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.END_OUT;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.END_STOP;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.INTAKE_OUT;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.INTAKE_SPEED;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.INTAKE_STOP;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.MIDDLE_IN;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.MIDDLE_OUT;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Rollers.MIDDLE_STOP;

import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Feed rollers: intake (front roller), middle, end.
 *
 * PHASE 1: only the intake is on the robot. The intake is required.
 * `middle` and `end` may be null when they are missing from the robot configuration;
 * their methods then do nothing. Use the methods below instead of calling
 * middle.setPower(...) / end.setPower(...) directly, so a missing part never crashes.
 **/


public class Rollers {

	public final DcMotor intake;
	public final DcMotor middle;
	public final CRServoImplEx end;

	public Rollers(DcMotor intake, DcMotor middle, CRServoImplEx end) {
		this.intake = intake;
		this.middle = middle;
		this.end = end;

		if (intake != null) {
			intake.setDirection(DcMotorSimple.Direction.REVERSE);
			intake.setPower(INTAKE_STOP);
		}

		if (middle != null) middle.setPower(MIDDLE_STOP);
		if (end != null) end.setPower(END_STOP);
	}

	public boolean hasIntake() {
		return intake != null;
	}

	public boolean hasMiddle() {
		return middle != null;
	}

	public boolean hasEnd() {
		return end != null;
	}

	public void setIntakePower(double power) {
		if (intake != null) {
			intake.setPower(INTAKE_SPEED * power);
		}
	}

	public void setintakePower(double power) {
		setIntakePower(power);
	}

	public void intakeIn() {
		setIntakePower(1);
	}

	public void intakeStop() {
		setIntakePower(0);
	}

	public void intakeOut() {
		setIntakePower(-1);
	}


	public void middleIn() {
		if (middle != null) middle.setPower(MIDDLE_IN);
	}

	public void middleStop() {
		if (middle != null) middle.setPower(MIDDLE_STOP);
	}

	public void middleOut() {
		if (middle != null) middle.setPower(MIDDLE_OUT);
	}

	public double getMiddlePower() {
		return middle != null ? middle.getPower() : 0;
	}

	public void endIn() {
		if (end != null) end.setPower(END_IN);
	}

	public void endStop() {
		if (end != null) end.setPower(END_STOP);
	}

	public void endOut() {
		if (end != null) end.setPower(END_OUT);
	}

	public double getEndPower() {
		return end != null ? end.getPower() : 0;
	}
}
/*public class Rollers {
	public final DcMotor intake;
	public final DcMotor middle;      // null when not installed
	public final CRServoImplEx end;   // null when not installed

	public boolean hasIntake() {
		return intake != null;
	}

	public Rollers(
			DcMotor intake, DcMotor middle, CRServoImplEx end) {
		this.intake = intake;
		this.middle = middle;
		this.end = end;

		intake.setDirection(DcMotorSimple.Direction.REVERSE);
		intake.setPower(INTAKE_STOP);
		if (middle != null) middle.setPower(MIDDLE_STOP);
		if (end != null) end.setPower(END_STOP);
	}

	public boolean hasMiddle() {
		return middle != null;
	}

	public boolean hasEnd() {
		return end != null;
	}

	// ---------- intake ----------

	/**
	 * power: 1 = full in, -1 = full out, 0 = stop. Scaled by INTAKE_SPEED.

	public void setIntakePower(double power) {
		intake.setPower(INTAKE_SPEED * power);
	}

	public void intakeIn() {
		intake.setPower(INTAKE_SPEED);
	}

	public void intakeStop() {
		intake.setPower(INTAKE_STOP);
	}

	public void intakeOut() {
		intake.setPower(INTAKE_OUT);
	}

	// ---------- middle ----------

	public void middleIn() {
		if (middle != null) middle.setPower(MIDDLE_IN);
	}

	public void middleStop() {
		if (middle != null) middle.setPower(MIDDLE_STOP);
	}

	public void middleOut() {
		if (middle != null) middle.setPower(MIDDLE_OUT);
	}

	public double getMiddlePower() {
		return middle != null ? middle.getPower() : 0;
	}

	// ---------- end ----------

	public void endIn() {
		if (end != null) end.setPower(END_IN);
	}

	public void endStop() {
		if (end != null) end.setPower(END_STOP);
	}

	public void endOut() {
		if (end != null) end.setPower(END_OUT);
	}

	public double getEndPower() {
		return end != null ? end.getPower() : 0;
	}

	public void setintakePower(double v) {
		setIntakePower(v);
	}

}
*/
