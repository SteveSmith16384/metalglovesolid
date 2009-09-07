package gauntlet.sprites;

import gauntlet.Main;

public class ExitDoor extends Sprite {
	
	public int to_level;

	public ExitDoor(Main m, int x, int y, int lvl) {
		super(m, "Exit", x, y, 72, 72, true, 1, "scenery/dwarven-doors-closed.png", false);
		
		to_level = lvl;
	}

	public void process() {
		// Do nothing
	}

	public void collidedWith(Sprite s) {
		// Do nothing
	}

}
