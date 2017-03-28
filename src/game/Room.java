package game;

import java.util.ArrayList;
import java.util.List;

public class Room {
	public List<Unit> units;
	public int[][] map;
	public int width = 20;
	public int height = 20;
	
	public Room() {
		units = new ArrayList<Unit>();
		map = new int[width][height];
	}
}
