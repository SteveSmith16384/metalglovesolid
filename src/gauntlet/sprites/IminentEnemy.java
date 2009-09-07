package gauntlet.sprites;

import gauntlet.Main;

import java.awt.Graphics;

/**
 * This is used to determine of there is room for a new enemy to be created.
 *
 */
public class IminentEnemy extends Sprite {

	//private boolean clear = false;

	public IminentEnemy(Main m, int x, int y, int w, int h) {//, String typ) {
		super(m, "", x, y, w, h, true, 1, "", false);
		//type = typ;
	}

	public void collidedWith(Sprite s) {
		/*if (s instanceof Enemy) {
			clear = false; // Not clear!
		} else {
			clear = true && clear;
		}*/
		
	}

	public void draw(Graphics g, int x, int y) {
		// Do nothing
	}

	public void process() {
	/*	clear = true;
		this.checkForCollisions(); // We need this here cos we don't move. 
		if (clear) {
			Enemy.EnemyFactory(main, type, this.x, this.y);
			this.remove(false);
		}*/
	}

}
