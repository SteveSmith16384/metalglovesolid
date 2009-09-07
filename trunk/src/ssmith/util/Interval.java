/*
 * Created on Oct 1, 2005
 *
 */
package ssmith.util;

/**
 * @author Stephen Smith
 *
 */
public class Interval {

	private long last_check_time, duration;


	/**
	 *
	 */
	public Interval(long duration, boolean hit_immed) {
		super();
		this.duration = duration;
		if (!hit_immed) {
			this.last_check_time = System.currentTimeMillis();
		}
	}

	public void setInterval(long amt) {
		duration = amt;
                this.last_check_time = System.currentTimeMillis();
	}

	public boolean hitInterval() {
		if (System.currentTimeMillis() - duration > this.last_check_time) {
			this.last_check_time = System.currentTimeMillis();
			return true;
		}
		return false;
	}

}
