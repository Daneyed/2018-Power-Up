package org.usfirst.frc.team7042.robot.commands;

import org.usfirst.frc.team7042.robot.Robot;
import org.usfirst.frc.team7042.robot.choosers.ControlChooser;
import org.usfirst.frc.team7042.robot.controllers.DriveController;
import org.usfirst.frc.team7042.robot.subsystems.DriveSystem;

import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TeleopNoPID extends Command {
	
	private DriveSystem drive = Robot.driveSystem;
	private ControlChooser chooser = Robot.controlChooser;
	
	private boolean reversed;
	
    public TeleopNoPID() {
        requires(drive);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    }

    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	DriveController controller = chooser.getSelected();
    	if(controller.getReverseDirection())
    		reversed = !reversed;
    	drive.arcadeDrive((reversed)?-controller.getMoveRequest():controller.getMoveRequest(), controller.getTurnRequest(), controller.getSpeedLimiter());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return false;
    }

    // Called once after isFinished returns true
    protected void end() {
    	drive.stop();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	end();
    }
}
