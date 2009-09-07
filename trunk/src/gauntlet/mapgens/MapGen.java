package gauntlet.mapgens;

import gauntlet.LevelDataRecord;
import gauntlet.Main;
import gauntlet.MapSquare;
import gauntlet.SpriteDataRecord;
import gauntlet.spells.DeathSpell;
import gauntlet.spells.TeleportSpell;
import gauntlet.sprites.Enemy;
import gauntlet.sprites.ExitDoor;
import gauntlet.sprites.Gold;
import gauntlet.sprites.MonsterGenerator;
import gauntlet.sprites.MovingWall;
import gauntlet.sprites.PickupFood;
import gauntlet.sprites.PowerUpIncDamage;
import gauntlet.sprites.PowerUpIncReloadSpeed;

import java.awt.Image;
import java.awt.Point;
import ssmith.astar.AStar;
import ssmith.astar.IAStarMapInterface;
import ssmith.image.ImageFunctions;
import ssmith.lang.Functions;

public abstract class MapGen extends Thread implements IAStarMapInterface {

	protected static final int NOT_SET = 0;
	protected static final int FLOOR = 1;
	protected static final int WALL = 2;

	protected Main main;
	protected int imap[][];
	protected LevelDataRecord data;
	private Image walls[];
	
	public MapGen(Main m, LevelDataRecord _data) {
		super("MapGen");
		this.setDaemon(true);
		main = m;
		data = _data;

		walls = ImageFunctions.ExtractGraphics(main.getImage("scenery/" + data.tileset), 4, 4, 32, 2, main);
		for (int i=0 ; i<walls.length ; i++) {
			walls[i] = ImageFunctions.scaleImage(walls[i], Main.SQ_SIZE, Main.SQ_SIZE, main);
		}
	}

	public void run() {
		boolean can_exit = true;
		while (can_exit) {
			imap = new int[data.width][data.height]; 
			for (int y=0 ; y<data.height ; y++) {
				for (int x=0 ; x<data.width ; x++) {
					imap[x][y] = NOT_SET;
				}
			}

			Main.p("Generating map...");
			this.createMap();
			//drawMap();
			Main.p("Checking for exit...");
			can_exit = routeExists();
		}
		Main.p("Map generated.");
		this.createMapsquareSprites();

		Point p = this.getPlayerStartPointInMapCoords();
		main.player.x = p.x * Main.SQ_SIZE;
		main.player.y = p.y * Main.SQ_SIZE;

		p = this.getExitPointInMapCoords();
		new ExitDoor(main, p.x * Main.SQ_SIZE, p.y * Main.SQ_SIZE, main.getLevel()+1);

		addItems();
	}

	protected void drawMap() {
		for (int y=0 ; y<data.height ; y++) {
			for (int x=0 ; x<data.width ; x++) {
				if (imap[x][y] == WALL) {
					System.out.print("W");
				} else {
					System.out.print("+");
				}
			}
			System.out.println("");
		}

	}

	private void addItems() {
		// Add monster gens
		for (int i=0 ; i<data.num_gens ; i++) {
			SpriteDataRecord sdf = main.getSpriteData(data.monster_gen_type);
			Point p = getRandomClearArea(sdf.width, sdf.height);
			MonsterGenerator.GeneratorFactory(main, p.x, p.y, data.monster_gen_type);
		}

		// Food
		for (int i=0 ; i<data.num_food ; i++) {
			Point p = getRandomClearArea(PickupFood.SIZE, PickupFood.SIZE);
			new PickupFood(main, p.x, p.y);
		}

		// Gold
		for (int i=0 ; i<data.num_gold ; i++) {
			Point p = getRandomClearArea(Gold.SIZE, Gold.SIZE);
			new Gold(main, p.x, p.y);
		}

		// Power ups
		Point p = getRandomClearArea(Main.SQ_SIZE, Main.SQ_SIZE);
		int pu = Functions.rnd(1, 2);
		switch (pu) {
		case 1:
			new PowerUpIncReloadSpeed(main, p.x, p.y);
			break;
		case 2:
			new PowerUpIncDamage(main, p.x, p.y);
			break;
		default:
			throw new RuntimeException("No power-up selected");
		}

		// Spells
		p = getRandomClearArea(Main.SQ_SIZE, Main.SQ_SIZE);
		int spell = Functions.rnd(2, 2);
		switch (spell) {
		case 1:
			new DeathSpell(main, p.x, p.y);
			break;
		case 2:
			new TeleportSpell(main, p.x, p.y);
			break;
		default:
			throw new RuntimeException("No spell selected");
		}

		// Special enemies
		if (data.special_enemies.length() > 0) {
			String types[] = data.special_enemies.split(",");
			for (int i=0 ; i<types.length ; i++) {
				p = getRandomClearArea(Main.SQ_SIZE, Main.SQ_SIZE);
				Enemy.EnemyFactory(main, types[i], p.x, p.y);
			}
		}

		// Num Moving Walls
		if (data.num_moving_walls > 0) {
			for (int i=0 ; i<data.num_moving_walls ; i++) {
				p = getRandomClearArea(Main.SQ_SIZE, Main.SQ_SIZE);
				new MovingWall(main, p.x, p.y, walls[0]);
			}
		}

	}

