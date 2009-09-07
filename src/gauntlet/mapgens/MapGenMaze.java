package gauntlet.mapgens;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import ssmith.lang.Functions;
import gauntlet.LevelDataRecord;
import gauntlet.Main;

public class MapGenMaze extends MapGen {

	private int attempts;

	public MapGenMaze(Main m, LevelDataRecord data) {
		super(m, data);
		attempts = 0;
	}

	@Override
	public void createMap() {
		attempts++;
		
		// Create outer walls
		for (int y=0 ; y<data.height ; y++) {
			for (int x=0 ; x<data.width ; x++) {
				if (x == 0 || y == 0 || x == data.width-1 || y == data.height-1) {
					imap[x][y] = WALL;
				} else {
					imap[x][y] = FLOOR;
				}
			}
		}

		ArrayList rects = new ArrayList();
		rects.add(new Rectangle(1, 1, data.width-2, data.height-2));

		while (rects.size() > 0) {
			Rectangle rect = (Rectangle)rects.get(0);
			rects.remove(0);

			if (rect.width > data.map_param1 && rect.height > data.map_param1) {
				// Draw vertical line
				int rnd_w = Functions.rnd(2, rect.width-3);// Need to have some room!
				int gaps[] = new int[attempts+1];
				for (int i=0 ; i<gaps.length ; i++) {
					gaps[i] = Functions.rnd(rect.y+2, rect.y+rect.height-3);
				}
				start1: for (int y=rect.y ; y<rect.y+rect.height ; y++) {
					for (int i=0 ; i<gaps.length ; i++) {
						if (y == gaps[i]) {
							continue start1;
						}
					}
					imap[rect.x+rnd_w][y] = WALL;
				}

				// Draw horizontal line
				int rnd_h = Functions.rnd(2, rect.height-3);
				for (int i=0 ; i<gaps.length ; i++) {
					gaps[i] = Functions.rnd(rect.x+2, rect.x+rect.width-3);
				}
				start2: for (int x=rect.x ; x<rect.x+rect.width; x++) {
					for (int i=0 ; i<gaps.length ; i++) {
						if (x == gaps[i]) {
							continue start2;
						}
					}
					imap[x][rect.y+rnd_h] = WALL;
				}

				// Add new rooms to list
				rects.add(new Rectangle(rect.x+1, rect.y+1, rnd_w-1, rnd_h-1)); // TL

				rects.add(new Rectangle(rect.x+rnd_w+1, rect.y+1, rect.width - rnd_w-1, rnd_h-1));  //TR

				rects.add(new Rectangle(rect.x+1, rect.y+rnd_h+1, rnd_w-1, rect.height - rnd_h-1)); // BL

				rects.add(new Rectangle(rect.x+rnd_w+1, rect.y+rnd_h+1, rect.width - rnd_w-1, rect.height - rnd_h-1)); //BR

				//super.drawMap();

				//Main.p("Split 1");
			}
		}
	}

	@Override
	public Point getExitPointInMapCoords() {
		return new Point(data.width-3, data.height-3);
	}

	@Override
	public Point getPlayerStartPointInMapCoords() {
		return new Point(1, 1);
	}

}
