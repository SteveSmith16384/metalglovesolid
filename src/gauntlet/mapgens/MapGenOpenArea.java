package gauntlet.mapgens;

import java.awt.Point;

import ssmith.lang.Functions;

import gauntlet.LevelDataRecord;
import gauntlet.Main;

public class MapGenOpenArea extends MapGen {
	
	public MapGenOpenArea(Main m, LevelDataRecord data) {
		super(m, data);
	}

	@Override
	public void createMap() {
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

		for (int i=0 ; i<data.map_param1 ; i++) {
			int x = Functions.rnd(2, data.width-2);
			int y = Functions.rnd(2, data.height-2);
			imap[x][y] = WALL;
		}
	}

	@Override
	public Point getExitPointInMapCoords() {
		int x = Functions.rnd(2, data.width/3);
		int y = Functions.rnd(2, data.height/3);
		return new Point(x, y);
	}

	@Override
	public Point getPlayerStartPointInMapCoords() {
		int x = Functions.rnd(data.width/2, data.width-2);
		int y = Functions.rnd(data.height/2, data.height-2);
		return new Point(x, y);
	}

}
