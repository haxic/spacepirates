package entities;

import static org.lwjgl.glfw.GLFW.*;

import org.joml.Vector3f;

import inputHandlers.KeyboardHandler;
import inputHandlers.MouseScrollHandler;
import renderEngine.DisplayManager;

public class Camera {
	public Vector3f E = new Vector3f(0, 0, 0);
	public Vector3f D = new Vector3f(0, 0, -1);
	public Vector3f U = new Vector3f(0, 1, 0);
	public Vector3f R = new Vector3f(1, 0, 0);
	public Vector3f lookAt = E.add(D, new Vector3f());

	Vector3f v1 = new Vector3f();
	Vector3f v2 = new Vector3f();

	public void move(float delta) {
		delta = 0.01f;
		float speed = 20;
		float realSpeed = speed * delta;
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_W))
			E.add(D.mul(realSpeed, v1));
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_S))
			E.sub(D.mul(realSpeed, v1));
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_A))
			E.sub(R.mul(realSpeed, v1));
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_D))
			E.add(R.mul(realSpeed, v1));
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_Q))
			roll(realSpeed*0.1f);
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_E))
			roll(-realSpeed*0.1f);
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_LEFT_CONTROL))
			E.add(U.mul(realSpeed, v1));
		if (KeyboardHandler.kb_keyDown(GLFW_KEY_LEFT_SHIFT))
			E.sub(U.mul(realSpeed, v1));
		
		if (KeyboardHandler.kb_keyDownOnce(GLFW_KEY_LEFT_ALT))
			DisplayManager.toggleCursor();
		if (DisplayManager.mouseDeltas.x != 0)
			yaw(-DisplayManager.mouseDeltas.x * 0.5f);
		if (DisplayManager.mouseDeltas.y != 0)
			pitch(DisplayManager.mouseDeltas.y * 0.5f);
		E.add(D, lookAt);
	}

	public void pitch(double angle) {
		// D = normalize(D * cos a + U * sin a)
		D.mul((float) Math.cos(angle), v1).add(U.mul((float) Math.sin(angle), v2)).normalize(D);
		R.cross(D, U);
	}

	public void roll(double angle) {
		// R = normalize(R * cos a + U * sin a)
		R.mul((float) Math.cos(angle), v1).add(U.mul((float) Math.sin(angle), v2)).normalize(R);
		R.cross(D, U);
	}

	public void yaw(double angle) {
		// R = R * cos a + D * sin a
		R.mul((float) Math.cos(angle), v1).add(D.mul((float) Math.sin(angle), v2), R);
		U.cross(R, D);
	}
}
