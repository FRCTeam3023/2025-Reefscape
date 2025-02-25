// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot;

import com.pathplanner.lib.auto.AutoBuilder;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Elevator;
import frc.robot.Util.PIDDisplay;

public class RobotContainer {
  private static final Drivetrain drivetrain = new Drivetrain();
  private static final Elevator elevator = new Elevator();
  // private static final Pivot pivot = new Pivot();
  // private static final EndEffector endEffector = new EndEffector();
  SendableChooser<Command> autoChooser;

  public RobotContainer() {
    new PIDDisplay();
    //new ChassisVisionLocalizer();

    ControlPanel.configureBinding(drivetrain, elevator/*, pivot, endEffector*/);
    configureAuto();

    PIDDisplay.Init();
  }

  private void configureAuto() {
    autoChooser = AutoBuilder.buildAutoChooser();
    SmartDashboard.putData("Auto Chooser", autoChooser);
  }

  public Command getAutonomousCommand() {
    return new SequentialCommandGroup(
      drivetrain.homeCommand(),
      autoChooser.getSelected()
    );
  }
}
