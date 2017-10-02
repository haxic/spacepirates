package particles;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL31;

import entities.Camera;
import models.RawModel;
import utils.InsertionSort;
import utils.Loader;
import utils.Maths;

public class ParticleRenderer {

	private static final float[] VERTICES = { -0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f };
	private static final int MAX_INSTANCES = 50000;
	private static final int INSTANCE_DATA_LENGTH = 21;
	
	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
	
	private RawModel quad;
	private ParticleShader shader;
	
	private Loader loader;
	private int vboID;
	private int pointer = 0;

	protected ParticleRenderer(Loader loader, Matrix4f projectionMatrix) {
		this.loader = loader;
		this.vboID = loader.createEmptyVBO(INSTANCE_DATA_LENGTH * MAX_INSTANCES);
		quad = loader.loadToVAO(VERTICES, 2);
		// Modelview matrix
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 1, 4, INSTANCE_DATA_LENGTH, 0);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 2, 4, INSTANCE_DATA_LENGTH, 4);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 3, 4, INSTANCE_DATA_LENGTH, 8);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 4, 4, INSTANCE_DATA_LENGTH, 12);
		// Texture offsets
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 5, 4, INSTANCE_DATA_LENGTH, 16);
		// Blend factors
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 6, 1, INSTANCE_DATA_LENGTH, 20);
		
		shader = new ParticleShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	protected void render(Map<ParticleTexture, List<Particle>> particles, Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		prepare();
		for (ParticleTexture particleTexture : particles.keySet()) {
			bindTexture(particleTexture);
			List<Particle> particleList = particles.get(particleTexture);
			pointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCE_DATA_LENGTH];
			for (Particle particle : particleList) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix, vboData);
				updateTextureCoordinateData(particle, vboData);
			}
//			InsertionSort.sortHighToLow(particleList);
			loader.updateVBO(vboID, vboData, buffer);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCount(), particleList.size());
		}
		finishRendering();
	}
	
	private void bindTexture(ParticleTexture particleTexture) {
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, particleTexture.getTextureID());
		shader.loadAtlasSize(particleTexture.getAtlasSize());
	}

	private static Matrix4f tempMatrix = new Matrix4f();

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData) {
		Matrix4f modelMatrix = tempMatrix.identity();
		modelMatrix.translate(position);
		modelMatrix.m00(viewMatrix.m00());
		modelMatrix.m01(viewMatrix.m10());
		modelMatrix.m02(viewMatrix.m20());
		modelMatrix.m10(viewMatrix.m01());
		modelMatrix.m11(viewMatrix.m11());
		modelMatrix.m12(viewMatrix.m21());
		modelMatrix.m20(viewMatrix.m02());
		modelMatrix.m21(viewMatrix.m12());
		modelMatrix.m22(viewMatrix.m22());
		Matrix4f modelViewMatrix = viewMatrix.mul(modelMatrix, tempMatrix);
		modelViewMatrix.rotateZ((float) Math.toRadians(rotation));
		modelViewMatrix.scale(scale);
		storeMatrixData(modelViewMatrix, vboData);
	}
	
	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[pointer++] = matrix.m00();
		vboData[pointer++] = matrix.m01();
		vboData[pointer++] = matrix.m02();
		vboData[pointer++] = matrix.m03();
		vboData[pointer++] = matrix.m10();
		vboData[pointer++] = matrix.m11();
		vboData[pointer++] = matrix.m12();
		vboData[pointer++] = matrix.m13();
		vboData[pointer++] = matrix.m20();
		vboData[pointer++] = matrix.m21();
		vboData[pointer++] = matrix.m22();
		vboData[pointer++] = matrix.m23();
		vboData[pointer++] = matrix.m30();
		vboData[pointer++] = matrix.m31();
		vboData[pointer++] = matrix.m32();
		vboData[pointer++] = matrix.m33();
	}
	
	private void updateTextureCoordinateData(Particle particle, float[] data) {
		data[pointer++] = particle.getTextureOffset1().x;
		data[pointer++] = particle.getTextureOffset1().y;
		data[pointer++] = particle.getTextureOffset2().x;
		data[pointer++] = particle.getTextureOffset2().y;
		data[pointer++] = particle.getBlendFactor();
	}

	protected void cleanUp() {
		shader.cleanUp();
	}

	private void prepare() {
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);
		// Enable transparency.
		GL11.glEnable(GL11.GL_BLEND);
		// Render latest on top.
//		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		// Additive blending. Good for fire etc, bad for solid particles.
		 GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
		GL11.glDepthMask(false);
	}

	private void finishRendering() {
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
