package entities;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import inputHandlers.KeyboardHandler;
import renderEngine.DisplayManager;

public class Camera {
	public Vector3f position = new Vector3f();
	public Vector3f rotation = new Vector3f(); // pitch, yaw, roll

	public void move(float delta) {
		float speed = 20;
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_W))
			position.z -= speed * delta;
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_S))
			position.z += speed * delta;
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_A))
			position.x -= speed * delta;
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_D))
			position.x += speed * delta;
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_LEFT_CONTROL))
			position.y += speed * delta;
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_LEFT_SHIFT))
			position.y -= speed * delta;
		if (KeyboardHandler.kb_keyDownOnce(GLFW_KEY_Q))
			DisplayManager.toggleCursor();
	}
}
