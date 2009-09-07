package gauntlet.sprites;

import gauntlet.Main;
import gauntlet.MapSquare;
import gauntlet.SpriteDataRecord;
import ssmith.lang.Functions;

public abstract class Enemy extends Sprite {
	
	public int speed, harm_caused;
	protected int no_move_count;

	public static void EnemyFactory(Main m, String code, int x, int y) {
		SpriteDataRecord sdf = m.getSpriteData(code);

		if (sdf.enemy_type.equalsIgnoreCase("SCE")) {
			new SimpleChasingEnemy(m, sdf.name, x, y, sdf.width, sdf.height, sdf.collides, sdf.health, sdf.speed, sdf.img_filename, sdf.damage, sdf.always_process);
		} else {
			throw new RuntimeException("Unknown enemy type: " + sdf.enemy_type);
		}

	}

	protected Enemy(Main m, String name, int x, int y, int w, int h, boolean collide, int health, int spd, String fname, int harm) {
		// Notice we randomly adjust size (only smaller!) to make them "crowd" better
		super(m, name, x, y, w - Functions.rnd(0, 5), h - Functions.rnd(0, 5), collide, health, fname + ".png", true);

		speed = spd;
		harm_caused = harm;
		imgN = main.getImage(fname + ".png");
		imgS = main.getImage(fname + ".png");
		imgE = main.getImage(fname + ".png");
		imgW = main.getImage(fname + ".png");
	}
	
	public void collidedWith(Sprite s) {
		if (s instanceof Player) {
			s.damage(this.harm_caused);
			this.moveBack();
		} else if (s instanceof MapSquare) {
			this.moveBack();
		} else if (s instanceof Enemy) {
			this.moveBack();
		} else if (s instanceof Scenery) {
			if (s.collideable) {
				this.moveBack();
			}
		} else if (s instanceof LockedDoor) {
			this.moveBack();
		}
	}	

	public void markForRemoval() {
		super.markForRemoval();
		//main.player.score += 10;
	}

}

