package gauntlet.sprites;

import gauntlet.Main;
import gauntlet.SpriteDataRecord;
import ssmith.lang.Functions;
import ssmith.util.Interval;

public class MonsterGenerator extends Sprite {

	private Interval gen_interval = new Interval(1500, true);
	private String enemy_type;

	public static void GeneratorFactory(Main m, int x, int y, String gen_code) {
		SpriteDataRecord sdf = m.getSpriteData(gen_code);
		new MonsterGenerator(m, x, y, sdf.width, sdf.height, sdf.health, sdf.enemy_type, sdf.img_filename);
	}

	private MonsterGenerator(Main m, int x, int y, int w, int h, int health, String enemy_typ, String filename) {
		super(m, "Monster Generator", x, y, w, h, true, health, filename, true);
		enemy_type = enemy_typ;
	}

	public void collidedWith(Sprite s) {
		// Do nothing
	}

	public void process() {
		if (gen_interval.hitInterval()) {
			if (Functions.rnd(1, 2) == 1) {
				// Check the player isn't too close
				if (this.distanceTo(main.player) > (Main.SQ_SIZE*3)) {
					SpriteDataRecord sdf = main.getSpriteData(enemy_type);
					if (main.isAreaClear(x, y, sdf.width, sdf.height)) {
						//new IminentEnemy(main, this.x, this.y, enemy_type);
						Enemy.EnemyFactory(main, enemy_type, this.x, this.y);
					}
				}
			}
		}

	}

}