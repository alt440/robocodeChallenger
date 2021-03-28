package primahackathon;

import robocode.HitByBulletEvent;
import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.BulletHitBulletEvent;
import robocode.BulletHitEvent;
import robocode.BulletMissedEvent;
import robocode.HitWallEvent;
import robocode.Robot;
import robocode.RobotStatus;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.StatusEvent;
import robocode.util.Utils;

import java.awt.*;
import java.util.Iterator;
import java.util.LinkedList;

public class Underdog extends AdvancedRobot {
	private boolean hasPreviousBulletHit; //when close to enemy, all bullets will hit. More power we want
	public static double battleFieldHeight;
	public static double battleFieldWidth;
	private long timestampLastFireEvent = System.currentTimeMillis();
	private double distanceRegisteredScan = 999999999;
	private double bearingScan = 9999999;
	private Point approxCoorScan = new Point(9999999, 9999999);
	public static final double LIMIT_DISTANCE_SHOOT = 300;
	public static final double CLOSE_RANGE_SHOOT = 100;
	public static final double DEGREES_REVERSE = 180;
	public static final double NB_DEGREES = 360;
	public static final double DEGREES_GUN_PER_TURN = 20;
	public static final int DEFAULT_FIRE_ENERGY = 1;
	public static final long ONE_HALF_SECONDS = 1500;
	
	public static final double THRESHOLD_DIRECTION_CHANGE = 3.0;
	public static final double THRESHOLD_DIST_REMAINING = 10;
	public static final double THRESHOLD_TOTAL_CHANGE = 25.0;
	public static final double THRESHOLD_BEARING = 45.0;
	public static final double MIN_BEARING = -180;
	public static final double MAX_BEARING = 180;
	public static final double FACING_ENEMY = 90;
	public static final double MIN_DISTANCE = 20;
	public static final double RANGE_DISTANCE = 200;
	
	private boolean isGunAndRadarIndependent = true;
	private LinkedList<BulletWrapper> bulletsInMotion;
	private AverageGunHeadingChange avgChangeHeading;
	
    @Override //this code runs every time a turn is finished??
    public void run() {
    	avgChangeHeading = new AverageGunHeadingChange();
    	bulletsInMotion = new LinkedList<>();
    	setAdjustGunForRobotTurn(isGunAndRadarIndependent);
    	setAdjustRadarForGunTurn(isGunAndRadarIndependent);
    	setTurnRadarRight(Double.POSITIVE_INFINITY);
    	
    	battleFieldHeight = getBattleFieldHeight();
    	battleFieldWidth = getBattleFieldWidth();
        setAllColors(Color.black);
        setBulletColor(Color.RED);
        setBodyColor(Color.black);
        setScanColor(Color.green);
        setBodyColor(Color.ORANGE);
        
        boolean isFirst = true;
        
        while (true) {
        	if(isFirst) {
        		RobotUtils.moveCurve(Math.random()*400, Math.random()*200, this, true);
        		isFirst = false;
        	}
        	execute();
        	
        	long currentTime = System.currentTimeMillis();
        	if( Math.abs(getGunTurnRemaining())<10 && 
        			(isDistanceCondOK() ||
        			hasLowChangeHeading() ||
        			currentTime-timestampLastFireEvent>500)) {
        		out.println("FIRE!");
        		timestampLastFireEvent = currentTime;
        		Bullet bulletFired = fireBullet(DEFAULT_FIRE_ENERGY);
        		if(bulletFired != null) {
	        		bulletsInMotion.add(new BulletWrapper(bulletFired, 
	        				System.currentTimeMillis()));
        		}
        	} else {
        	}
        	
        	if(RobotUtils.isMovingForward && 
        			getDistanceRemaining()<THRESHOLD_DIST_REMAINING) {
        		RobotUtils.moveBack(Math.random()*RANGE_DISTANCE+MIN_DISTANCE, this);
        	} else if(!RobotUtils.isMovingForward && 
        			getDistanceRemaining()<THRESHOLD_DIST_REMAINING){
        		RobotUtils.moveAhead(Math.random()*RANGE_DISTANCE+MIN_DISTANCE, this);
        	} else {
        		
        	}
        	
        	if ( getRadarTurnRemaining() == 0.0 ) {
                setTurnRadarRightRadians( Double.POSITIVE_INFINITY );
        	}
        }
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
    	distanceRegisteredScan = e.getDistance();
        bearingScan = e.getBearing();
    	
        setTurnRadarRight(2.0 * Utils.normalRelativeAngleDegrees(getHeading() + e.getBearing() - getRadarHeading()));
        double angleTurn = (getHeading() - getGunHeading() + e.getBearing())%NB_DEGREES;
        if(angleTurn<0) {
        	double angleLeft = Math.abs(angleTurn);
        	if(angleLeft>180) {
        		setTurnGunRight(NB_DEGREES-angleLeft);
        	} else {
        		setTurnGunLeft(Math.abs(angleTurn));
        	}
        } else {
        	if(angleTurn>180) {
        		setTurnGunLeft(NB_DEGREES-angleTurn);
        	} else {
        		setTurnGunRight(angleTurn);
        	}
        }
        
        if(bearingScan>MAX_BEARING-THRESHOLD_BEARING || 
        		(bearingScan<THRESHOLD_BEARING && bearingScan>-THRESHOLD_BEARING) ||
        		bearingScan<MIN_BEARING+THRESHOLD_BEARING) {
        	double degreesTurn = (e.getBearing()+NB_DEGREES-FACING_ENEMY)%FACING_ENEMY;
        	setTurnLeft(degreesTurn);
        }
    }
    
