// Copyright (c) FIRST and other WPILib contributors.
// Open Source Software; you can modify and/or share it under the terms of
// the WPILib BSD license file in the root directory of this project.

package frc.robot.Commands;

import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj2.command.Command;
import frc.robot.Constants;
import frc.robot.RobotContainer;
import frc.robot.Subsystems.Drivetrain;

public class JoystickDrive extends Command {
  Drivetrain drivetrain;
  Joystick controller;

  public JoystickDrive(Drivetrain drivetrain, Joystick controller) {
    this.drivetrain = drivetrain;
    this.controller = controller;
    addRequirements(drivetrain);
  }

  // Called when the command is initially scheduled.
  @Override
  public void initialize() {}

  // Called every time the scheduler runs while the command is scheduled.
  @Override
  public void execute() {
    double xInputRight = -applyDeadband(controller.getRawAxis(4), Constants.DrivetrainConstants.DRIVE_TOLERANCE_PERCENT);
    double yInputRight = -applyDeadband(controller.getRawAxis(5), Constants.DrivetrainConstants.DRIVE_TOLERANCE_PERCENT);

    xInputRight = Math.abs(xInputRight) * xInputRight;
    yInputRight = Math.abs(yInputRight) * yInputRight;

    double xSpeed = yInputRight * Constants.DrivetrainConstants.MAX_DRIVE_SPEED * (RobotContainer.isBlue() ? 1 : -1);
    double ySpeed = xInputRight * Constants.DrivetrainConstants.MAX_DRIVE_SPEED * (RobotContainer.isBlue() ? 1 : -1);

    double xInputLeft = applyDeadband(controller.getRawAxis(0), Constants.DrivetrainConstants.DRIVE_TOLERANCE_PERCENT);
    double rotationSpeed = -xInputLeft * Math.abs(xInputLeft) * Constants.DrivetrainConstants.MAX_ANGULAR_SPEED;

    drivetrain.drive(new ChassisSpeeds(xSpeed, ySpeed, rotationSpeed), true);
  }

  // Called once the command ends or is interrupted.
  @Override
  public void end(boolean interrupted) {
    drivetrain.drive(new ChassisSpeeds(), false);
  }

  // Returns true when the command should end.
  @Override
  public boolean isFinished() {
    return false;
  }

  private double applyDeadband(double joystickValue, double tolerance){
    if(Math.abs(joystickValue) > tolerance){
      return Math.signum(joystickValue) * (Math.abs(joystickValue) - tolerance)/(1-tolerance);
    } 
    return 0;
  }
}
