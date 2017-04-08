package org.usfirst.frc.team1002.robot;

import org.usfirst.frc.team1002.system.DriveSystem;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class Auto {
	public enum Side {
		LEFT,
		RIGHT,
		;
	}
	
	public static void autoDriveFwd(double speed, double angle, double time, double targetDistance) {
		double endTime = Timer.getFPGATimestamp() + time;
		double distance = 0;
		while (Timer.getFPGATimestamp() < endTime && distance >= targetDistance) {
			DriveSystem.robotDrive.mecanumDrive_Polar(speed, angle, 0);
			SmartDashboard.putNumber("Encoder", distance = DriveSystem.encoder.getDistance());
			Timer.delay(0.05);
		}
		DriveSystem.robotDrive.stopMotor();
	}

	public static void autoDriveFwdGyro(double speed, double angle, double time, double targetDistance) {
		double endTime = Timer.getFPGATimestamp() + time;
		double distance = 0;
		while (Timer.getFPGATimestamp() < endTime && distance >= targetDistance) {
			DriveSystem.robotDrive.mecanumDrive_Cartesian(Math.sin(angle) * speed, Math.cos(angle) * speed, 0, Robot.gyro.getAngle());
			SmartDashboard.putNumber("Encoder", distance = DriveSystem.encoder.getDistance());
			Timer.delay(0.05);
		}
		DriveSystem.robotDrive.stopMotor();
	}
	
	public static void autoDriveFwdGyroVision(double speed, double angle, double time, double targetDistance) {
		double endTime = Timer.getFPGATimestamp() + time;
		double distance = 0;
		while (Timer.getFPGATimestamp() < endTime && distance >= targetDistance) {
			if (Robot.camsys.getTargetPlacement()) {
				DriveSystem.robotDrive.mecanumDrive_Polar(speed, angle, Math.sin(Math.toDegrees(Robot.camsys.external_xAngle)));
			} else {
				DriveSystem.robotDrive.mecanumDrive_Cartesian(Math.sin(angle) * speed, Math.cos(angle) * speed, 0, Robot.gyro.getAngle());
			}
			SmartDashboard.putNumber("Encoder", distance = DriveSystem.encoder.getDistance());
			Timer.delay(0.05);
		}
		DriveSystem.robotDrive.stopMotor();
	}
	
	private static void turn(double angle, double time, Side side){
		double endTime = Timer.getFPGATimestamp() + time;
		switch (side) {
			case LEFT:
				while (Timer.getFPGATimestamp() < endTime && Robot.gyro.getAngle() <= angle){
					DriveSystem.robotDrive.mecanumDrive_Polar(0, 0, 0.5);
					Timer.delay(0.05);
				}
			case RIGHT:
				while (Timer.getFPGATimestamp() < endTime + time && Robot.gyro.getAngle() >= angle){
					DriveSystem.robotDrive.mecanumDrive_Polar(0, 0, -0.5);
					Timer.delay(0.05);
				}
			default:
				break;
		}
		DriveSystem.robotDrive.stopMotor();
	}

	public static void sidePegAuto(Side side, double speed, double degrees, double forwardTime1, double forwardDistance1,double angle, double turnTime, double forwardTime2, double forwardDistance2){
		autoDriveFwd(speed, degrees, forwardTime1, forwardDistance1);
		turn(angle, turnTime, side);
		autoDriveFwd(speed, degrees, forwardTime2, forwardDistance2);
	}
}
