package game;

import engine.V3;

public class Unit {
	public V3 size = new V3(10, 10, 0);
	public V3 sizeHalf = new V3(5, 5, 0);
	public V3 position = new V3();
	public V3 velocity = new V3();
	public boolean onGround = false;
	public String name;
	public int id;

	public Unit(int id, String name, V3 position) {
		this.id = id;
		this.name = name;
		this.position = position;
	}
}
