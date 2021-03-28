package primahackathon;

import robocode.AdvancedRobot;

public class RobotUtils {

	public static final double NB_DEGREES = 360;
	public static final double DEGREES_GUN_PER_TURN = 20;
	public static final int RIGHT = 0;
	public static boolean isMovingForward = true;
	
	//The angle position is the angle relative to body of robot
    public static void placeGunToAngle(double anglePosition, AdvancedRobot robot) {
    	double gunHeading = robot.getGunHeading();
    	//get angle relative to 360
    	double anglePositionLeft = anglePosition;
    	if(anglePosition<gunHeading) {
    		anglePositionLeft+=NB_DEGREES;
    	}
    	//always positive
    	double diffLeft = anglePositionLeft-gunHeading;
    	
    	double anglePositionRight = gunHeading;
    	if(anglePosition>gunHeading) {
    		anglePositionRight += NB_DEGREES;
    	}
    	double diffRight = anglePositionRight-anglePosition;
    	
    	if(diffLeft<diffRight){
    		robot.turnGunRight(diffRight);
    	} else {
    		robot.turnGunLeft(diffLeft);
    	}
    	
    }
    
    public static void placeGunToAngleV2(double anglePosition, AdvancedRobot robot) {
    	double gunHeading = robot.getGunHeading();
    	double angle = gunHeading - anglePosition;
    	
    	if(angle > 0) {
    		if(angle < 180) {
    			robot.turnGunLeft(angle);
    		} else {
    			robot.turnGunRight(360 - gunHeading + anglePosition);
    		}
    	} else {
    		if(Math.abs(angle) < 180) {
    			robot.turnGunRight(Math.abs(angle));
    		} else {
    			robot.turnGunLeft(360 - anglePosition + gunHeading);
    		}
    	}      	
    }
    
    public static void moveCurve(double valAhead, double turnDegrees, AdvancedRobot robot, boolean turnRight ) {
    	
    	if(valAhead > robot.getBattleFieldWidth()*0.8) { // Si le valAhead est vraiment trop grand, je le met à la moitié du width
    		valAhead = robot.getBattleFieldWidth()/2;
    	}

    	double robotHeading = robot.getHeading() + turnDegrees;
    	
    	double distancePossible = distanceMax(robotHeading, robot.getX(), robot.getY(), 
    			robot.getBattleFieldWidth(), robot.getBattleFieldHeight(), robot);
    	//robot.out.print("Distance possible: " + distancePossible);
    	
    		if (valAhead < distancePossible) { //On pourrait curver à droit ou à gauche, j'ai choisi droite	
    			robot.out.println("Move curve right " + valAhead + ", turn degrees: " + turnDegrees + ", heading: " + robot.getHeading());
    			moveCurveRight(valAhead, turnDegrees, robot);
    		} else { // valAhead > distancePossible gauche et droit
    			turnDegrees += 90;
    			robot.out.println("dans le else");
    			moveCurve(valAhead, turnDegrees, robot, turnRight); 	
    		}
    }
    
    
    public static void moveCurveRight(double valAhead, double turnDegrees, AdvancedRobot robot) {
    	//am I going to hit a wall? I have hypothenus - check the O and A
    	robot.setAhead(valAhead);	
    	robot.setTurnRight(turnDegrees);
    	robot.execute();
    }
    
    public static void moveCurveLeft(double valAhead, double turnDegrees, AdvancedRobot robot) {
    	robot.setAhead(valAhead);
    	robot.setTurnLeft(turnDegrees);
    	robot.execute();
    	isMovingForward = true;
    }
    
    public static void moveBackCurveLeft(double valBack, double turnDegrees, AdvancedRobot robot) {
    	
    	
    	robot.setBack(valBack);
    	robot.setTurnLeft(turnDegrees);
    	robot.execute();
    	isMovingForward = false;
    }

	public static double distanceMax(double robotHeading, double robotX, double robotY, double battleFieldWidth,
			double battleFieldHeight, AdvancedRobot robot) {
		double distanceMax = 0;
    	double a = 0;
    	double b = 0;
		
		if(robotHeading <= 90) {
			distanceMax = (battleFieldHeight - robotY) / Math.cos(robotHeading);
		} else if (robotHeading <= 180) {
			distanceMax = (robotY - battleFieldHeight)/ Math.cos(180 - robotHeading);
		} else if (robotHeading <= 270) {
			distanceMax = (robotY - battleFieldHeight)/ Math.cos(robotHeading - 180);
		} else {
			distanceMax = (battleFieldHeight - robotY) / Math.cos(360 - robotHeading);
		}
		//robot.out.println("Distance max: " + distanceMax);
		return distanceMax;
		
	}
	
    public static void doRandomMove(AdvancedRobot robot) {
    	double valToMove = Math.random()*Math.min(robot.getBattleFieldHeight(), robot.getBattleFieldWidth())-360;
    	if(valToMove > 0) {
    		robot.setAhead(valToMove);
    		setRightOrLeftMvmt(robot);
    	} else {
    		robot.setBack(valToMove);
    		setRightOrLeftMvmt(robot);
    	}
    	
    	robot.execute();
    }
    
    private static void setRightOrLeftMvmt(AdvancedRobot robot) {
    	int rightOrLeft = (int)(Math.random()*2);
		double degreesToMove = Math.random()*180;
		if(rightOrLeft == RIGHT) {
			robot.setTurnRight(degreesToMove);
		} else {
			robot.setTurnLeft(degreesToMove);
		}
    }
    
    public static void moveAhead(double value, AdvancedRobot robot) {
    	isMovingForward = true;
    	robot.ahead(value);
    }
    
    public static void moveBack(double value, AdvancedRobot robot) {
    	isMovingForward = false;
    	robot.back(value);
    }
}
