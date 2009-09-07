package gauntlet.mapgens;

import gauntlet.LevelDataRecord;
import gauntlet.Main;
import java.awt.Point;
import java.util.ArrayList;
import ssmith.lang.Functions;

public class MapGenCorridors extends MapGen {

	private Point start, end;

	public MapGenCorridors(Main m, LevelDataRecord data) {
		super(m, data);
	}

	public void createMap() {
		ArrayList room_points = new ArrayList();
		// Create map
		for (int i=0 ; i<data.map_param1 ; i++) {
			int mx = Functions.rnd(2, data.width-12);
			int my = Functions.rnd(2, data.height-12);
			int mw = Functions.rnd(4, 10);
			int mh = Functions.rnd(4, 10);
			if (createRoomByTopLeft(mx, my, mw, mh) == false) {
				i--;
			} else {
				//Main.p("Room added at " + mx + "," + my);
				room_points.add(new Point(mx, my));
				if (i == 0) {
					start = new Point(mx+2, my+2);
				} else if (i == data.map_param1-1) {
					end = new Point(mx+2, my+2);
				}
			}
		}
		for (int i=0 ; i<room_points.size()-1 ; i++) {
			Point p = (Point)room_points.get(i);
			Point p2 = (Point)room_points.get(i+1);
			this.addCorridor(p.x+2, p.y+2, p2.x+2, p2.y+2, Functions.rnd(2, 4));

		}

		addWalls(); // Adds walls around the floor

	}

	protected boolean createRoomByCentre(int centre_x, int centre_y, int l, int d) {
		return this.createRoomByTopLeft(centre_x - (l/2), centre_y - (d/2), l, d);
	}


	protected boolean createRoomByTopLeft(int x, int y, int l, int d) {
		// First check the area is clear
		for (int y2=y ; y2<=y+d ; y2++) {
			for (int x2=x ; x2<=x+l ; x2++) {
				if (imap[x2][y2] != NOT_SET) {
					return false;
				}
			}
		}

		for (int y2=y ; y2<=y+d ; y2++) {
			for (int x2=x ; x2<=x+l ; x2++) {
				imap[x2][y2] = FLOOR;
			}
		}
		return true;
	}


	protected void addCorridor(int sx, int sy, int ex, int ey, int hsize) {
		// Make sure the values are right way round
		if (ex < sx) {
			int t = sx;
			sx = ex;
			ex = t;
		}
		if (ey < sy) {
			int t = sy;
			sy = ey;
			ey = t;
		}

		int difx = Functions.sign(ex-sx);
		int dify = Functions.sign(ey-sy);

		// Across
		if (difx != 0) {
			int y = sy;
			for (int x=sx-hsize ; x<ex+hsize ; x += difx) {
				for (int i=-hsize+1 ; i<hsize ; i++) {
					try {
						imap[x][y+i] = FLOOR;
					} catch (java.lang.ArrayIndexOutOfBoundsException ex2) {
						// Do nothing
					}
				}
			}
		}
		// Down
		if (dify != 0) {
			int x = ex;
			for (int y=sy-hsize+1 ; y<ey+hsize ; y += dify) {
				for (int i=-hsize+1 ; i<hsize ; i++) {
					imap[x+i][y] = FLOOR;
				}
			}
		}

	}


	private void addWalls() {
		for (int y=0 ; y<data.height ; y++) {
			for (int x=0 ; x<data.width ; x++) {
				if (imap[x][y] == NOT_SET) {
					seeIfWallNeedsAdding(x, y);
				}
			}
		}
	}


	private void seeIfWallNeedsAdding(int mx, int my) {
		for (int y=my-1 ; y<=my+1 ; y++) {
			for (int x=mx-1 ; x<=mx+1 ; x++) {
				try {
					if (imap[x][y] == FLOOR) {
						imap[mx][my] = WALL;
						return;
					}
				} catch (java.lang.ArrayIndexOutOfBoundsException ex) {
					// Do nothing
				}
			}
		}
	}

	@Override
	public Point getExitPointInMapCoords() {
		return end;
	}

	@Override
	public Point getPlayerStartPointInMapCoords() {
		return start;
	}

}
