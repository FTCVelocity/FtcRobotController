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

public class Rollers {
	public final DcMotor intake;
	public final CRServoImplEx middle;
	public final CRServoImplEx end;
	
	public Rollers(
			DcMotor intake, CRServoImplEx middle, CRServoImplEx end) {
		this.intake = intake;
		this.middle = middle;
		this.end = end;
		
		intake.setDirection(DcMotorSimple.Direction.REVERSE);
		intake.setPower(INTAKE_STOP);
		middle.setPower(MIDDLE_STOP);
		end.setPower(END_STOP);
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
	
	public void middleIn() {
		middle.setPower(MIDDLE_IN);
	}
	
	public void middleStop() {
		middle.setPower(MIDDLE_STOP);
	}
	
	public void middleOut() {
		middle.setPower(MIDDLE_OUT);
	}
	
	public void endIn() {
		end.setPower(END_IN);
	}
	
	public void endStop() {
		end.setPower(END_STOP);
	}
	
	public void endOut() {
		end.setPower(END_OUT);
	}
}