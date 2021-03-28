package primahackathon;

import robocode.Bullet;

public class BulletWrapper {
	private Bullet bullet;
	private long timestampShot;
	
	public BulletWrapper(Bullet bullet, long timestampShot) {
		this.bullet = bullet;
		this.timestampShot = timestampShot;
	}
	
	public long getTimestamp() {
		return timestampShot;
	}
	
	public Bullet getBullet() {
		return bullet;
	}
}
