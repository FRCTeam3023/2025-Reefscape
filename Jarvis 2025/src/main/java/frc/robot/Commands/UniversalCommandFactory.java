package frc.robot.Commands;

import java.util.Set;

import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.DeferredCommand;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.ParallelDeadlineGroup;
import edu.wpi.first.wpilibj2.command.RepeatCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import edu.wpi.first.wpilibj2.command.WaitUntilCommand;
import frc.robot.Constants;
import frc.robot.ControlPanel;
import frc.robot.Subsystems.Drivetrain;
import frc.robot.Subsystems.Elevator;
import frc.robot.Subsystems.EndEffector;
import frc.robot.Subsystems.Pivot;
import frc.robot.Util.Elastic;

public class UniversalCommandFactory {
    public static final Command reefCycle(Drivetrain drivetrain, Elevator elevator, Pivot pivot, EndEffector endEffector) {
        return new RepeatCommand(
            new SequentialCommandGroup(
                new InstantCommand(() -> ControlPanel.ReefCycle.setTravelState(endEffector.coralPresent())),
                new ParallelCommandGroup(
                    ControlPanel.ReefCycle.mutatingPathCommand(),
                    
                    new SequentialCommandGroup(
                        new WaitCommand(0.5),
                        new ParallelCommandGroup(
                            elevator.moveCommand(Constants.ElevatorConstants.MIN_ELEVATOR_EXTENSION),
                            UniversalCommandFactory.pivotAngleCommand(Constants.PivotConstants.TRAVEL_POSITION, pivot, endEffector)
                        ),
                        new ParallelCommandGroup(
                            new SequentialCommandGroup(
                                new WaitUntilCommand(() -> pivot.timeToReach(ControlPanel.ReefCycle.getAngle()) 
                                    > drivetrain.timeToReach(ControlPanel.ReefCycle.getLineupPath().getStartingHolonomicPose().get())),
                                new DeferredCommand(() -> UniversalCommandFactory.pivotAngleCommand(ControlPanel.ReefCycle.getAngle(), pivot, endEffector), Set.of(pivot, endEffector))
                            ),
                            new SequentialCommandGroup(
                                new WaitUntilCommand(() -> elevator.timeToReach(ControlPanel.ReefCycle.getHeight()) 
                                    > drivetrain.timeToReach(ControlPanel.ReefCycle.getLineupPath().getStartingHolonomicPose().get())),
                                new DeferredCommand(() -> elevator.moveCommand(ControlPanel.ReefCycle.getHeight()), Set.of(elevator))
                            )
                        )
                    )
                ),
                new DeferredCommand(() -> endEffector.moveCoralCommand(ControlPanel.ReefCycle.getTravelState()), Set.of(endEffector))
            )
        );
    }
    
    public static final Command pivotAngleCommand(Rotation2d angle, Pivot pivot, EndEffector endEffector) {
        return new ParallelDeadlineGroup(
            new WaitUntilCommand(() -> Math.abs(pivot.getAngle().minus(angle).getRadians()) < Constants.PivotConstants.POSITION_TOLERANCE.getRadians()),
            new InstantCommand(() -> {
                pivot.setAngle(angle);
                Elastic.selectTab(angle.getDegrees() == Constants.PivotConstants.TRAVEL_POSITION.getDegrees() ? "Teleoperated" : "End Effector View");
            })
        );
    }
}