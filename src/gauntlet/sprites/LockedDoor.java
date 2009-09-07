package gauntlet.sprites;

import gauntlet.Main;

public class LockedDoor extends Sprite{

	public LockedDoor(Main m, int x, int y) {
		super(m, "Locked Door", x, y, Main.SQ_SIZE, Main.SQ_SIZE, true, 999, "scenery/door.png", true);
	}

	public void collidedWith(Sprite s) {
	}

	public void process() {
	}

}
