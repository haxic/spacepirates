package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;

public class MasterRenderer {

	private static final float FOV = 70;
	private static final float NEAR_PLANE = 0.1f;
	private static final float FAR_PLANE = 1000f;
	Matrix4f projectionMatrix;

	private StaticShader shader = new StaticShader();
	private EntityRenderer entityRenderer;
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();

	public MasterRenderer() {
		enableBackCulling();
		// Create projection matrix.
		projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), DisplayManager.width / DisplayManager.height, NEAR_PLANE, FAR_PLANE);
		// Create renderers.
		entityRenderer = new EntityRenderer(shader, projectionMatrix);
	}

	public static void enableBackCulling() {
		// Enable face culling and cull back faces (Don't render the back side of polygons!)
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glCullFace(GL11.GL_BACK);
	}

	public static void disableBackCulling() {
		// Disable back culling (Render the back side of polygons!)
		GL11.glDisable(GL11.GL_CULL_FACE);
	}

	public void render(Light light, Camera camera) {
		prepare();
		shader.start();
		shader.loadLight(light);
		shader.loadAmbientLight();
		shader.loadViewMatrix(camera);
		entityRenderer.render(entities);
		shader.stop();
		entities.clear();
	}

	/**
	 * Prepares OpenGL to render.
	 */
	public void prepare() {
		// Enable depth test for OpenGL to automatically decide which side of
		// polygons is the inside/outside.
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		// Clear screen.
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
		GL11.glClearColor(1, 1, 1, 1);
	}

	public void cleanUp() {
		shader.cleanUp();
	}

	public void processEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = entities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			entities.put(entityModel, newBatch);
		}
	}
}
