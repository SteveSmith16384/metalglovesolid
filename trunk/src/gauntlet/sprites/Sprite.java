package gauntlet.sprites;

import gauntlet.Main;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Iterator;
import ssmith.lang.Functions;

public abstract class Sprite {

	private static final long serialVersionUID = 1L;

	protected Main main;
	public int x, y, width, height;
	private int prev_x, prev_y;
	protected boolean collideable;
	protected int health;
	public boolean to_be_removed = false, process;
	public Image img;
	protected Image imgN, imgS, imgE, imgW;
	private String name;

	public Sprite(Main m, String _name, int x, int y, int w, int h, boolean collide, int hlth, String filename, boolean _process) {
		main = m;
		name = _name;
		this.x = x;
		this.y = y;
		this.width = w;
		this.height = h;
		collideable = collide;
		health = hlth;
		process = _process;

		if (filename.length() > 0) {
			img = main.getImage(filename);
		}
		main.addSprite(this);
		
		/*if (this instanceof MapSquare == false) {
		if (w >= Main.SQ_SIZE || h >= Main.SQ_SIZE) {
			throw new RuntimeException("Sprite to big!");
		}
		}*/
	}

	public void draw(Graphics g, int x, int y) {
		if (img != null) {
			//g.setColor(Color.white);
			//g.drawRect(x, y, width, height);
			g.drawImage(img, x, y, main);
		} else {
			Main.p("Null image for " + this.toString());
		}
	}


	public abstract void process();

	public abstract void collidedWith(Sprite s);
	
	public String getName() {
		return name;
	}

	public boolean isCollideable() {
		return this.collideable;
	}

	public int getHealth() {
		return this.health;
	}

	/**
	 * Returns whether they were able to move.
	 */
	public boolean move(int off_x, int off_y) {
		int orig_x = this.x;
		int orig_y = this.y;
		
		prev_x = off_x;
		prev_y = 0;
		this.x += off_x;
		/*if (x < 0) {
			x = 0;
		}*/
		this.checkForCollisions();

		prev_x = 0;
		prev_y = off_y;
		this.y += off_y;
		/*if (y < 0) {
			y = 0;
		}*/
		this.checkForCollisions();
		
		return orig_x != this.x || orig_y != this.y;
	}

	public void moveBack() {
		x -= prev_x;
		y -= prev_y;
	}

	public void selectImage(int off_x, int off_y) {
		if (off_x > 0) {
			img = imgE;
		} else if (off_x < 0) {
			img = imgW;
		} else if (off_y > 0) {
			img = imgS;
		} else {
			img = imgN;
		}
	}

	public void damage(int amt) {
		this.health -= amt;
		if (health <= 0) {
			health = 0;
			this.markForRemoval();
		}
	}

	public double distanceTo(Sprite s) {
		return Functions.distance(x, y, s.x, s.y);
	}

	public void markForRemoval() {
		to_be_removed = true;
	}

	public void checkForCollisions() {
		ArrayList arr = main.getPotentialColliders(this);
		Iterator it = arr.iterator();
		while (it.hasNext()) {
			Sprite s = (Sprite)it.next();
			if (s != this) {
				if (s.to_be_removed == false) {
					if (s.hasCollidedWith(this)) {
						//main.p("Collided with " + s.toString());
						this.collidedWith(s);
					}
				}
			}
		}
	}

	public boolean hasCollidedWith(Sprite r) {
		int tw = this.width;
		int th = this.height;
		int rw = r.width;
		int rh = r.height;
		if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
			return false;
		}
		int tx = this.x;
		int ty = this.y;
		int rx = r.x;
		int ry = r.y;
		rw += rx;
		rh += ry;
		tw += tx;
		th += ty;
		//      overflow || intersect
		return ((rw < rx || rw > tx) &&
				(rh < ry || rh > ty) &&
				(tw < tx || tw > rx) &&
				(th < ty || th > ry));
	}


	public int getZ() {
		return getY();
	}

	public int getY() {
		return this.y;
	}

	public int getX() {
		return x;
	}

	public int getHeight() {
		return height;
	}

	public long getWidth() {
		return width;
	}

}
