package renderEngine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.TexturedModel;
import normalMappingRenderer.NormalMappingRenderer;
import utils.Loader;
import utils.Maths;

public class MasterRenderer {

	public static final float FOV = 70;
	public static final float NEAR_PLANE = 0.1f;
	public static final float FAR_PLANE = 1000f;
	Matrix4f projectionMatrix;

	private StaticShader staticShader = new StaticShader();
	private EntityRenderer entityRenderer;
	private NormalMappingRenderer normalMappingRenderer;
	private SkyboxRenderer skyboxRenderer;
	
	private Map<TexturedModel, List<Entity>> entities = new HashMap<>();
	private Map<TexturedModel, List<Entity>> normalMappedEntities = new HashMap<>();

	public MasterRenderer(Loader loader) {
		enableBackCulling();
		// Create projection matrix.
		projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(FOV), (float) DisplayManager.getAspectRatio(), NEAR_PLANE, FAR_PLANE);
		// Create renderers.
		entityRenderer = new EntityRenderer(staticShader, projectionMatrix);
		skyboxRenderer = new SkyboxRenderer(loader, projectionMatrix);
		normalMappingRenderer = new NormalMappingRenderer(projectionMatrix);
	}
	
	public Matrix4f getProjectionMatrix() {
		return projectionMatrix;
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

	public void renderScene(List<Entity> entities, List<Entity> normalMappedEntities, List<Light> lights, Camera camera) {
		for (Entity entity : entities)
			processEntity(entity);
		for (Entity entity : normalMappedEntities)
			processNormalMappedEntity(entity);
		render(lights, camera);
	}
	
	private void render(List<Light> lights, Camera camera) {
		prepare();
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		staticShader.start();
		staticShader.loadLights(lights);
		staticShader.loadAmbientLight();
		staticShader.loadViewMatrix(viewMatrix);
		entityRenderer.render(entities);
		staticShader.stop();
		normalMappingRenderer.render(normalMappedEntities, lights, camera);
		skyboxRenderer.render(camera);
		entities.clear();
		normalMappedEntities.clear();
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
		GL11.glClearColor(0, 0, 0, 1);
	}

	public void cleanUp() {
		staticShader.cleanUp();
		normalMappingRenderer.cleanUp();
		skyboxRenderer.clearUp();
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
	
	public void processNormalMappedEntity(Entity entity) {
		TexturedModel entityModel = entity.getModel();
		List<Entity> batch = normalMappedEntities.get(entityModel);
		if (batch != null) {
			batch.add(entity);
		} else {
			List<Entity> newBatch = new ArrayList<>();
			newBatch.add(entity);
			normalMappedEntities.put(entityModel, newBatch);
		}
	}
}
