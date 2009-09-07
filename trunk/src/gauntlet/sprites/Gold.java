package gauntlet.sprites;

import gauntlet.Main;

public class Gold extends AbstractPickup {
	
	public static final int SIZE = 30;
	
	public Gold(Main m, int x, int y) {
		super(m, "Gold", x, y, "/items/gold-coins-small.png");
	}

}
