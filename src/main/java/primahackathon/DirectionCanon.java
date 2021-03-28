package primahackathon;

import robocode.Robot;

public class DirectionCanon {
	
	public static void fixerCanon(Robot robot, double x, double y) {		
		double gunHeading = robot.getGunHeading();
		double robotX = robot.getX();
		double robotY = robot.getY();
		double angle = 0;
		
		if((x-robotX) > 0 && (y-robotY) > 0) { // le point à fixer est à droite et plus haut que le robot
			angle = Math.toDegrees(Math.atan((x-robotX)/(y-robotY)));
			if(gunHeading <= angle) {
				robot.turnGunRight(angle - gunHeading);
			} else {
				if(gunHeading - angle < 180) {
					robot.turnGunLeft(gunHeading - angle);
				} else {
					robot.turnGunRight(360 - gunHeading + angle);
				}
			}
		} else if((x-robotX) > 0 && (y-robotY) < 0) { // le point à fixer est à droite et plus bas que le robot
			angle = 180 - Math.toDegrees(Math.atan((x-robotX)/Math.abs((y-robotY))));
			if(gunHeading <= angle) {
				robot.turnGunRight(angle - gunHeading);
			} else {
				if(gunHeading - angle < 180) {
					robot.turnGunLeft(gunHeading - angle);
				} else {
					robot.turnGunRight(360 - gunHeading + angle);
				}
			}
		} else if ((x-robotX) < 0 && (y-robotY) < 0) { // le point à fixer est à gauche et plus bas que le robot
			angle = 180 + Math.toDegrees(Math.atan(Math.abs((x-robotX))/Math.abs((y-robotY))));
			if(gunHeading >= angle) {
				robot.turnGunLeft(gunHeading - angle);
			} else {
				if(angle - gunHeading < 180) {
					robot.turnGunRight(angle - gunHeading);
				} else {
					robot.turnGunLeft(360 - angle + gunHeading);
				}
			}
		} else { // le point à fixer est à gauche et plus haut que le robot
			angle = 360 - Math.toDegrees(Math.atan(Math.abs((x-robotX))/(y-robotY)));
			if(gunHeading >= angle) {
				robot.turnGunLeft(gunHeading - angle);
			} else {
				if(angle - gunHeading < 180) {
					robot.turnGunRight(angle - gunHeading);
				} else {
					robot.turnGunLeft(360 - angle + gunHeading);
				}
			}
		}
		
		robot.out.println("Gun Heading: " + gunHeading + ", angle de tir: " + angle + ", X: " + robot.getX() + ", Y: " + robot.getY());
	}
	
	public static void enleverFixeCanon(Robot robot) {
		robot.setAdjustGunForRobotTurn(false);
	}

}
