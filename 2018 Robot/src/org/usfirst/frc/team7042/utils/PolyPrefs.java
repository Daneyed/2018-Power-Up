package org.usfirst.frc.team7042.utils;

import edu.wpi.first.wpilibj.Preferences;

public class PolyPrefs {
	
	private static Preferences prefs = Preferences.getInstance();
	
	private static final double AUTO_SPEED = 0.25;
	private static final double WHEEL_DIST = 0.61;
	private static final double ENC_TICKS = 700; // Number of encoder ticks per meter of travel
	private static final double MAX_MOVE_SPEED = 2; // Maximum velocity of the robot in meters/second
	private static final double MAX_TURN_SPEED = 4; // Maximum angular velocity of the robot in degrees/second
	private static final double TILT_RANGE = 2;
	
	
	
	private static void checkDouble(String key, double def) {
		if(!prefs.containsKey(key)) {
			prefs.putDouble(key, def);
		}
	}
	
	private static void checkInt(String key, int def) {
		if(!prefs.containsKey(key)) {
			prefs.putInt(key, def);
		}
	}
	public static double getAutoSpeed() {
		checkDouble("AUTO_SPEED", AUTO_SPEED);
		return prefs.getDouble("AUTO_SPEED", AUTO_SPEED);
	}
	public static double getWheelDist() {
		checkDouble("WHEEL_DIST", WHEEL_DIST);
		return prefs.getDouble("WHEEL_DIST", WHEEL_DIST);
	}
	public static double getEncTicks() {
		checkDouble("ENC_TICKS", ENC_TICKS);
		return prefs.getDouble("ENC_TICKS", ENC_TICKS);
	}
	public static double getMaxMoveSpeed() {
		checkDouble("MAX_MOVE_SPEED", MAX_MOVE_SPEED);
		return prefs.getDouble("MAX_MOVE_SPEED", MAX_MOVE_SPEED);
	}
	public static double getMaxTurnSpeed() {
		checkDouble("MAX_TURN_SPEED", MAX_TURN_SPEED);
		return prefs.getDouble("MAX_TURN_SPEED", MAX_TURN_SPEED);
	}
	public static double getTiltRange() {
		checkDouble("TILT_RANGE",TILT_RANGE);
		return prefs.getDouble("TILT_RANGE", TILT_RANGE);
	}

}
