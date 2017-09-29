package entities;

import org.joml.Vector2f;
import org.joml.Vector3f;

import models.TexturedModel;

public class Entity {
	private TexturedModel model;
	private Vector3f position;
	private Vector3f rotation;
	private Vector3f scale;
	private int textureIndex = 0;
	private Vector2f textureOffset;

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale) {
		super();
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		textureOffset = new Vector2f(calculateTextureXOffset(), calculateTextureYOffset());
	}

	public Entity(TexturedModel model, Vector3f position, Vector3f rotation, Vector3f scale, int textureIndex) {
		super();
		this.model = model;
		this.position = position;
		this.rotation = rotation;
		this.scale = scale;
		this.textureIndex = textureIndex;
		textureOffset = new Vector2f(calculateTextureXOffset(), calculateTextureYOffset());
	}

	private float calculateTextureXOffset() {
		int column = textureIndex % model.getModelTexture().getAtlasSize();
		return (float) column / (float) model.getModelTexture().getAtlasSize();
	}

	private float calculateTextureYOffset() {
		int row = textureIndex / model.getModelTexture().getAtlasSize();
		return (float) row / (float) model.getModelTexture().getAtlasSize();
	}

	public void moveBy(Vector3f velocity) {
		this.position.add(velocity);
	}

	public void rotateBy(Vector3f rotation) {
		this.rotation.add(rotation);
	}

	public TexturedModel getModel() {
		return model;
	}

	public void setModel(TexturedModel model) {
		this.model = model;
	}

	public Vector3f getPosition() {
		return position;
	}

	public void setPosition(Vector3f position) {
		this.position = position;
	}

	public Vector3f getRotation() {
		return rotation;
	}

	public void setRotation(Vector3f rotation) {
		this.rotation = rotation;
	}

	public Vector3f getScale() {
		return scale;
	}

	public void setScale(Vector3f scale) {
		this.scale = scale;
	}

	public Vector2f getTextureOffset() {
		return textureOffset;
	}

	public int getTextureIndex() {
		return textureIndex;
	}

	public void setTextureIndex(int textureIndex) {
		this.textureIndex = textureIndex;
		textureOffset = new Vector2f(calculateTextureXOffset(), calculateTextureYOffset());
	}
}
