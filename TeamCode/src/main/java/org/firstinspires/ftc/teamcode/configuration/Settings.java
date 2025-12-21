package org.firstinspires.ftc.teamcode.configuration;

import com.pedropathing.geometry.Pose;

import org.firstinspires.ftc.teamcode.CloseAuto;

public class Settings {
	public static class Rollers {
		public static double INTAKE_SPEED = 1;
		public static double INTAKE_STOP = 0;
		public static double INTAKE_OUT = -1;
		public static double MIDDLE_IN = 1;
		public static double MIDDLE_STOP = 0;
		public static double MIDDLE_OUT = -1;
		public static double END_IN = 1;
		public static double END_STOP = 0;
		public static double END_OUT = -1;
	}
	
	public static class Hardware {
		public static String PINPOINT = "pinpoint";
		public static String LAUNCHER_LEFT = "outtakeleft";
		public static String LAUNCHER_RIGHT = "outtakeright";
		public static String INTAKE = "intake";
		public static String MIDDLE = "middleWheels";
		public static String END = "finalWheels";
		
	}
	
	public static class Launcher {
		public static double CLOSE_VELOCITY = 2500;
		public static double FAR_VELOCITY = 3500;
		public static double MAX_SPEED_ERROR = 50;
		public static double TICKS_PER_REVOLUTION = 28;
	}
	
	public static class Auto {
		public static double ROLL_TIME = 3.0; // Time in seconds to run rollers
	}
	
	public static class Positions {
		public static Pose STORED_POSE = new Pose(72, 72, Math.toRadians(90));
		public static CloseAuto.Side SIDE = CloseAuto.Side.BLUE;
		
		public static class Starting {
			public static Pose close = new Pose(22, 125, Math.toRadians(315));
			public static Pose far = new Pose(55.5, 8, Math.toRadians(90));
		}
		
		public static class Launch {
			public static Pose close = new Pose(57., 92, Math.toRadians(315));
			public static Pose far = new Pose(55.5, 8, Math.toRadians(135));
		}
		
		public static class Preset2 {
			public static Pose PREP = new Pose(41, 60, Math.toRadians(180));
			public static Pose END = new Pose(12, 60, Math.toRadians(180));
		}
		
		public static class Preset3 {
			public static Pose PREP = new Pose(41, 84, Math.toRadians(180));
			public static Pose END = new Pose(12, 84, Math.toRadians(180));
		}
		
		public static class Park {
			public static Pose close = new Pose(56, 110, 0); // TODO: Set actual park position
		}
	}
}
