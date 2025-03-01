// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.robot.Commands.UniversalCommandFactory;
import frc.robot.Subsystems.ChassisVisionLocalizer;
import frc.robot.Subsystems.Climber;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.EndEffector;
import frc.robot.Subsystems.Pivot;
import frc.robot.Util.PIDDisplay;

public class RobotContainer {
  private static final Drivetrain drivetrain = new Drivetrain();
  private static final Elevator elevator = new Elevator();
  private static final Pivot pivot = new Pivot();
  private static final Climber climber = new Climber();
  private static final EndEffector endEffector = new EndEffector();
  SendableChooser<Command> autoChooser;

  double elevatorHeight;
  Rotation2d pivotAngle;

  public RobotContainer() {

    NamedCommands.registerCommand("Set Elevator Height L1", new InstantCommand(() -> elevatorHeight = Constants.ElevatorConstants.PRESET_HEIGHTS[0] + Constants.PivotConstants.LENGTH));
    NamedCommands.registerCommand("Set Elevator Height L2", new InstantCommand(() -> elevatorHeight = Constants.ElevatorConstants.PRESET_HEIGHTS[1] + Constants.PivotConstants.LENGTH));
    NamedCommands.registerCommand("Set Elevator Height L3", new InstantCommand(() -> elevatorHeight = Constants.ElevatorConstants.PRESET_HEIGHTS[2] - Math.sin(Constants.PivotConstants.END_MOUNT_ANGLE.getRadians()) * Constants.PivotConstants.LENGTH));
    NamedCommands.registerCommand("Set Elevator Height L4", new InstantCommand(() -> elevatorHeight = Constants.ElevatorConstants.PRESET_HEIGHTS[3] - Math.sin(Constants.PivotConstants.END_MOUNT_ANGLE.getRadians()) * Constants.PivotConstants.LENGTH));
    
    NamedCommands.registerCommand("Intake Position", new InstantCommand(() -> pivotAngle = Constants.PivotConstants.CORAL_INTAKE_ANGLE));
    NamedCommands.registerCommand("Default Posiiton", new InstantCommand(() -> pivotAngle = Rotation2d.fromDegrees(-90)));
    
    NamedCommands.registerCommand("Elevator Move", elevator.moveCommand(elevatorHeight));

    NamedCommands.registerCommand("Set Pivot L1", new InstantCommand(() -> pivotAngle = Constants.PivotConstants.CORAL_DEPOSIT_ANGLES[0]));
    NamedCommands.registerCommand("Set Pivot L2", new InstantCommand(() -> pivotAngle = Constants.PivotConstants.CORAL_DEPOSIT_ANGLES[1]));
    NamedCommands.registerCommand("Set Pivot L3", new InstantCommand(() -> pivotAngle = Constants.PivotConstants.CORAL_DEPOSIT_ANGLES[2]));
    NamedCommands.registerCommand("Set Pivot L4", new InstantCommand(() -> pivotAngle = Constants.PivotConstants.CORAL_DEPOSIT_ANGLES[3]));

    NamedCommands.registerCommand("Elevator Pivot", new InstantCommand(() -> UniversalCommandFactory.pivotAngleCommand(pivotAngle, true, pivot, endEffector)));

    NamedCommands.registerCommand("Deposit Coral", new SequentialCommandGroup(
    new InstantCommand(() -> endEffector.velocityCoralCommand(5)),
    new WaitCommand(1),
    new InstantCommand(() -> endEffector.velocityCoralCommand(0))
     )
    );
    NamedCommands.registerCommand("Deposit Coral", new SequentialCommandGroup(
    new InstantCommand(() -> endEffector.velocityCoralCommand(5)),
    new WaitCommand(1),
    new InstantCommand(() -> endEffector.velocityCoralCommand(0))
     )
    );
    



    NamedCommands.registerCommand("Intake Coral", new SequentialCommandGroup(
      new InstantCommand(() -> endEffector.velocityCoralCommand(-5)),
      new WaitCommand(1),
      new InstantCommand(() -> endEffector.velocityCoralCommand(0))
       )
      );

    new PIDDisplay();
    new ChassisVisionLocalizer();

    ControlPanel.configureBinding(drivetrain, elevator, pivot, endEffector, climber);
    configureAuto();

    PIDDisplay.Init();
  }

  public void onTeleopEnabled() { 
      // Notifications.GENERAL.send("Starting test sequence").andThen(new RepeatCommand(new SequentialCommandGroup(
      //   new WaitCommand(1),
      //   UniversalCommandFactory.reefCycle(drivetrain, elevator, pivot, endEffector)
      // ))).schedule();
  }

  private void configureAuto() {
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
      new ParallelCommandGroup(
        drivetrain.homeCommand()
        //elevator.homeCommand()
      ),
      AutoBuilder.buildAuto(autoChooser.getSelected().getName())
    );
  }
}
