package gauntlet;

import gauntlet.mapgens.MapGen;
import gauntlet.mapgens.MapGenCorridors;
import gauntlet.mapgens.MapGenMaze;
import gauntlet.mapgens.MapGenOpenArea;

public class LevelDataRecord {
	
	public String name, map_type, tileset, monster_gen_type, floor_tile, special_enemies = "";
	public int level_num, map_param1, width, height, num_gens, num_food, num_gold, num_moving_walls;
	
	public MapGen getMapGen(Main m) {
		if (map_type.equalsIgnoreCase("Corridor")) {
			return new MapGenCorridors(m, this);
		} else if (map_type.equalsIgnoreCase("maze")) {
			return new MapGenMaze(m, this);
		} else if (map_type.equalsIgnoreCase("OpenArea")) {
			return new MapGenOpenArea(m, this);
		} else {
			throw new RuntimeException("Unknown map type: " + map_type);
		}
	}

}
