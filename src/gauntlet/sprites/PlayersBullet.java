package gauntlet.sprites;

import gauntlet.Main;
import gauntlet.MapSquare;
import ssmith.lang.Functions;

public class PlayersBullet extends Sprite {
	
	private static final int SPEED = 14;
	private int move_x, move_y, dist_so_far;
	private int damage;
	public static int total=0;

	public PlayersBullet(Main m, int x, int y, int w, int h, int ox, int oy, int dmg, int dist, String filename) {
		super(m, "Bullet", x, y, w, h, true, 1, filename, true);
		
		move_x = Functions.sign(ox) * SPEED;
		move_y = Functions.sign(oy) * SPEED;
		damage = dmg;
		dist_so_far = dist;
		
		total++;
	}

	public void collidedWith(Sprite s) {
		if (s instanceof MapSquare) {
			this.markForRemoval();
			//s.markForRemoval();
			//Enemy.EnemyFactory(main, "E1", x, y);
		} else if (s instanceof LockedDoor) {
			this.markForRemoval();
		} else if (s instanceof Enemy) {
			s.damage(damage);
			this.markForRemoval();
		} else if (s instanceof Scenery) {
			if (s.collideable) {
				this.markForRemoval();
			}
		} else if (s instanceof MonsterGenerator) {
			s.damage(damage);
			this.markForRemoval();
		} else if (s instanceof AbstractPickup) {
			s.damage(damage);
			this.markForRemoval();
			main.msg_box.setText("You shot the " + s.getName() + "!");
		}
	}

	public void process() {
		move(move_x, move_y);
		//Main.p("" + this.y);
		dist_so_far -= SPEED;
		if (dist_so_far < 0) {
			this.markForRemoval();
		}
	}

	public void markForRemoval() {
		if (this.to_be_removed == false) {
			total--;
		}
		super.markForRemoval();
	}

}
