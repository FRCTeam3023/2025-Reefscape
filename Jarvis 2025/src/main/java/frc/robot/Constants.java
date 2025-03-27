// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.path.PathConstraints;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import frc.robot.Util.Gains;

/** Add your docs here. */
public class Constants {
    public static boolean allowTabSwitching = true;
    public static int LED_LENGTH = 10;

    public enum CAN_DEVICES {
        // These are assigned algorithmically in Drivetrain; do not use for anything else
        // DRIVE_FRONT_LEFT(1),
        // DRIVE_FRONT_RIGHT(2),
        // DRIVE_BACK_LEFT(3),
        // DRIVE_BACK_RIGHT(4),
        // TURN_FRONT_LEFT(5),
        // TURN_FRONT_RIGHT(6),
        // TURN_BACK_LEFT(7),
        // TURN_BACK_RIGHT(8),

        ROBORIO(0),
        ELEVATOR_MOTOR(9),
        END_EFFECTOR(10),
        PIVOT(11),
        CLIMBER(12),
        PIGEON_2(20);

        public int id;
        private CAN_DEVICES(int id) {
            this.id = id;
        }
    }

    public static class GAINS {
        public static Gains DRIVE = new Gains(5, 0, 0.15, 2.65, 12);
        public static Gains TURN = new Gains(.6, 1);
        public static Gains ELEVATOR = new Gains(20, 0, 0, 0, 0, 12);
        public static Gains END_EFFECTOR = new Gains(0.005, 0, 0, 0, 12);
        public static Gains PIVOT = new Gains(3, 0, 0, 0, 12);
        public static Gains CLIMBER = new Gains(100, 0, 0, 0, 12);
    }

    public static class DrivetrainConstants {
        public static final double WHEEL_BASE = Units.inchesToMeters(22.5);
        public static final double MAX_DRIVE_SPEED = 5;
        public static final double MAX_ANGULAR_SPEED = 3;
        public static final double ALIGN_CONTROL_MULTIPLIER = 0.2;
        public static final double DRIVE_TOLERANCE_PERCENT = 0.05;
    }

    public static class ModuleConstants{
        /** Overall max speed of the module in m/s */
        public static final double MAX_SPEED = DrivetrainConstants.MAX_DRIVE_SPEED;

        /** rotational offset in radians of modules during homing */
        public static final double[] MODULE_OFFSETS = new double[] {
            -Math.PI/4,
            Math.PI/4,
            Math.PI/4,
            -Math.PI/4
        };

        /** Gear Ratio of the drive motor */
        public static final double DRIVE_GEARING = 8;

        /**Gear ratio of the turning motor */
        public static final double TURN_GEARING = 2.89 * 2.89 * 6;

        /**Diameter of the billet wheel */
        public static final double WHEEL_DIA = Units.inchesToMeters(3.875);

        public static final Translation2d[] MODULE_POSITIONS = {
            new Translation2d(DrivetrainConstants.WHEEL_BASE/2 , DrivetrainConstants.WHEEL_BASE/2),
            new Translation2d(DrivetrainConstants.WHEEL_BASE/2 , -DrivetrainConstants.WHEEL_BASE/2),
            new Translation2d(-DrivetrainConstants.WHEEL_BASE/2 , DrivetrainConstants.WHEEL_BASE/2),
            new Translation2d(-DrivetrainConstants.WHEEL_BASE/2 , -DrivetrainConstants.WHEEL_BASE/2)
        };
    }

    public static class ElevatorConstants{
        public static final double TOP_LIMIT_POSITION = Units.inchesToMeters(65.25);
        public static final double BOTTOM_LIMIT_POSITION = Units.inchesToMeters(42);

        public static final double MAX_ELEVATOR_EXTENSION = TOP_LIMIT_POSITION;
        public static final double MIN_ELEVATOR_EXTENSION = BOTTOM_LIMIT_POSITION;

        //Diameter of the elevator drive sprocket in meters
        public static final double SPROCKET_DIA = Units.inchesToMeters(1.5);
        public static final double SPROKET_CIRCUMFERENCE = Math.PI * SPROCKET_DIA;

        //Gear ratio of the elevator drive motor
        public static final double GEARING = 9 * 3;

        //Tolerence for elevator height
        public static final double SETPOINT_RANGE = .25;
        
        public static final double LINEAR_SPEED = 0.24;

        public static final double[] PRESET_HEIGHTS = {
            1.07,
            1.20,
            1.05,
            TOP_LIMIT_POSITION
        };

        public static final double CORAL_INTAKE_HEIGHT = 1.13;
    }

