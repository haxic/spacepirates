package particles;

import org.joml.Vector3f;

import renderEngine.DisplayManager;

public class ParticleSystem {
	private float pps;
	private float speed;
	private float lifeLength;

	private ParticleTexture particleTexture;

	public ParticleSystem(float pps, float speed, float lifeLength, ParticleTexture particleTexture) {
		this.pps = pps;
		this.speed = speed;
		this.lifeLength = lifeLength;
		this.particleTexture = particleTexture;
	}

	public void generateParticles(Vector3f systemCenter) {
		float delta = DisplayManager.getDelta();
		float particlesToCreate = pps * delta;
		int count = (int) Math.floor(particlesToCreate);
		float partialParticle = particlesToCreate % 1;
		for (int i = 0; i < count; i++) {
			emitParticle(systemCenter);
		}
		if (Math.random() < partialParticle) {
			emitParticle(systemCenter);
		}
	}

	private void emitParticle(Vector3f center) {
		float dirX = (float) Math.random() * 2f - 1f;
		float dirZ = (float) Math.random() * 2f - 1f;
		float dirY = (float) Math.random() * 2f - 1f;
		Vector3f velocity = new Vector3f(dirX, dirY, dirZ);
		velocity.normalize();
		velocity.mul(speed);
		new Particle(new Vector3f(center), velocity, lifeLength, (float) (Math.random() * 360), 1, particleTexture);
	}
}
