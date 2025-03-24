// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import java.util.Set;

import com.pathplanner.lib.auto.AutoBuilder;
import com.pathplanner.lib.auto.NamedCommands;
import com.pathplanner.lib.events.EventTrigger;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.DriverStation.Alliance;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.DeferredCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Commands.Notifications;
import frc.robot.Subsystems.ChassisVisionLocalizer;
import frc.robot.Subsystems.Climber;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.EndEffector;
import frc.robot.Subsystems.LED;
import frc.robot.Subsystems.Pivot;
import frc.robot.Util.PIDDisplay;

public class RobotContainer {
  private static final Drivetrain drivetrain = new Drivetrain();
  private static final Elevator elevator = new Elevator();
  private static final Pivot pivot = new Pivot();
  private static final Climber climber = new Climber();
  private static final EndEffector endEffector = new EndEffector();
  private static final LED led = new LED();
  SendableChooser<Command> autoChooser;

  int targetLevel;
  private static boolean isBlue = false;

  public RobotContainer() {
    NamedCommands.registerCommand("Set Target L1", new InstantCommand(() -> targetLevel = 0));
    NamedCommands.registerCommand("Set Target L2", new InstantCommand(() -> targetLevel = 1));
    NamedCommands.registerCommand("Set Target L3", new InstantCommand(() -> targetLevel = 2));
    NamedCommands.registerCommand("Set Target L4", new InstantCommand(() -> targetLevel = 3));
    
    new EventTrigger("Move Elevator").onTrue(new DeferredCommand(() -> elevator.moveCommand(Constants.ElevatorConstants.PRESET_HEIGHTS[targetLevel]), Set.of(elevator)));
    new EventTrigger("Move Pivot").onTrue(new DeferredCommand(() -> pivot.setAngleCommand(Constants.PivotConstants.CORAL_DEPOSIT_ANGLES[targetLevel]), Set.of(pivot)));
    new EventTrigger("Event Notification").onTrue(Notifications.PATHPLANNER_EVENT.send());

    new EventTrigger("Intake Coral").onTrue(endEffector.moveCoralCommand(true));
    new EventTrigger("Deposit Coral").onTrue(endEffector.moveCoralCommand(false));

    new PIDDisplay();
    new ChassisVisionLocalizer();

    ControlPanel.configureBinding(drivetrain, elevator, pivot, endEffector, climber);
    configureAuto();

    PIDDisplay.Init();
    
    Ultrasonic.setAutomaticMode(true);
  }

  public static boolean isBlue() {
    return isBlue;
  }

  public void onTeleopEnabled() {
    isBlue = DriverStation.getAlliance().get().equals(Alliance.Blue);
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
