/*
 * Created on 31-Aug-2005
 * 
 * This is the A* path-finding algorythm.  
 * Written by Stephen Smith (stephen.carlylesmith@googlemail.com).
 * Feel free to use this in any programs of any kind.  Credit is optional but
 * would be nice!
 * 
 * 
 * To use it, implement the IAStarMapInterface in your "map" class.  Then in
 * your code, write something like:-
 * 
 * AStar my_astar = new AStar(mymapclass);
 * 
 * my_astar.findpath(start_x, start_y, end_x, end_y, b_run_in_thread);
 * 
 * If you're running it in its own thread (i.e. b_run_in_thread == true in the
 * above example) then you can check whether its finished with something like..:-
 * 
 * while (looping) {
 * 		if (my_astar.finishedFindingPath()) {
 * 			// Got a path
 * 			WayPoints route = my_astar.getRoute();
 * 		}
 * 		..
 * 		..
 * }
 * 
 * Otherwise, if its running in the same thread (i.e. b_run_in_thread == false) 
 * then on the next line write:-
 * 
 * WayPoints route = my_astar.getRoute();
 * 
 * The route is return in the class WayPoints, which is basically an ArrayList 
 * of Points.
 * 
 *
 */
package ssmith.astar;

import java.awt.Point;

/**
 * @author stephen smith
 *
 */
public class AStar extends Thread {

	private static final boolean SHOW_MAP = false;

	private IAStarMapInterface map_interface;
	private Node map[][];
	private PriorityList open;
	private boolean[][] checked;
	private int start_x, start_z, end_x, end_z, max_dist;
	private volatile boolean finding_path = false;
	private volatile boolean failed;
	private volatile WayPoints route = new WayPoints();
	public volatile static int tot_threads=0;
	private boolean can_timeout = false;
	private long max_dur, timeout_time;

	// Debugging vars
	private String[][] strmap;

	public AStar(IAStarMapInterface intface, long max_duration) {
		this(intface);
		can_timeout = true;
		max_dur = max_duration;
	}

	public AStar(IAStarMapInterface intface) {
		super();
		this.setDaemon(true);
		this.map_interface = intface;

		if (SHOW_MAP) {
			int w = intface.getMapWidth();
			int h = intface.getMapHeight();
			strmap = new String[w][h];
		}
	}

	public void findPath(int start_x, int start_z, int targ_x, int targ_z, boolean thread) {
		findPath(start_x, start_z, targ_x, targ_z, -1, thread);

	}

	/**
	 * This returns an empty WayPoints if a route cannot be found.
	 * @param start_x
	 * @param start_z
	 * @param targ_x
	 * @param targ_z
	 * @param max_dist - For avoiding grenades.
	 * @return
	 */
	public void findPath(int start_x, int start_z, int targ_x, int targ_z, int max_dist, boolean thread) {
		if (this.can_timeout) {
			timeout_time = System.currentTimeMillis() + max_dur; 
		}

		if (finding_path) {
			System.err.println("Trying to find path concurrently!");
		}
		//System.out.println("Finding path from " + start_x + "," + start_z + " to " + targ_x+ "," + targ_z + ".");
		route = new WayPoints();
		this.finding_path = true;
		this.start_x = start_x;
		this.start_z = start_z;
		this.end_x = targ_x;
		this.end_z = targ_z;
		this.max_dist = max_dist;
		if (thread) {
			start();
		} else {
			run();
		}
	}

