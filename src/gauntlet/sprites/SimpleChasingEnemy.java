package gauntlet.sprites;

import gauntlet.Main;
import ssmith.lang.Functions;

public class SimpleChasingEnemy extends Enemy {

	private static final int RANDOM_MOVE_THRESH = 40;
	private boolean moving_normally = true;
	private int random_move_count;
	private int rnd_off_x, rnd_off_y;
	private boolean always_process;

	public SimpleChasingEnemy(Main m, String name, int x, int y, int w, int h, boolean collides, int health, int speed, String filename, int harm, boolean _always_process) {
		super(m, name, x, y, w, h, true, health, speed, filename, harm);
		
		always_process = _always_process;
	}

	public void process() {
		if (always_process || this.distanceTo(main.player) <= Main.ENEMY_PROCESS_DIST) {
			if (moving_normally) {
				if (this.no_move_count < RANDOM_MOVE_THRESH) {
					int off_x = Functions.sign(main.player.x - this.x) * speed;
					int off_y = Functions.sign(main.player.y - this.y) * speed;
					super.selectImage(off_x, off_y);
					if (this.move(off_x, off_y)) {
						no_move_count = 0;
					} else {
						no_move_count++;
					}
				} else {
					moving_normally = false;
					no_move_count = 0;
					random_move_count = RANDOM_MOVE_THRESH;
					while (true) {
						rnd_off_x = Functions.rnd(-1, 1);
						rnd_off_y = Functions.rnd(-1, 1);
						if (rnd_off_x != 0 || rnd_off_y != 0) {
							break;
						}
					}
					rnd_off_x = rnd_off_x * speed;
					rnd_off_y = rnd_off_y * speed;
				}
			} else {
				super.selectImage(rnd_off_x, rnd_off_y);
				this.move(rnd_off_x, rnd_off_y);
				random_move_count--;
				if (random_move_count <= 0) {
					this.moving_normally = true;
				}
			}
		}

	}

}