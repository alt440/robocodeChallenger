package primahackathon;

import robocode.AdvancedRobot;

public class TrigoUtils {

	public static double NB_DEGREES = 360;
	
	public static double[] getOAndA(double hypothenus, double angle) {
		double[] opposite_adjacent = new double[2];
		opposite_adjacent[0] = hypothenus* Math.sin((angle*Math.PI)/180);
		opposite_adjacent[1] = hypothenus* Math.cos((angle*Math.PI)/180);
		return opposite_adjacent;
	}
	
	public static double getDistanceBtwPoints(double startX, double startY,
			double endX, double endY) {
		double diffX = Math.abs(startX-endX);
		double diffY = Math.abs(startY-endY);
		return Math.sqrt(Math.pow(diffX, 2)+Math.pow(diffY, 2));
	}
	
	public static void sanitizeAngleReceivedGun(double angleTurn, AdvancedRobot robot) {
		if(angleTurn<0) {
        	double angleLeft = Math.abs(angleTurn);
        	if(angleLeft>180) {
        		robot.setTurnGunRight(NB_DEGREES-angleLeft);
        	} else {
        		robot.setTurnGunLeft(Math.abs(angleTurn));
        	}
        } else {
        	if(angleTurn>180) {
        		robot.setTurnGunLeft(NB_DEGREES-angleTurn);
        	} else {
        		robot.setTurnGunRight(angleTurn);
        	}
        }
	}
	
	public static void sanitizeAngleReceivedBody(double angleTurn, AdvancedRobot robot) {
		if(angleTurn<0) {
        	double angleLeft = Math.abs(angleTurn);
        	if(angleLeft>180) {
        		robot.setTurnRight(NB_DEGREES-angleLeft);
        	} else {
        		robot.setTurnLeft(Math.abs(angleTurn));
        	}
        } else {
        	if(angleTurn>180) {
        		robot.setTurnLeft(NB_DEGREES-angleTurn);
        	} else {
        		robot.setTurnRight(angleTurn);
        	}
        }
	}
}
