package engineTester;

import org.joml.Vector3f;
import org.lwjgl.glfw.GLFW;

import com.sun.javafx.scene.EnteredExitedHandler;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.Loader;
import models.RawModel;
import models.TexturedModel;
import renderEngine.DisplayManager;
import renderEngine.MasterRenderer;
import renderEngine.EntityRenderer;
import renderEngine.StaticShader;
import textures.ModelTexture;
import utils.ModelData;
import utils.OBJFileLoader;

public class MainGameLoop {
	public static void main(String[] args) {
		DisplayManager.createDisplay();

		Loader loader = new Loader();

		ModelData modelData1 = OBJFileLoader.loadOBJ("untitled");
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

		Light light = new Light(new Vector3f(200, 200, 100), new Vector3f(1, 1f, 1f));
		Camera camera = new Camera();
		Entity entity1 = new Entity(texturedModel1, new Vector3f(-30, 0, -15), new Vector3f(0, 0, 0), new Vector3f(1, 1, 1));
		Entity entity2 = new Entity(texturedModel2, new Vector3f(-20, -5, -15), new Vector3f(0, 180, 0), new Vector3f(1, 1, 1));
		Entity entity3 = new Entity(texturedModel3, new Vector3f(0, -10, -35), new Vector3f(0, 225, 0), new Vector3f(3, 3, 3));
		Entity entity4 = new Entity(texturedModel4, new Vector3f(0, -7, -15), new Vector3f(0, 0, 0), new Vector3f(1.5f, 1.5f, 1.5f));

		long timer = System.currentTimeMillis();
		int frames = 0;
		MasterRenderer masterRenderer = new MasterRenderer();
		while (!GLFW.glfwWindowShouldClose(DisplayManager.windowID)) {
			// LOGIC
			// Move camera.
			float delta = DisplayManager.getDelta();
			entity1.rotateBy(new Vector3f(delta * 250, 0, 0));
			entity4.rotateBy(new Vector3f(0, delta * 5, 0));
			camera.move(delta);
			// entity1.rotateBy(new Vector3f(1,1,0));
			
			// RENDER
			// Prepare entities that should be rendered.
			masterRenderer.processEntity(entity1);
			masterRenderer.processEntity(entity2);
			masterRenderer.processEntity(entity3);
			masterRenderer.processEntity(entity4);
			// Render scene.
			masterRenderer.render(light, camera);

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

		loader.cleanUp();
		DisplayManager.closeDisplay();
	}
}
