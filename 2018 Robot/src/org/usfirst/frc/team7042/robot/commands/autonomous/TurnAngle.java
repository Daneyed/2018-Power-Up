package org.usfirst.frc.team7042.robot.commands.autonomous;

import org.usfirst.frc.team7042.robot.PolyPrefs;
import org.usfirst.frc.team7042.robot.Robot;
import org.usfirst.frc.team7042.robot.RobotMap;
import org.usfirst.frc.team7042.robot.subsystems.DriveSystem;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.command.Command;

/**
 *
 */
public class TurnAngle extends Command {
	
	private Encoder left = RobotMap.leftEncoder;
	private Encoder right = RobotMap.rightEncoder;
	
	
	private PIDController angleController;
	
	private double angle;
	private EncoderTurnOutput angleGetter;

	
	static class EncoderTurnOutput implements PIDSource {
		
		private Encoder left, right;
		private double lStart, rStart;
		
		private PIDSourceType sourcetype = PIDSourceType.kDisplacement;
		
		
		public EncoderTurnOutput(Encoder left, Encoder right) {
			this.left = left;
			this.right = right;
			lStart = left.getDistance();
			rStart = right.getDistance();
		}
		
		private double getAngle() { // Positive is clockwise. Returns drift angle, multiply by -1 to get correction
	    	return 180*((left.getDistance() - lStart) - (right.getDistance() - rStart) / (Math.PI * PolyPrefs.getWheelDist()));
	    }
		private double getAngleRate() {
			return ((left.getRate() - right.getRate())/PolyPrefs.getWheelDist()) * 360;
		}

		@Override
		public void setPIDSourceType(PIDSourceType pidSource) {
			sourcetype = pidSource;
		}

		@Override
		public PIDSourceType getPIDSourceType() {
			return sourcetype;
		}

		@Override
		public double pidGet() {
			switch(sourcetype) {
			case kRate:
				return getAngleRate();
			case kDisplacement:
			default:
				return getAngle();
			}
		}
		
	}
	
	static class DriveTurn implements PIDOutput {
		private DriveSystem output;
		
		public DriveTurn(DriveSystem output) {
			this.output = output;
		}
		@Override
		public void pidWrite(double output) {
			this.output.arcadeDrive(0, output, PolyPrefs.getAutoSpeed());
		}
		
	}

    public TurnAngle(double angle) {
    	requires(Robot.drive);
    	this.angle = angle;
    	angleGetter = new EncoderTurnOutput(left, right);
    	angleController = new PIDController(
    			0.2,
    			0,
    			0.01,
    			new EncoderTurnOutput(left, right),
    			new DriveTurn(Robot.drive)
    			);
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	angleController.setSetpoint(angleGetter.getAngle() + angle);
    	angleController.enable();
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        return angleController.onTarget();
    }

    // Called once after isFinished returns true
    protected void end() {
    	angleController.disable();
    }

    // Called when another command which requires one or more of the same
    // subsystems is scheduled to run
    protected void interrupted() {
    	angleController.disable();
    }
}
