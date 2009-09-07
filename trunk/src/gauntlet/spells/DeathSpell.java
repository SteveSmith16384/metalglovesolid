package gauntlet.spells;

import gauntlet.Main;
import gauntlet.sprites.Enemy;
import gauntlet.sprites.Sprite;
import java.util.ArrayList;
import java.util.Iterator;

public class DeathSpell extends AbstractSpell {

	private static final int RANGE = Main.SQ_SIZE * 5;
	private static final int HARM = 100;

	public DeathSpell(Main m, int x, int y) {
		super(m, "Death", x, y, "items/book5.png");
	}

	public void collidedWith(Sprite s) {
		// Do nothing
	}

	public void process() {
		// Do nothing
	}

	public void cast() {
		int range = RANGE * main.player.magic_power;
		
		ArrayList arr = main.coll_sprites;
		Iterator it = arr.iterator();
		while (it.hasNext()) {
			Sprite s = (Sprite)it.next();
			if (s instanceof Enemy) {
				if (s.distanceTo(main.player) <= range) {
					Enemy e = (Enemy)s;
					e.damage(HARM);
				}
			}
		}
	}

}

