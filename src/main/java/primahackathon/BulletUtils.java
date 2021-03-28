package primahackathon;

import java.util.Iterator;
import java.util.LinkedList;

import robocode.AdvancedRobot;
import robocode.Bullet;
import robocode.Event;

public class BulletUtils {
	
	public static double predictTimeToEnemy(double distance, double power) {
		double speedBullet = 20 - 3*power; // see https://robowiki.net/wiki/Bullet
		double time = distance/speedBullet;
		return time; //what is time really?
	}
	
	public static double predictNextMouvementEnemy() {
		return 0;
	}
	
	public static void removeBulletNoLongerPresent(LinkedList<BulletWrapper> bulletsInMotion, 
			Bullet eventDeadBullet, AdvancedRobot robot) {
		Iterator it = bulletsInMotion.iterator();
    	while(it.hasNext()) {
    		BulletWrapper currentBullet = (BulletWrapper)it.next();

    		if(!currentBullet.getBullet().isActive()) {
    			robot.out.println("Time taken: "+(System.currentTimeMillis()-currentBullet.getTimestamp()));
    			robot.out.println("Distance: "+TrigoUtils.getDistanceBtwPoints(eventDeadBullet.getX(), 
    					eventDeadBullet.getY(), robot.getX(), robot.getY()));
    			robot.out.println("Diff heading: "+Math.abs(robot.getGunHeading()-eventDeadBullet.getHeading()));
    			robot.out.println("--------------------");
    			it.remove();
    			break;
    		}
    	}
	}
}
