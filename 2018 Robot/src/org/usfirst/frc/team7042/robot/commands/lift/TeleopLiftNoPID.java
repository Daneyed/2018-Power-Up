package org.usfirst.frc.team7042.robot.commands.lift;

import org.usfirst.frc.team7042.robot.Robot;
import org.usfirst.frc.team7042.robot.subsystems.Lift;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TeleopLiftNoPID extends Command {

	private Lift lift = Robot.lift;
	
    public TeleopLiftNoPID() {
        requires(lift);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	lift.setSpeedNoLimit(Robot.controlChooser.getSelected().getLiftSpeed());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	lift.setSpeed(0);
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    }
}
