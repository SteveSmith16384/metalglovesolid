package gauntlet.sprites;

import gauntlet.Main;

public class PickupFood extends AbstractPickup {
	
	public static final int SIZE = 38;
	
	/*public static PickupFood Factory(Main main, int x, int y) {
		SpriteDataRecord sdf = main.getSpriteData("FOOD1");
		return new PickupFood(main, x, y, sdf.img_filename);
	}*/
	
	public PickupFood(Main m, int x, int y) {
		super(m, "Food", x, y, "items/barrel.png");
	}

	public int getHealthInc() {
		return 10;
	}
}
