package particles;

import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera;
import renderEngine.DisplayManager;

public class Particle {
	private Vector3f position;
	private Vector3f velocity;
	private float lifeLength;
	private float rotation;
	private float scale;
	
	
	private float elapsedTime;
	private float distanceToCamera;

	private ParticleTexture particleTexture;

	private Vector2f textureOffset1 = new Vector2f();
	private Vector2f textureOffset2 = new Vector2f();
	private float blendFactor;

	// Reuse this vector instead of creating a new one for every calculation.
	private static Vector3f tempVector = new Vector3f();

	public Particle(Vector3f position, Vector3f velocity, float lifeLength, float rotation, float scale, ParticleTexture particleTexture) {
		super();
		this.position = position;
		this.velocity = velocity;
		this.lifeLength = lifeLength;
		this.rotation = rotation;
		this.scale = scale;
		this.particleTexture = particleTexture;
		ParticleManager.addParticle(this);
	}

	protected boolean update(Camera camera) {
		position.add(velocity.mul(DisplayManager.getDelta(), tempVector));
		elapsedTime += DisplayManager.getDelta();
		distanceToCamera = camera.getPosition().sub(position, tempVector).lengthSquared();
		updateTextureCoordinate();
		return elapsedTime < lifeLength;
	}

	// Calculate what texture coordinate in the sub-texture of the particle texture that should be used depending on elapsed time.
	private void updateTextureCoordinate() {
		float lifeFactor = elapsedTime / lifeLength;
		int stageCount = particleTexture.getAtlasSize() * particleTexture.getAtlasSize();
		float atlasProgression = lifeFactor * stageCount;
		int index1 = (int) Math.floor(atlasProgression);
		int index2 = index1 < stageCount - 1 ? index1 + 1 : index1;
		blendFactor = atlasProgression % 1;
		setTextureOffset(textureOffset1, index1);
		setTextureOffset(textureOffset2, index2);
	}
	
	private void setTextureOffset(Vector2f offset, int index) {
		int column = index % particleTexture.getAtlasSize();
		int row = index / particleTexture.getAtlasSize();
		offset.x = (float) column / particleTexture.getAtlasSize();
		offset.y = (float) row / particleTexture.getAtlasSize();
	}
	
	public float getDistanceToCamera() {
		return distanceToCamera;
	}


	public Vector3f getPosition() {
		return position;
	}

	public float getRotation() {
		return rotation;
	}

	public float getScale() {
		return scale;
	}

	public ParticleTexture getParticleTexture() {
		return particleTexture;
	}

	public Vector2f getTextureOffset1() {
		return textureOffset1;
	}

	public Vector2f getTextureOffset2() {
		return textureOffset2;
	}

	public float getBlendFactor() {
		return blendFactor;
	}

	public float getRows() {
		return 0;
	}
}
