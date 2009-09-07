package gauntlet.sprites;

import gauntlet.Main;
import gauntlet.MapSquare;
import gauntlet.spells.AbstractSpell;
import gauntlet.spells.ISpell;
import java.util.ArrayList;
import ssmith.util.Interval;

/**
 * The player needs to be the same size as the map squares so he can fit down corridors.
 *
 */
public class Player extends Sprite {

	public int off_x, off_y;
	private int shoot_x=1, shoot_y=1; // Default direction in case the player hasn't moved
	public int last_x, last_y;
	private int bullet_w, bullet_h;
	private Interval lose_health_interval = new Interval(2000, false);
	public int keys = 0, score=0;
	public String bullet_filename;
	public ArrayList<ISpell> spells = new ArrayList<ISpell>();
	
	// Stats
	public int speed;
	public int magic_power;// Starts at 1-4
	public float protection;
	public int allowed_bullets = 1;
	public int bullet_dam, bullet_dist;
	private Interval shot_interval;

	public Player(Main m, int w, int h, int health, int spd, String filename, int dmg, int dst, int _bullet_w, int _bullet_h, String bullet_fname, int magic_pow, float protec) {
		super(m, "", 0, 0, w, h, true, health, filename + ".png", true); // Needs to be size SQ-1 cos the player jumps a few pixels at a time

		speed = spd;
		bullet_dam = dmg;
		bullet_dist = dst;
		bullet_w = _bullet_w;
		bullet_h = _bullet_h;
		main.player = this;
		this.bullet_filename = bullet_fname;
		imgN = main.getImage(filename + ".png"); //todo
		imgS = main.getImage(filename + ".png");
		imgE = main.getImage(filename + ".png");
		imgW = main.getImage(filename + ".png");
		this.setShotInterval(100);
		PlayersBullet.total = 0;
		magic_power = magic_pow;
		protection = protec;
	}

	public void setShotInterval(int shot_int) {
		shot_interval = new Interval(shot_int, true);
	}

	public void process() {
		if (lose_health_interval.hitInterval()) {
			damage(1);
		}
		if (off_x != 0 || off_y != 0) {
			last_x = off_x;
			last_y = off_y;
			this.selectImage(off_x, off_y);
			this.move(off_x, off_y);
		}
	}
	
	public void damage(int amt) {
		int a = amt - (int)((float)amt * this.protection);
		super.damage(a);
	}

	public void markForRemoval() {
		super.markForRemoval();
		main.setGameStage(1);
	}

	public void collidedWith(Sprite s) {
		if (s instanceof MapSquare) {
			moveBack();
		} else if (s instanceof MonsterGenerator) {
			moveBack();
		} else if (s instanceof MovingWall) {
			moveBack();
		} else if (s instanceof Enemy) {
			moveBack();
		} else if (s instanceof Scenery) {
			if (s.collideable) {
				this.moveBack();
			}
		} else if (s instanceof LockedDoor) {
			if (this.keys == 0) {
				moveBack();
			} else {
				s.markForRemoval();
				this.keys--;
			}
		} else if (s instanceof ExitDoor) {
			ExitDoor exit = (ExitDoor)s;
			main.nextLevel(exit.to_level);
		} else if (s instanceof PickupKey) {
			s.markForRemoval();
			this.keys++;
		} else if (s instanceof Gold) {
			s.markForRemoval();
			this.score += 10;
		} else if (s instanceof PickupFood) {
			s.markForRemoval();
			PickupFood f = (PickupFood)s;
			this.health += f.getHealthInc();
		} else if (s instanceof AbstractSpell) {
			s.markForRemoval();
			this.spells.add((ISpell)s);
			main.msg_box.setText(s.getName() + " Spell Acquired!");
		} else if (s instanceof PowerUpIncReloadSpeed) {
			s.markForRemoval();
			this.allowed_bullets++;
			main.msg_box.setText("Weapon Speed Increased!");
		} else if (s instanceof PowerUpIncDamage) {
			s.markForRemoval();
			this.bullet_dam++;
			main.msg_box.setText("Weapon Damage Increased!");
		}
	}

	public void lockShootDirection() {
		if (this.off_x != 0 || this.off_y != 0) {
			this.shoot_x = this.off_x;
			this.shoot_y = this.off_y;
		}
	}

	public void shoot() {
		if (shot_interval.hitInterval()) {
			//Main.p("" + PlayersBullet.total);
			if (PlayersBullet.total <= allowed_bullets-1) {
				new PlayersBullet(main, this.x, this.y, bullet_w, bullet_h, shoot_x, shoot_y, bullet_dam, bullet_dist, this.bullet_filename);
				//Main.p("Player shot!");
			}
		}
	}

}
