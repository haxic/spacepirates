package engineTester;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.joml.Vector2f;
import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import entities.Camera;
import entities.Entity;
import entities.Light;
import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontRendering.TextManager;
import models.RawModel;
import models.TexturedModel;
import normalMappingObjConverter.NormalMappedObjLoader;
import particles.Particle;
import particles.ParticleManager;
import particles.ParticleSystem;
import particles.ParticleTexture;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import utils.Loader;
import utils.ModelData;
import utils.OBJFileLoader;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();
		MasterRenderer masterRenderer = new MasterRenderer(loader);
		TextManager.init(loader);
		ParticleManager.init(loader, masterRenderer.getProjectionMatrix());

		FontType font = new FontType(loader.loadFontTexture("fonts/Candara/Candara"), new File("res/fonts/Candara/Candara.fnt"));
		GUIText text = new GUIText("Test string", 1, font, new Vector2f(0, 0), (800.0f/DisplayManager.getWidth()), false);
		text.setColour(0, 0, 0);
		
		ModelData modelData1 = OBJFileLoader.loadOBJ("texturedCube");
		RawModel rawModel1 = loader.loadToVAO(modelData1);
		ModelTexture modelTexture1 = new ModelTexture(loader.loadTexture("texture"));
		TexturedModel texturedModel1 = new TexturedModel(rawModel1, modelTexture1);
		texturedModel1.getModelTexture().setShineDamper(5);
		texturedModel1.getModelTexture().setReflectivity(1);

		ModelData modelData2 = OBJFileLoader.loadOBJ("stall");
		RawModel rawModel2 = loader.loadToVAO(modelData2);
		ModelTexture modelTexture2 = new ModelTexture(loader.loadTexture("stallTexture"));
		TexturedModel texturedModel2 = new TexturedModel(rawModel2, modelTexture2);
		texturedModel2.getModelTexture().setShineDamper(5);
		texturedModel2.getModelTexture().setReflectivity(1);

		ModelData modelData3 = OBJFileLoader.loadOBJ("dragon");
		RawModel rawModel3 = loader.loadToVAO(modelData3);
		ModelTexture modelTexture3 = new ModelTexture(loader.loadTexture("dragon"));
		TexturedModel texturedModel3 = new TexturedModel(rawModel3, modelTexture3);
		texturedModel3.getModelTexture().setShineDamper(10);
		texturedModel3.getModelTexture().setReflectivity(1);

		ModelData modelData4 = OBJFileLoader.loadOBJ("fern");
		RawModel rawModel4 = loader.loadToVAO(modelData4);
		ModelTexture modelTexture4 = new ModelTexture(loader.loadTexture("fern"));
		modelTexture4.setHasTransparency(true);
		modelTexture4.setAllowBackLighting(true);
		TexturedModel texturedModel4 = new TexturedModel(rawModel4, modelTexture4);

		Entity entity1 = new Entity(texturedModel1, new Vector3f(-30, 0, 0), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Entity entity2 = new Entity(texturedModel2, new Vector3f(-20, -5, -15), new Vector3f(0, 180, 0), new Vector3f(1, 1, 1));
		Entity entity3 = new Entity(texturedModel3, new Vector3f(0, -10, -35), new Vector3f(0, 225, 0), new Vector3f(3, 3, 3));
		Entity entity4 = new Entity(texturedModel4, new Vector3f(0, -7, -15), new Vector3f(0, 0, 0), new Vector3f(1.5f, 1.5f, 1.5f));

		TexturedModel normalTexturedModelData1 = new TexturedModel(NormalMappedObjLoader.loadOBJ("models/barrel/barrel", loader), new ModelTexture(loader.loadTexture("models/barrel/barrel")));
		normalTexturedModelData1.getModelTexture().setNormalMap(loader.loadTexture("models/barrel/barrelNormal"));
		normalTexturedModelData1.getModelTexture().setHasTransparency(true);
		normalTexturedModelData1.getModelTexture().setAllowBackLighting(true);

		TexturedModel normalTexturedModelData2 = new TexturedModel(NormalMappedObjLoader.loadOBJ("models/boulder/boulder", loader), new ModelTexture(loader.loadTexture("models/boulder/boulder")));
		normalTexturedModelData2.getModelTexture().setNormalMap(loader.loadTexture("models/boulder/boulderNormal"));
		normalTexturedModelData2.getModelTexture().setHasTransparency(true);
		normalTexturedModelData2.getModelTexture().setAllowBackLighting(true);

		TexturedModel normalTexturedModelData3 = new TexturedModel(NormalMappedObjLoader.loadOBJ("models/crate/crate", loader), new ModelTexture(loader.loadTexture("models/crate/crate")));
		normalTexturedModelData3.getModelTexture().setNormalMap(loader.loadTexture("models/crate/crateNormal"));
		normalTexturedModelData3.getModelTexture().setHasTransparency(true);
		normalTexturedModelData3.getModelTexture().setAllowBackLighting(true);

		Entity normalEntity1 = new Entity(normalTexturedModelData1, new Vector3f(-40, 0, -15), new Vector3f(90, 0, 0), new Vector3f(1, 1, 1));
		Entity normalEntity2 = new Entity(normalTexturedModelData2, new Vector3f(-60, 0, -15), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Entity normalEntity3 = new Entity(normalTexturedModelData3, new Vector3f(-80, 0, -15), new Vector3f(0, 0, 0), new Vector3f(0.06f, 0.06f, 0.06f));

		Entity entity5 = new Entity(normalTexturedModelData1, new Vector3f(-50, 0, -15), new Vector3f(90, 0, 0), new Vector3f(1, 1, 1));
		Entity entity6 = new Entity(normalTexturedModelData2, new Vector3f(-70, 0, -15), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Entity entity7 = new Entity(normalTexturedModelData3, new Vector3f(-90, 0, -15), new Vector3f(0, 0, 0), new Vector3f(0.06f, 0.06f, 0.06f));
		
		List<Entity> entities = new ArrayList();
		entities.add(entity1);
		entities.add(entity2);
		entities.add(entity3);
		entities.add(entity4);
		entities.add(entity5);
		entities.add(entity6);
		entities.add(entity7);
		List<Entity> normalMappedEntities = new ArrayList();
		normalMappedEntities.add(normalEntity1);
		normalMappedEntities.add(normalEntity2);
		normalMappedEntities.add(normalEntity3);
		
		ParticleTexture particleTexture = new ParticleTexture(loader.loadTexture("cosmic"), 4);

		List<Light> lights = new ArrayList<Light>();
		Light light1 = new Light(new Vector3f(-30, 0, 0), new Vector3f(1.0f, 1.0f, 1.0f));
		lights.add(light1);
		int md = 25; // max distance
//		for (int i = 0; i < 5; i++) {
//			Light light2 = new Light(new Vector3f((float) (Math.random() * md - md / 2), (float) (Math.random() * md - md / 2), (float) (Math.random() * md - md / 2)),
//					new Vector3f((float) (Math.random() * Math.random() * 5), (float) (Math.random() * Math.random() * 5), (float) (Math.random() * Math.random() * 5)),
//					new Vector3f(1, 0.01f, 0.004f));
//			lights.add(light2);
//		}
		Camera camera = new Camera();

		long timer = System.currentTimeMillis();
		int frames = 0;
		
		ParticleSystem ps = new ParticleSystem(50, 2, 3, particleTexture);
		
		while (!GLFW.glfwWindowShouldClose(DisplayManager.windowID)) {
			// LOGICs
			// Move camera.
			double time = System.currentTimeMillis();
			float delta = DisplayManager.getDelta();
			// light1.getPosition().x = (float) (Math.cos(time) / 200);
			// light1.getPosition().z = (float) (Math.sin(time) / 200);
			entity1.rotateBy(new Vector3f(delta * 250, 0, 0));
//			light1.getPosition().x = (float) (Math.cos(time / 800)) * 50;
//			light1.getPosition().z = (float) (Math.sin(time / 800)) * 50 - 20;
			entity4.rotateBy(new Vector3f(0, delta * 5, 0));
			camera.move(delta);
			ps.generateParticles(new Vector3f(0, 0, -10));
			ParticleManager.update(camera);
			// entity1.rotateBy(new Vector3f(1,1,0));

			// RENDER
			// Render scene.
			masterRenderer.renderScene(entities, normalMappedEntities, lights, camera);
			TextManager.render();
			ParticleManager.renderParticles(camera);
			// Draw screen
			DisplayManager.updateDisplay();

			// CHECK FPS
			frames++;
			if (System.currentTimeMillis() - timer > 1000) {
				timer += 1000;
				System.out.println("Fps: " + frames + ". Time delta: " + delta);
				frames = 0;
			}
		}
		masterRenderer.cleanUp();
		ParticleManager.cleanUp();
		TextManager.cleanUp();
		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
