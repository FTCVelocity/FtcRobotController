package org.firstinspires.ftc.teamcode.pedro;
//package org.firstinspires.ftc.teamcode.pedroPathing;

import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

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
    public static PinpointConstants pinpointConstants = new PinpointConstants()
            .forwardPodY(-3.5)
            .strafePodX(1.25)
            .distanceUnit(DistanceUnit.INCH)
            .hardwareMapName("pinpoint")
            .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
            .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED)
            .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    /* ===================== FOLLOWER BUILDER ===================== */
    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(FOLLOWER_CONSTANTS, hardwareMap)
                .pathConstraints(PATH_CONSTRAINTS)
                .mecanumDrivetrain(DRIVE_CONSTANTS)
                .pinpointLocalizer(pinpointConstants)
                .build();
    }
}


