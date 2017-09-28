package entities;

import org.joml.Vector3f;

public class Light {
	public Vector3f position;
	public Vector3f color;

	public Light(Vector3f position, Vector3f color) {
		this.position = position;
		this.color = color;
	}
}
