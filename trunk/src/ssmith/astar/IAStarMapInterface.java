/*
 * Created on 31-Aug-2005
 *
 */
package ssmith.astar;

/**
 * @author stephen smith
 */
public interface IAStarMapInterface {
	
	int getMapWidth();

	int getMapHeight();
	
	boolean isMapSquareTraversable(int x, int z);

	float getMapSquareDifficulty(int x, int z);

}
