package client;

import static org.lwjgl.glfw.GLFW.*;

import graphics.DisplayManager;
import graphics.Loader;
import graphics.Renderer;
import graphics.StaticShader;
import graphics.models.RawModel;
import graphics.models.TexturedModel;
import graphics.textures.ModelTexture;

public class MainGameLoop {

	Loader loader;
	Renderer renderer;
	TexturedModel texturedModel;
	StaticShader shader;

	public MainGameLoop() {
		initDisplay();
		init();
		while (glfwWindowShouldClose(DisplayManager.windowID) == false) {
			updateGraphics();
			update();
		}
		cleanUp();
	}

	private void initDisplay() {
		DisplayManager.createDisplay();
		loader = new Loader();
		renderer = new Renderer();
		shader = new StaticShader();
	}

	private void init() {
		float[] vertices = { //
				-0.5f, 0.5f, 0f, //
				-0.5f, -0.5f, 0f, //
				0.5f, -0.5f, 0f, //
				0.5f, 0.5f, 0f, //
		};
		float[] textureCoords = { //
				0, 0, //
				0, 1, //
				1, 1, //
				1, 0 //
		};
		int[] indices = { //
				0, 1, 3, //
				3, 1, 2 //
		};

		RawModel rawModel = loader.loadToVAO(vertices, textureCoords, indices);
		ModelTexture texture = new ModelTexture(loader.loadTexture("grass"));
		texturedModel = new TexturedModel(rawModel, texture);
	}

	private void updateGraphics() {
		glfwPollEvents();
		renderer.prepare();
		// shader.start();
		renderer.render(texturedModel);
		// shader.stop();
		DisplayManager.updateDisplay();
	}

	private void update() {
	}

	private void cleanUp() {
		shader.cleanUp();
		loader.cleanUP();
		DisplayManager.closeDisplay();
		System.out.println("Cleaned up successful!");
	}

	public static void main(String[] args) {
		new MainGameLoop();
	}

}
