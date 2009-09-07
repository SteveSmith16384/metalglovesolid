package gauntlet.mapgens;

import java.awt.Point;

import gauntlet.LevelDataRecord;
import gauntlet.Main;

public class MapGenSolid extends MapGen {
	
	public MapGenSolid(Main m, LevelDataRecord data) {
		super(m, data);
	}

	public void createMap() {
		
	}

	@Override
	public Point getExitPointInMapCoords() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Point getPlayerStartPointInMapCoords() {
		// TODO Auto-generated method stub
		return null;
	}
}