	public void run() {
		failed = false;
		tot_threads++;
		//System.out.println("Tot A* threads:" + this.tot_threads);

		int w = map_interface.getMapHeight();
		int h = map_interface.getMapWidth();

		map = new Node[w][h]; // Reset the map
		if (SHOW_MAP) {
			for(int z=0 ; z< h ; z++) {
				for(int x=0 ; x<w ; x++) {
					strmap[x][z] = "-";
				}
			}
			strmap[start_x][start_z] = "S";
			strmap[end_x][end_z] = "F";
		}

		open = new PriorityList();
		checked = new boolean[w][h];


		// Now find the path
		Node node = new Node(start_x, start_z);
		map[start_x][start_z] = node;
		node.setHeuristic(null, end_x, end_z, 1);
		open.add(node);
		//System.out.println("Starting to find path.");
		while (node.x != end_x || node.z != end_z) {
			if (can_timeout) {
				if (System.currentTimeMillis() > this.timeout_time) {
					failed = true;
					return;
				}
			}
			//System.out.println("Node: " + node.x + "," + node.z);
			open.remove(node);
			checked[node.x][node.z] = true;
			this.getAdjacentSquares(0, -1, node, end_x, end_z, max_dist);
			this.getAdjacentSquares(1, 0, node, end_x, end_z, max_dist);
			this.getAdjacentSquares(0, 1, node, end_x, end_z, max_dist);
			this.getAdjacentSquares(-1, 0, node, end_x, end_z, max_dist);

			if (open.size() > 0) {
				// Get th next square and go from there.
				node = (Node)open.getFirst();
				//System.out.println("Next node: " + node.x + "," + node.z);
			} else {
				// Cannot get to the square!  Move as far as we can.
				failed = true;
				//System.out.println("Cannot get from " + start_x + "," + start_z + " to " + end_x + "," + end_z);
				route = new WayPoints();
				finding_path = false;
				// break;
			}
		}

		// We have finished, so iterate through the parents.
		while (node.getParent() != null) {
			route.add(0, new Point(node.x, node.z));
			node = node.getParent();
			if (SHOW_MAP) {
				strmap[node.x][node.z] = "X";
			}
		}
		route.add(0, new Point(start_x, start_z)); // add the start pos just in case

		if (SHOW_MAP) {
			strmap[start_x][start_z] = "S";
			strmap[end_x][end_z] = "F";
			showMap();
		}

		this.finding_path = false;
		if (failed) {
			if (SHOW_MAP) {
				showMap();
			}
			//throw new CannotFindPathException();
		}
		tot_threads--;
		//System.out.println("Finished finding path. (threads:" + this.tot_threads + ")");
	}

	public boolean finishedFindingPath() {
		return this.finding_path == false;
	}

	public boolean failedFindingPath() {
		return this.failed == false;
	}

	public WayPoints getRoute() {
		return this.route;
	}

	public void showMap() {
		try {
			int w = map_interface.getMapWidth();
			int h = map_interface.getMapHeight();

			for(int z=0 ; z< h ; z++) {
				for(int x=0 ; x<w ; x++) {
					System.out.print(strmap[x][z]);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void getAdjacentSquares(int off_x, int off_z, Node prnt, int targ_x, int targ_z, int max_dist) {
		int x = prnt.x + off_x;
		int z = prnt.z + off_z;
		try {
			// Check if we're checking the distance
			if (max_dist > 0) {
				if (prnt.getDistFromStart() >= max_dist) {
					return;
				}
			}
			if (checked[x][z] == false) {
				if (this.map_interface.isMapSquareTraversable(x, z) || (x == targ_x && z == targ_z)) {
					Node n = new Node(x, z);
					float dist = this.map_interface.getMapSquareDifficulty(x, z);
					n.setHeuristic(prnt, targ_x, targ_z, dist);
					// Check if there's a node already in open at the same co-ords
					boolean found = false;
					if (map[x][z] != null) {
						Node other = map[x][z];
						if (other.getHeuristic() <= n.getHeuristic()) {
							// Its a better node
							found = true;
						} else {
							// Its worse so remove this one.
							map[x][z] = null;
							open.remove(other);
						}
					}
					if (found == false) {
						map[x][z] = n;
						open.add(n);
						if (SHOW_MAP) {
							if (strmap[x][z].equalsIgnoreCase("-")) {
								strmap[x][z] = "+"; // Mark it as checked
							}
						}
					}
				} else {
					if (SHOW_MAP) {
						strmap[x][z] = "B";
					}
				}
			} else {
				if (SHOW_MAP) {
					if (strmap[x][z].equalsIgnoreCase("B") == false) {
						strmap[x][z] = "W";
					}
				}
			}
		} catch (ArrayIndexOutOfBoundsException ex) {
			//ex.printStackTrace();
		}
	}

}