    public boolean hasLowChangeHeading() {
    	return avgChangeHeading.getTotalChangeHeading() < THRESHOLD_TOTAL_CHANGE
    			&& avgChangeHeading.getAverageChangeHeading() < THRESHOLD_DIRECTION_CHANGE
    			&& avgChangeHeading.hasChangedAllValuesList();
    }
    
    public boolean isDistanceCondOK() {
    	return (distanceRegisteredScan < LIMIT_DISTANCE_SHOOT && 
    			(getEnergy() > 20)) ||
    			distanceRegisteredScan < CLOSE_RANGE_SHOOT;
    }
    
    //TODO
    public boolean isBearingCondOK() {
    	return true;
    }
    
    //TODO: Turn at the same time from coming back from wall
    //You lose health by hitting a wall!!
    @Override
    public void onHitWall(HitWallEvent e) {
    	//go back
    	out.println("hit");
    	
    }
    
    @Override
    public void onStatus(StatusEvent e) {
    	RobotStatus robotStatus = e.getStatus();
    	double x = robotStatus.getX();
    	double y = robotStatus.getY();
    	boolean close = false;
    	
    	if(x < 20) {
    		close = true;
    	} else if (x > getBattleFieldWidth() - 20) {
    		close = true;
    	} else if (y < 20) {
    		close = true;
    	} else if (y > getBattleFieldHeight() - 20) {
    		close = true;
    	}
    	
    	if (close) {
    		TooCloseToWallEvent event = new TooCloseToWallEvent(getHeading(), x, y);
    	}
    }
    
    public void onCloseToWall(TooCloseToWallEvent e) {
    	
    }
    
    @Override
    public void onBulletHit(BulletHitEvent e) {
    	double headingBullet = e.getBullet().getHeading();
    	double velocityBullet = e.getBullet().getVelocity(); //measured in pixels per turn
    	double bulletX = e.getBullet().getX();
    	double bulletY = e.getBullet().getY();
    	double robotX = getX();
    	double robotY = getY(); //for now. should have a queue
    	double distanceFired = TrigoUtils.getDistanceBtwPoints(bulletX, bulletY, robotX, robotY);
    	
    	if(hasPreviousBulletHit && distanceFired < CLOSE_RANGE_SHOOT) {
    		//lucky if two bullets on enemy consecutive. Assuming next to robot.
    		fire(Rules.MAX_BULLET_POWER); 
    	} else {
    		fire(DEFAULT_FIRE_ENERGY);
    	}
    	hasPreviousBulletHit = true;
    	BulletUtils.removeBulletNoLongerPresent(bulletsInMotion, e.getBullet(), this);
    }
    
    @Override
    public void onBulletMissed(BulletMissedEvent e) {
    	hasPreviousBulletHit = false;
    	
    	BulletUtils.removeBulletNoLongerPresent(bulletsInMotion, e.getBullet(), this);
    }
    
    //TODO: Create method to determine how long would it take to reach enemy and plan 
    //deltaX and deltaY of bullet accordingly
    
    @Override
    public void onBulletHitBullet(BulletHitBulletEvent e) {
    	
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
    	RobotUtils.doRandomMove(this);
    	//esquive();
    }

	private void esquive() {
		this.turnLeft(45);
		this.ahead(50);
		this.back(100);
		this.ahead(200);
		
	}
}