    public static class PivotConstants {
        public static final double GEARING = 90 * 5 / 1.5;
        /**The angle between the arm and a loaded coral, with positive x pointing radially outwards*/
        public static final Rotation2d END_MOUNT_ANGLE = Rotation2d.fromDegrees(0);
        public static final Rotation2d POSITION_TOLERANCE = Rotation2d.fromDegrees(15);
        public static final Rotation2d CORAL_DEPOSIT_ANGLES[] = {
            Rotation2d.fromDegrees(-125),
            Rotation2d.fromDegrees(-115),
            Rotation2d.fromDegrees(131),
            Rotation2d.fromDegrees(131),
        };
        public static final Rotation2d CORAL_INTAKE_ANGLE = Rotation2d.fromDegrees(-48);
        public static final Rotation2d ANGULAR_SPEED = Rotation2d.fromDegrees(45);
        public static final Rotation2d TRAVEL_POSITION = Rotation2d.fromDegrees(-90);
        public static final Rotation2d CLIMB_POSITION = Rotation2d.fromDegrees(-110);
        /**Length from pivot axis to center of coral when loaded*/
        public static final double LENGTH = 1;
    }

    public static class EndEffectorConstants {
        public static final double CORAL_PROTRUSION_LENGTH = 0.5;
        public static final Rotation2d ACCEPTABLE_SCORING_RANGE = Rotation2d.fromDegrees(45);
        public static final double GEARING = 5 * 3 * 3 * 1.5;
        public static final double GEARING_TO_PIVOT = 42.0 / 16.0;
        public static final double INTAKE_RPM = 100;
        public static final double OUTAKE_RPM = -200;
    }

    public static class ClimberConstants {
        public static final Rotation2d MAX_ROTATION = Rotation2d.fromDegrees(170);
        public static final Rotation2d MIN_ROTATION = Rotation2d.fromDegrees(10);
        public static final Rotation2d SETPOINT_RANGE = Rotation2d.fromDegrees(1);
        public static final double GEARING = 9 * 5 * 4 * 4;
    }

    public static class PhotonConstants {
        public final static double CAMS_PITCH = Math.toRadians(-31);

        public static final Transform3d[] ROBOT_TO_CAMERAS = {
            new Transform3d(
                new Translation3d(0.36131825544983764, 0.2828967240769298, 0.24398308011415953), 
                new Rotation3d(0.04297364217214592, -0.651388421457077, 0.05937144813225164)
            ),
            new Transform3d(
                new Translation3d(0.37158888338880347, -0.35179733777755445, 0.1897655874189626), 
                new Rotation3d(-0.013738996804999229, -0.4724985726508548, -0.045998497869259296)
            ),
            new Transform3d(
                new Translation3d(-0.32640013519352523, 0.3448957075421277, 0.22699882408163932), 
                new Rotation3d(0.019564276550408863, -0.5619005036195722, 3.0486914317085922)
            ),
            new Transform3d(
                new Translation3d(-0.36698280706318676, -0.23131990452158516, 0.2556158176288365), 
                new Rotation3d(0.06141322240459643, -0.6644324625969564, -3.0962299059608647)
            )
        };

        public static final Transform3d[] CAMERAS_TO_ROBOT = {
            ROBOT_TO_CAMERAS[0].inverse(),
            ROBOT_TO_CAMERAS[1].inverse(),
            ROBOT_TO_CAMERAS[2].inverse(),
            ROBOT_TO_CAMERAS[3].inverse()
        };

        public static final AprilTagFieldLayout APRIL_TAG_FIELD_LAYOUT = AprilTagFieldLayout.loadField(AprilTagFields.kDefaultField);

        public static final Pose3d ROBOT_TO_CALIBRATION = new Pose3d(
            new Translation3d(
                Units.inchesToMeters(-44.75), 
                Units.inchesToMeters(0.05),
                Units.inchesToMeters(35.875)
            ),
            new Rotation3d(0, 0, 0)
        );
        public static final int NUM_CALIBRATION_ENTRIES = 100;
    }

    public static class NavigationConstants {
        public static final PathConstraints PATHING_CONSTRAINTS = new PathConstraints(
            DrivetrainConstants.MAX_DRIVE_SPEED * 3/5,
            3.5,
            Math.toRadians(540),
            Math.toRadians(720),
            12
        );

        public static final double DESTINATION_TOLERANCE = 0.01;
        public static final double SECONDARY_DESTINATION_TOLERANCE = 0.005;

        public static final String[] REEF_LOCATIONS = {
            "1L Lineup",
            "1R Lineup",
            "2L Lineup",
            "2R Lineup",
            "3L Lineup",
            "3R Lineup",
            "4L Lineup",
            "4R Lineup",
            "5L Lineup",
            "5R Lineup",
            "6L Lineup",
            "6R Lineup",
        };
        public static final String CORAL_STATIONS[] = {
            "SL Lineup",
            "SR Lineup"
        };

        public static final double OPERATION_RADIUS = 0.5;
    }
}