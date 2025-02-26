package frc.robot.Util;

import java.util.Set;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.DeferredCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.RunCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.ControlPanel;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.EndEffector;
import frc.robot.Subsystems.Pivot;

public class UniversalCommandFactory {
    public static final Command reefCycle(Drivetrain drivetrain, Elevator elevator, Pivot pivot, EndEffector endEffector) {
        return new RepeatCommand(
            new SequentialCommandGroup(
                new InstantCommand(() -> ControlPanel.ReefCycle.setTravelState(endEffector.coralPresent())),
                new ParallelCommandGroup(
                    new DeferredCommand(() -> drivetrain.pathingCommand(ControlPanel.ReefCycle.getLocation(), 0), Set.of(drivetrain))
                    .withDeadline(new WaitCommand(10)),
                    
                    new WaitUntilCommand(() ->
                        drivetrain.getPose().getTranslation().getDistance(ControlPanel.ReefCycle.getPreviousLocation().getTranslation())
                        > Constants.NavigationConstants.OPERATION_RADIUS
                    ).andThen(
                        new ParallelCommandGroup(
                            elevator.moveCommand(Constants.ElevatorConstants.MIN_ELEVATOR_EXTENSION),
                            UniversalCommandFactory.pivotAngleCommand(Rotation2d.fromDegrees(-90), false, pivot, endEffector)
                        )
                    ),

                    //Potentially add time to traverse operation radius?
                    // new WaitUntilCommand(() -> pivot.timeToReach(ControlPanel.ReefCycle.getAngle()) < drivetrain.timeToReach(ControlPanel.ReefCycle.getLocation()))
                    // .andThen(new DeferredCommand(() -> UniversalCommandFactory.pivotAngleCommand(ControlPanel.ReefCycle.getAngle(), true, pivot, endEffector), Set.of(pivot, endEffector))),
                    new DeferredCommand(() -> UniversalCommandFactory.pivotAngleCommand(ControlPanel.ReefCycle.getAngle(), true, pivot, endEffector), Set.of(pivot, endEffector)),

                    // new WaitUntilCommand(() -> elevator.timeToReach(ControlPanel.ReefCycle.getHeight()) < drivetrain.timeToReach(ControlPanel.ReefCycle.getLocation()))
                    // .andThen(new DeferredCommand(() -> elevator.moveCommand(ControlPanel.ReefCycle.getHeight()), Set.of(elevator)))
                    new DeferredCommand(() -> elevator.moveCommand(ControlPanel.ReefCycle.getHeight()), Set.of(elevator))
                ),
                new DeferredCommand(() -> endEffector.moveCoralCommand(ControlPanel.ReefCycle.getTravelState()), Set.of(endEffector))
            )
        );
    }
    
    public static final Command pivotAngleCommand(Rotation2d angle, boolean coralAngle, Pivot pivot, EndEffector endEffector) {
        return new ParallelDeadlineGroup(
            new WaitUntilCommand(() -> Math.abs(pivot.getAngle().minus(angle).getRadians()) < Constants.PivotConstants.POSITION_TOLERANCE.getRadians()),
            new InstantCommand(coralAngle ? () -> pivot.setEndCoralAngle(angle) : () -> pivot.setAngle(angle))
            //new RunCommand(() -> endEffector.setRPM(pivot.getSpeed() / Constants.EndEffectorConstants.GEARING_TO_PIVOT))
        );
    }
}