package gauntlet.spells;

import java.awt.Point;
import gauntlet.Main;
import gauntlet.sprites.Sprite;

public class TeleportSpell extends AbstractSpell {

	public TeleportSpell(Main m, int x, int y) {
		super(m, "Teleport", x, y, "items/book5.png");
	}

	public void collidedWith(Sprite s) {
		// Do nothing
	}

	public void process() {
		// Do nothing
	}

	public void cast() {
			Point p = main.map_gen.getRandomClearArea(main.player.width, main.player.height);
			main.player.x = p.x;
			main.player.y = p.y;
	}

}

