package gauntlet;

import gauntlet.mapgens.MapGen;
import gauntlet.menus.CharacterSelectionMenu;
import gauntlet.menus.ControlsMenu;
import gauntlet.menus.MainMenu;
import gauntlet.spells.AbstractSpell;
import gauntlet.sprites.IminentEnemy;
import gauntlet.sprites.MonsterGenerator;
import gauntlet.sprites.Player;
import gauntlet.sprites.PlayersBullet;
import gauntlet.sprites.Sprite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import javax.swing.JFrame;
import ssmith.awt.ImageCache;
import ssmith.awt.menu.AbstractMenu;
import ssmith.io.CSVFile;
import ssmith.lang.Functions;

public class Main extends JFrame implements KeyListener {

	public static Font font_large = new Font("Viner Hand ITC", Font.BOLD, 30);
	public static Font font_normal = new Font("Viner Hand ITC", Font.PLAIN, 20);

	public static int ENEMY_PROCESS_DIST;
	private static final String TITLE = "Metal Glove Solid (0.17)";
	public static final int SQ_SIZE = 50;//32;
	private static final long LOOP_DELAY = 35;
	private static final int MAP_SIZE = 80;
	private static final int KEY_SHOOT = KeyEvent.VK_CONTROL;

	private static final long serialVersionUID = 1L;

	private BufferStrategy BS;
	public ArrayList coll_sprites;
	private ArrayList process_sprites;
	private ArrayList non_process_sprites;
	public MapSquare map[][];
	private boolean keys[] = new boolean[256]; // What keys are currently being held down.
	public Player player;
	private ImageCache images;
	private int level;
	public ArrayList sprite_data, level_data;
	private AbstractMenu current_menu;
	private int game_stage = -1; //-1=pre-game, 0=game, 1=post-game
	public Image logo;
	private boolean already_shooting = false;
	private boolean show_spells = false, show_attribs = false;
	private boolean paused = false;
	public MapGen map_gen;
	private String dungeon_name = "Dungeon";
	private Dimension screen;
	public MessageBox msg_box = new MessageBox();

