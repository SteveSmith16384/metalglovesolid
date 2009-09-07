package gauntlet.spells;

import gauntlet.Main;
import gauntlet.sprites.AbstractPickup;

public abstract class AbstractSpell extends AbstractPickup implements ISpell {
	
	public AbstractSpell(Main m, String _name, int x, int y, String filename) {
		super(m, _name, x, y, filename);
	}

}
