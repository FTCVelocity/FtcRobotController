package org.firstinspires.ftc.teamcode.pedro;
//package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.Encoder;
import com.pedropathing.ftc.localization.constants.ThreeWheelIMUConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class Constants {


    /* ===================== PEDRO ===================== */


    /* ===================== FTC SDK ===================== */

        /* ===================== FOLLOWER ===================== */
        public static final FollowerConstants FOLLOWER_CONSTANTS =
                new FollowerConstants()
                        .mass(10.7); // Robot mass in kg

        public static final PathConstraints PATH_CONSTRAINTS =
                new PathConstraints(
                        0.99,   // max velocity
                        100,    // max acceleration
                        1,      // max deceleration
                        1       // max centripetal acceleration
                );

        /* ===================== DRIVETRAIN ===================== */
        public static final MecanumConstants DRIVE_CONSTANTS =
                new MecanumConstants()
                        .maxPower(1.0)
                        .leftFrontMotorName("frontleft")
                        .leftRearMotorName("backleft")
                        .rightFrontMotorName("frontright")
                        .rightRearMotorName("backright")
                        .leftFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
                        .leftRearMotorDirection(DcMotorSimple.Direction.REVERSE)
                        .rightFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
                        .rightRearMotorDirection(DcMotorSimple.Direction.FORWARD);

        /* ===================== LOCALIZER ===================== */
        /* ===================== LOCALIZER ===================== */
        public static final ThreeWheelIMUConstants LOCALIZER_CONSTANTS =
                new ThreeWheelIMUConstants()

                        // Encoder scaling (unchanged)
                        .forwardTicksToInches(0.001054)
                        .strafeTicksToInches(0.001054)
                        .turnTicksToInches(0.001054)

                        // Pod offsets (in inches) ✅ UPDATED
                        .leftPodY(7.5)      // half of 15"
                        .rightPodY(1)    // half of 15"
                        .strafePodX(5.0)    // half of 17"

                        // encoder hardware map names
                        .leftEncoder_HardwareMapName("frontleft")
                        .rightEncoder_HardwareMapName("backright")
                        .strafeEncoder_HardwareMapName("frontright")

                        // Encoder directions
                        .leftEncoderDirection(Encoder.REVERSE)
                        .rightEncoderDirection(Encoder.FORWARD)
                        .strafeEncoderDirection(Encoder.REVERSE)

                        // IMU configuration
                        .IMU_HardwareMapName("imu")
                        .IMU_Orientation(
                                new RevHubOrientationOnRobot(
                                        RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                        RevHubOrientationOnRobot.UsbFacingDirection.LEFT
                                )
                        );


    /* ===================== FOLLOWER BUILDER ===================== */
        public static Follower createFollower(HardwareMap hardwareMap) {
            return new FollowerBuilder(FOLLOWER_CONSTANTS, hardwareMap)
                    .pathConstraints(PATH_CONSTRAINTS)
                    .mecanumDrivetrain(DRIVE_CONSTANTS)
                    .threeWheelIMULocalizer(LOCALIZER_CONSTANTS)
                    .build();
        }
    }


