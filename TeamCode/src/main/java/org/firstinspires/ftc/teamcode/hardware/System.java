package org.firstinspires.ftc.teamcode.hardware;

import static org.firstinspires.ftc.teamcode.configuration.Settings.Hardware.END;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Hardware.INTAKE;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Hardware.LAUNCHER_LEFT;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Hardware.LAUNCHER_RIGHT;
import static org.firstinspires.ftc.teamcode.configuration.Settings.Hardware.MIDDLE;

import android.graphics.Color;

import com.pedropathing.follower.Follower;
import com.qualcomm.hardware.lynx.LynxModule;
import com.qualcomm.robotcore.hardware.CRServoImplEx;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.configuration.Constants;

import java.util.List;

public class System {
	private final List<LynxModule> allHubs;
	public Launcher launcher;
	public Rollers rollers;
	public Follower follower;
	
	
	public System(HardwareMap hwMap) {
		allHubs = hwMap.getAll(LynxModule.class);
		follower = Constants.createFollower(hwMap);
		launcher = new Launcher(
				hwMap.get(DcMotorEx.class, LAUNCHER_RIGHT),
				hwMap.get(DcMotorEx.class, LAUNCHER_LEFT));
		rollers = new Rollers(
				hwMap.get(DcMotor.class, INTAKE),
				hwMap.get(DcMotor.class, MIDDLE),
				hwMap.get(CRServoImplEx.class, END));
		setupHubs();
	}
	
	public void update() {
		updateHubs();
		follower.update();
		launcher.update();
	}
	
	public void setupHubs() {
		for (LynxModule hub : allHubs) {
			hub.setBulkCachingMode(LynxModule.BulkCachingMode.MANUAL);
			hub.setConstant(Color.MAGENTA);
		}
	}
	
	public void updateHubs() {
		for (LynxModule hub : allHubs) {
			hub.clearBulkCache();
		}
	}
}