	public Main() {
		super();
		try {
			screen = new Dimension(900, 900);//Toolkit.getDefaultToolkit().getScreenSize();
			ENEMY_PROCESS_DIST = screen.width;
			this.setTitle(TITLE);
			this.addKeyListener(this);
			this.setSize(screen.width, screen.height);
			this.setVisible(true);
			this.createBufferStrategy(2);
			BS = this.getBufferStrategy();
			images = new ImageCache(this);
			this.loadSpriteData();
			this.loadLevelData();
			logo = getImage("pics/mg_logo.gif");
			this.showMainMenu();
			gameLoop();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void showMainMenu() {
		this.game_stage = -1;
		current_menu = new MainMenu(this, 150, 250);
	}

	public int getLevel() {
		return this.level;
	}

	private void loadSpriteData() {
		sprite_data = new ArrayList();

		CSVFile file = new CSVFile();
		try {
			file.openFile("./data/sprites.csv", ",");
			file.readCSVLine(); // Skip heading
			while (file.isEOF() == false) {
				SpriteDataRecord sdf = new SpriteDataRecord();
				String line[] = file.readCSVLine();
				sdf.code = line[0];
				sdf.name = line[1];
				sdf.img_filename = line[2];
				try {
					sdf.width = Integer.parseInt(line[3]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				try {
					sdf.height = Integer.parseInt(line[4]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				sdf.collides = line[5].equalsIgnoreCase("1");
				try {
					sdf.health = Integer.parseInt(line[6]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				try {
					sdf.speed = Integer.parseInt(line[7]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				sdf.enemy_type = line[8];
				try {
					sdf.damage = Integer.parseInt(line[9]);
				} catch (java.lang.NumberFormatException ex) {
					// Do nothing
				}
				sdf.always_process = (line[10].equalsIgnoreCase("1"));
				
				sprite_data.add(sdf);
			}
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public SpriteDataRecord getSpriteData(String code) {
		for (int i=0 ; i<sprite_data.size() ; i++) {
			SpriteDataRecord sdf = (SpriteDataRecord)sprite_data.get(i);
			if (sdf.code.equalsIgnoreCase(code)) {
				return sdf;
			}
		}
		throw new RuntimeException("Sprite code " + code + " not found.");
	}


	private void loadLevelData() {
		level_data = new ArrayList();

		CSVFile file = new CSVFile();
		try {
			file.openFile("./data/levels.csv", ",");
			file.readCSVLine(); // Skip heading
			while (file.isEOF() == false) {
				LevelDataRecord ldf = new LevelDataRecord();
				String line[] = file.readCSVLine();
				try {
					ldf.level_num = Integer.parseInt(line[0]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}

				ldf.name = line[1];
				ldf.map_type = line[2];
				ldf.tileset = line[3];

				try {
					ldf.width = Integer.parseInt(line[4]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				try {
					ldf.height = Integer.parseInt(line[5]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				try {
					ldf.map_param1 = Integer.parseInt(line[6]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				try {
					ldf.num_gens = Integer.parseInt(line[7]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				ldf.monster_gen_type = line[8];

				try {
					ldf.num_food = Integer.parseInt(line[9]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				ldf.floor_tile = line[10];
				try {
					ldf.num_gold = Integer.parseInt(line[11]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}
				ldf.special_enemies = line[12];
				try {
					ldf.num_moving_walls = Integer.parseInt(line[13]);
				} catch (java.lang.NumberFormatException ex) {
					ex.printStackTrace();
				}

				level_data.add(ldf);
			}
			file.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public LevelDataRecord getLevelData(int level) {
		while (level > level_data.size()) {
			level -= this.level_data.size();
		}
		for (int i=0 ; i<level_data.size() ; i++) {
			LevelDataRecord sdf = (LevelDataRecord)level_data.get(i);
			if (sdf.level_num == level) {
				return sdf;
			}
		}
		throw new RuntimeException("Level data for level " + level + " not found.");
	}


	public void selectCharacter() {
		this.current_menu = new CharacterSelectionMenu(this, 150, 250);
	}

	public void showControls() {
		this.current_menu = new ControlsMenu(this, 150, 250);
	}

	public void characterSelected(String name) {
		if (name.equalsIgnoreCase("Warrior")) {
			new Player(this, 34, 48, 100, 4, "players/haldric-ii-sword-1", 1, 300, 35, 43, "items/flame-sword.png", 1, .3f);
		} else if (name.equalsIgnoreCase("Wizard")) {
			new Player(this, 40, 48, 80, 3, "players/elder-mage-defend", 2, 250, 32, 32, "items/dark-magic-2.png", 4, .1f);
		} else if (name.equalsIgnoreCase("Elf")) {
			new Player(this, 34, 45, 80, 5, "players/archer", 1, 350, 32, 32, "items/bone-ne.png", 2, .2f);
		} else if (name.equalsIgnoreCase("Dwarf")) {
			new Player(this, 39, 32, 110, 3, "players/fighter", 1, 300, 32, 32, "items/axe.png", 1, .4f);
		} else {
			throw new RuntimeException("Unknown character selected:" + name);
		}
		this.nextLevel(1);
	}

	public void nextLevel(int no) {
		game_stage = 0;
		this.current_menu = null;
		level = no;
		process_sprites = new ArrayList();
		non_process_sprites = new ArrayList();

		coll_sprites = new ArrayList();

		this.process_sprites.add(player); // Add the player since we've cleared the lists
		this.coll_sprites.add(player); // Add the player since we've cleared the lists
		PlayersBullet.total = 0;

		this.repaint();

		map = new MapSquare[MAP_SIZE][MAP_SIZE];
		LevelDataRecord ldf = this.getLevelData(level);
		dungeon_name = ldf.name;
		map_gen = ldf.getMapGen(this);
		map_gen.start();
	}

	public void setMapSquare(int mx, int my, MapSquare sq) {
		map[mx][my] = sq;
	}

	public void gameLoop() {
		while (this.isVisible()) {
			long start = System.currentTimeMillis();

			Graphics g = BS.getDrawGraphics();
			g.setFont(font_normal);
			g.setColor(Color.black);
			g.fillRect(0, 0, screen.width, screen.height);

			if (map_gen != null && map_gen.isAlive()) {
				g.setFont(font_large);
				g.setColor(Color.white);
				g.drawString("Entering " + this.dungeon_name + "...", screen.width/4, screen.height/2);
			} else {
				if (this.current_menu != null) {
					if (keys[KeyEvent.VK_UP]) {
						keys[KeyEvent.VK_UP] = false;
						this.current_menu.prevItem();
					} else if (keys[KeyEvent.VK_DOWN]) {
						keys[KeyEvent.VK_DOWN] = false;
						this.current_menu.nextItem();
					} else if (keys[KeyEvent.VK_ENTER]) {
						keys[KeyEvent.VK_ENTER] = false;
						this.current_menu.selectCurrentItem();
					}
				} else if (player != null) {
					if (game_stage == 0) {
						player.off_x = 0;
						player.off_y = 0;

						if (!paused) {
							if (keys[KeyEvent.VK_UP]) {
								player.off_y -= player.speed;
							} else if (keys[KeyEvent.VK_DOWN]) {
								player.off_y += player.speed;
							}
							if (keys[KeyEvent.VK_LEFT]) {
								player.off_x -= player.speed;
							} else if (keys[KeyEvent.VK_RIGHT]) {
								player.off_x += player.speed;
							}
							if (keys[KEY_SHOOT]) {
								if (already_shooting == false && (player.off_x != 0 || player.off_y != 0)) {
									already_shooting = true;
									player.lockShootDirection();
								}
								player.shoot();
							} else {
								already_shooting = false;
								player.lockShootDirection();
							}
							for (int i=0 ; i<10 ; i++) {
								if (keys[KeyEvent.VK_0+i]) {
									if (this.player.spells.size() > i) {
										this.cast(i);
										this.player.spells.remove(i);
										keys[KeyEvent.VK_0+i] = false; // Stop us casting loads
									}
								}
							}
						}
						if (keys[KeyEvent.VK_A]) {
							this.show_attribs = true;
						} else {
							this.show_attribs = false;
						}
						if (keys[KeyEvent.VK_N]) {
							this.level++;
							this.nextLevel(level);
						}
						if (keys[KeyEvent.VK_P]) {
							this.paused = !this.paused;
							keys[KeyEvent.VK_P] = false;
						}
						if (keys[KeyEvent.VK_S]) {
							this.show_spells = true;
						} else {
							this.show_spells = false;
						}
					}

					int px = player.getX(); 
					int py = player.getY();

					// Draw back map first
					MapSquare sq;

					int x_pos = (int)player.getX() - (screen.width/2);
					int y_pos = (int)player.getY() - (screen.height/2);

					int sx = x_pos / Main.SQ_SIZE;
					int sy = y_pos / Main.SQ_SIZE;
					int ex = (x_pos+screen.width) / Main.SQ_SIZE;
					int ey = (y_pos+screen.height) / Main.SQ_SIZE;

					for (int y = sy; y <= ey; y++) {
						for (int x = sx; x <= ex; x++) {
							try {
								if (map[x][y] != null) {
									sq = map[x][y];
									if (sq.img != null) {
										int x2 = (int)(sq.getX() - x_pos);
										int y2 = (int)(sq.getY() - y_pos);
										sq.draw(g, x2, y2);
									}
								}
							} catch (java.lang.ArrayIndexOutOfBoundsException ex2) {
								// Do nothing
							}

						}
					}

					// Process the sprites
					if (!paused) {
						for (int i=0 ; i<this.process_sprites.size() ; i++) {
							Sprite s = (Sprite)process_sprites.get(i);
							s.process();
							if (s.to_be_removed) {
								remove(s);
								i--;
							}
						}
					} else {
						g.setFont(font_large);
						g.setColor(Color.white);
						g.drawString("Paused", screen.width/2, screen.height/2);
					}

					int half_width = screen.width/2;
					int half_height = screen.height/2;
					for (int i=0 ; i<this.non_process_sprites.size() ; i++) {
						Sprite s = (Sprite)non_process_sprites.get(i);
						int coord_x = half_width - px + s.getX();
						int coord_y = half_height - py + s.getY();
						s.draw(g, coord_x, coord_y);
					}

					for (int i=0 ; i<this.process_sprites.size() ; i++) {
						Sprite s = (Sprite)process_sprites.get(i);
						int coord_x = half_width - px + s.getX();
						int coord_y = half_height - py + s.getY();
						s.draw(g, coord_x, coord_y);
					}

					g.setColor(Color.white);
					g.drawString("Health: " + player.getHealth(), 40,60);
					g.drawString("Keys: " + player.keys, 40,85);
					g.drawString("Score: " + player.score, 40,110);

					if (show_spells) {
						if (player.spells.size() > 0) {
							AbstractSpell spell;
							for (int i=0 ; i<player.spells.size(); i++) {
								spell = (AbstractSpell)player.spells.get(i);
								g.drawString(i + "-" + spell.getName(), 500, 60 + (i*25));
							}
						} else {
							g.drawString("You have no spells", 500, 60);
						}
					} else if (show_attribs) {
						g.drawString("Speed: " + player.speed, 500, 60);
						g.drawString("Magic Power: " + player.magic_power, 500, 85);
						g.drawString("Protection: " + (int)(player.protection*100), 500, 110);
						g.drawString("Combat Power: " + player.bullet_dam, 500, 135);
						g.drawString("Combat Range: " + player.bullet_dist, 500, 160);
						g.drawString("Combat Skill: " + player.allowed_bullets, 500, 185);
					}
				}

				g.setColor(Color.white);
				if (game_stage == 0) {
					g.drawString("Level: " + level, 40,135);
					msg_box.paint(g, 20, screen.height-50);
				} else if (game_stage == 1) {
					g.setFont(font_large);
					g.drawString("Press R to Restart", screen.width/2, screen.height/2);
					if (keys[KeyEvent.VK_R]) { // Restart game
						this.showMainMenu();
					}
				}

				if (this.current_menu != null) {
					this.current_menu.draw(g);
				}
			}

			BS.show();

			long wait = LOOP_DELAY - System.currentTimeMillis() + start;
			Functions.delay(wait);
		}

		System.exit(0);
	}

	private void cast(int spellno) {
		AbstractSpell spell = (AbstractSpell)this.player.spells.get(spellno);
		spell.cast();
	}

	public void exit() {
		this.setVisible(false);
	}

	public void setGameStage(int i) {
		this.game_stage = i;
	}

	public boolean isAreaClear(int x, int y, int w, int h) {
		IminentEnemy im = new IminentEnemy(this, x, y, w, h);
		boolean result = true;
		try {
			ArrayList arr = coll_sprites;
			Iterator it = arr.iterator();
			while (it.hasNext()) {
				Sprite s = (Sprite)it.next();
				if (s != im) {
					//if (s.to_be_removed == false) {
					if (s instanceof MonsterGenerator == false) {
						if (s.hasCollidedWith(im)) {
							result = false;
							break;
						}
					}
				}
			}
		} catch (java.util.ConcurrentModificationException ex) {
			ex.printStackTrace();
			result = false;
		}
		remove(im);
		return result;
	}

	public void keyPressed(KeyEvent e) {
		try {
			keys[e.getKeyCode()] = true;
			//p("Key p:" + e.getKeyCode());
		} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			// Do nothing
		}
	}

	public void keyReleased(KeyEvent e) {
		try {
			keys[e.getKeyCode()] = false;
			if (e.getKeyCode() == KEY_SHOOT) {
				this.already_shooting = false;
			}
		} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
			// Do nothing
		}
	}

	public void keyTyped(KeyEvent e) {
		//p("Key t:" + e.getKeyCode());
	}

	public Image getImage(String filename) {
		return this.images.getImage("./data/images/" + filename);
	}

	public static void p(String s) {
		System.out.println(s);
	}

	public void addSprite(Sprite s) { 
		if (coll_sprites != null) {
			if (s.isCollideable()) {
				//addSpriteToCollisionList(this);
				this.coll_sprites.add(s);
			}
			//if (!coll_matrix_only) {
			if (s instanceof MapSquare) {
				setMapSquare(s.getX()/Main.SQ_SIZE, s.getY()/Main.SQ_SIZE, (MapSquare)s);
			} else if (s.process) {
				process_sprites.add(s);
			} else {
				non_process_sprites.add(s);
			}
			//}
		}
	}


	public void remove(Sprite s) {
		if (s.to_be_removed = false) {
			throw new RuntimeException("Trying to remove a sprite directly!");
		}
		if (s.isCollideable()) {
			this.coll_sprites.remove(s);
		}
		if (s.process) {
			process_sprites.remove(s);
		} else {
			non_process_sprites.remove(s);
		}
	}


	public ArrayList getPotentialColliders(Sprite s) {
		return this.coll_sprites;
	}


//	----------------------------

	public static void main(String args[]) {
		new Main();
	}

}
