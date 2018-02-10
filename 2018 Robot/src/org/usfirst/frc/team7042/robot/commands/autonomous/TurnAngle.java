package org.usfirst.frc.team7042.robot.commands.autonomous;

import org.usfirst.frc.team7042.robot.PolyPrefs;
import org.usfirst.frc.team7042.robot.Robot;
import org.usfirst.frc.team7042.robot.RobotMap;
import org.usfirst.frc.team7042.robot.subsystems.DriveSystem;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.PIDSource;
import edu.wpi.first.wpilibj.PIDSourceType;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.command.Command;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

/**
 *
 */
public class TurnAngle extends Command {
	
	private Encoder left = RobotMap.leftEncoder;
	private Encoder right = RobotMap.rightEncoder;
	
	
	private PIDController angleController;
	
	private double angle;
	private AHRSInterface angleGetter;
	
	Timer onTargetTimer = new Timer();

	
	static class AHRSInterface implements PIDSource {
		private AHRS ahrs = RobotMap.ahrs;
		private PIDSourceType sourcetype = PIDSourceType.kDisplacement;
		
		public double getAngle() {
			return ahrs.getAngle();
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
				return ahrs.getRate();
			case kDisplacement:
			default:
				return ahrs.getAngle();
			}
		}
		
	}
	
	static class DriveTurn implements PIDOutput {
		
		@Override
		public void pidWrite(double output) {
			Robot.drive.arcadeDrive(0, -output, PolyPrefs.getAutoSpeed());
		}
		
	}

    public TurnAngle(double angle) {
    	requires(Robot.drive);
    	this.angle = angle;
    	angleGetter = new AHRSInterface();
    	angleController = new PIDController(
    			PolyPrefs.getTurnP(),
    			PolyPrefs.getTurnI(),
    			PolyPrefs.getTurnD(),
    			angleGetter,
    			new DriveTurn()
    			);
    	angleController.setAbsoluteTolerance(PolyPrefs.getTolerance());
    }

    // Called just before this Command runs the first time
    protected void initialize() {
    	angleController.setSetpoint(angleGetter.getAngle() + angle);
    	angleController.enable();
    	onTargetTimer.start();
    	System.out.println("Starting turn. Setpoint set to "+angleController.getSetpoint());
    	System.out.format("P:%.2f I:%.2f D:%.2f\n", angleController.getP(), angleController.getI(), angleController.getD());
    }
    
    // Called repeatedly when this Command is scheduled to run
    protected void execute() {
    	System.out.format("Current: %.2f, Setpoint: %.2f, Output: %.2f\n", angleGetter.getAngle(), angleController.getSetpoint(), angleController.get());
    }

    // Make this return true when this Command no longer needs to run execute()
    protected boolean isFinished() {
        if(angleController.onTarget()) {
        	if(onTargetTimer.hasPeriodPassed(PolyPrefs.getTargetTime())) {
        		return true;
        	}
        } else {
        	onTargetTimer.reset();
        }
        return false;
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
