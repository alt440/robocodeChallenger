package primahackathon;

import robocode.Event;

public class TooCloseToWallEvent extends Event{
	private double heading;
	private double x;
	private double y;
	
	public TooCloseToWallEvent(double heading, double x, double y) {
		this.heading = heading;
		this.x = x;
		this.y = y;
	}
}