	public abstract void createMap();

	public abstract Point getPlayerStartPointInMapCoords();

	public abstract Point getExitPointInMapCoords();

	public Point getRandomClearArea(int w, int h) {
		while (true) {
			int mx = Functions.rnd(1, data.width-1);
			int my = Functions.rnd(1, data.height-1);
			if (imap[mx][my] == FLOOR) {
				int x = mx * Main.SQ_SIZE;
				int y = my * Main.SQ_SIZE;
				if (main.isAreaClear(x, y, w, h)) {
					return new Point(x, y);
				}
			}
		}
	}

	public boolean routeExists() {
		AStar astar = new AStar(this, 3000);
		Point start = this.getPlayerStartPointInMapCoords();
		Point end = this.getExitPointInMapCoords();
		astar.findPath(start.x, start.y, end.x, end.y, false);
		return !astar.failedFindingPath();
	}

	public int getMapHeight() {
		return data.height;
	}

	public float getMapSquareDifficulty(int x, int z) {
		return 0;
	}

	public int getMapWidth() {
		return data.width;
	}

	public boolean isMapSquareTraversable(int x, int z) {
		return imap[x][z] == FLOOR;
	}

	protected void createMapsquareSprites() {
		Image img_floor = ImageFunctions.scaleImage(main.getImage("scenery/" + data.floor_tile), Main.SQ_SIZE, Main.SQ_SIZE, main);

		// Create sprites based on map
		for (int y=0 ; y<data.height ; y++) {
			for (int x=0 ; x<data.width ; x++) {
				if (imap[x][y] == WALL) {
					new MapSquare(main, x * Main.SQ_SIZE, y * Main.SQ_SIZE, true, null);
					// Calc correct wall graphic
					main.map[x][y].img = getImageForWalls(imap, walls, x, y);
				} else if (imap[x][y] == FLOOR) {
					new MapSquare(main, x * Main.SQ_SIZE, y * Main.SQ_SIZE, false, img_floor);
				}
			}
		}

	}

	private Image getImageForWalls(int imap[][], Image walls[], int mx, int my) {
		int add = 1;
		int tot=0;
		for (int y=my-1 ; y<=my+1 ; y++) {
			for (int x=mx-1 ; x<=mx+1 ; x++) {
				if (y==my || x == mx) {
					try {
						if (imap[x][y] == WALL) {
							tot = tot + add;
						}
					} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
						// Do nothing
					}

				}
				add = add * 2;
			}
		}
		tot -= 16; // Centre square
		switch (tot) {
		case 0:
			return walls[0];
		case 8: 
			return walls[3];
		case 128:
			return walls[2];
		case 130:
			return walls[6];
		case 32: return walls[1];
		case 2: return walls[4];
		case 40: return walls[5];
		//case 0: return walls[6];
		case 160: return walls[7];
		case 136: return walls[8];
		case 10: return walls[9];
		case 34: return walls[10];
		case 168: return walls[11];
		case 138: return walls[12];
		case 42: return walls[13];
		case 162: return walls[14];
		case 170: return walls[15];
		default: 
			throw new RuntimeException("Unknown walls for " + tot);
		}
	}


}
