package gauntlet.sprites;

import gauntlet.Main;

public abstract class AbstractPickup extends Sprite {
	
	public AbstractPickup(Main m, String name, int x, int y, String filename) {
		super(m, name, x, y, 32, 32, true, 1, filename, true);

	}

	public void collidedWith(Sprite s) {
		// Do nothing
	}

	public void process() {
		// Do nothing
	}

}
