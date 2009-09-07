package gauntlet.sprites;

import gauntlet.Main;
import gauntlet.SpriteDataRecord;

public class Scenery extends Sprite {

	public static void SceneryFactory(Main m, int x, int y, String code) {
		SpriteDataRecord sdf = m.getSpriteData(code);
		new Scenery(m, sdf.name, x, y, sdf.width, sdf.height, sdf.collides, sdf.img_filename);

	}
	
	private Scenery(Main m, String name, int x, int y, int w, int h, boolean blocks, String filename) {
		super(m, name, x, y, w, h, blocks, 0, filename, false);
	}

	public void collidedWith(Sprite s) {
		// Do nothing
	}

	public void process() {
		// Do nothing
	}

}
