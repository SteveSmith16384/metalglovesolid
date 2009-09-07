package gauntlet.sprites;

import java.awt.Image;

import ssmith.lang.Functions;

import gauntlet.Main;

public class MovingWall extends Sprite {
	
	private int rnd_off_x, rnd_off_y;
	
	public MovingWall(Main m, int x, int y, Image img) {
		super(m, "Moving Wall", x, y, Main.SQ_SIZE-1, Main.SQ_SIZE-1, true, 999, "", true);
		this.img = img;
		this.getRandomDirection();
	}

	@Override
	public void collidedWith(Sprite s) {
		this.moveBack();
	}

	@Override
	public void process() {
		if (this.move(rnd_off_x, rnd_off_y) == false) {
			this.getRandomDirection();
		}
	}
	
	private void getRandomDirection() {
		while (true) {
			rnd_off_x = Functions.rnd(-1, 1);
			rnd_off_y = Functions.rnd(-1, 1);
			if (rnd_off_x != 0 || rnd_off_y != 0) {
				break;
			}
		}

	}

}
