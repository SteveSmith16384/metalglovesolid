package gauntlet;

import java.awt.Image;

import gauntlet.sprites.Sprite;

public class MapSquare extends Sprite {
	
/*	public static void MapSquareFactory(Main m, int x, int y, boolean collides, String filename) {
		//SpriteDataRecord sdf = m.getSpriteData(code);
		new MapSquare(m, x, y, collides, filename);

	}
	*/
	public MapSquare(Main m, int x, int y, boolean blocks, Image img) {
		super(m, "", x, y, Main.SQ_SIZE, Main.SQ_SIZE, blocks, 1, "", false);
		this.img = img;
	}

	public void process() {
		// Do nothing
	}

	public void collidedWith(Sprite s) {
		// Do nothing
	}

}
