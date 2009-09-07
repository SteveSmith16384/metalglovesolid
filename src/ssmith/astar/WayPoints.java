/*
 * Created on 19-Sep-2005
 *
 */
package ssmith.astar;

import java.awt.Point;
import java.util.ArrayList;
import ssmith.lang.Functions;

/**
 * @author stephen smith
 *
 */
public class WayPoints extends ArrayList {
	
	private static final long serialVersionUID = 1L;

	public WayPoints() {
		super();
	}
	
	public void insertRoute(int pos, WayPoints w) {
		this.addAll(pos, w);
	}
	
	public void truncate(int amt) {
	    while (this.size() > amt) {
	        this.remove(this.size()-1);
	    }
	}
	
	public void remove(int x, int y) {
		for (int i=0 ; i<this.size() ; i++) {
			Point p = (Point)get(i);
			if (p.x == x && p.y == y) {
				this.remove(p);
			}
		}
	}

	public boolean contains(int x, int y) {
		for (int i=0 ; i<this.size() ; i++) {
			Point p = (Point)get(i);
			if (p.x == x && p.y == y) {
				return true;
			}
		}
		return false;
	}

	public Point getClosestPoint(int x, int y) {
		double closest_dist = Double.MAX_VALUE;
		int closest_point = -1;
		for(int p=0 ; p<this.size() ; p++) {
			Point pnt = (Point)this.get(p);
			double dist = Functions.distance(x, y, pnt.x, pnt.y);
			if (dist < closest_dist) {
				closest_dist = dist;
				closest_point = p;
			}
		}
		return (Point)this.get(closest_point);
	}

	public Point getNextPoint() {
		if (this.hasAnotherPoint()) {
			return (Point)get(0);
		} else {
			return null;
		}
	}
	
	public Point getLastPoint() {
		return (Point)get(size()-1);
	}
	
	public void removeCurrentPoint() {
		remove(0);
	}
	
	public boolean hasAnotherPoint() {
		return this.size() > 0;
	}
	
	public Point getRandomPoint() {
		return this.getRandomPoint(false);
	}
	
	public Point getRandomPoint(boolean remove) {
		if (this.hasAnotherPoint()) {
			int no = Functions.rnd(0, size()-1);
			Point p = (Point)get(no); 
			if (remove) {
				this.remove(no);
			}
			return p;
		}
		return null;
	}
	
	public void add(int x, int y) {
		add(new Point(x, y));
	}
	
	public void clear() {
		removeAll(this);
	}
	
	public WayPoints copy() {
		WayPoints w = new WayPoints();
		for (int i=0 ; i<this.size() ; i++) {
			Point p = (Point)get(i);
			w.add(p);
		}
		return w;
	}
	
}
