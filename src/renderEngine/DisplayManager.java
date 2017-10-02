package renderEngine;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

import inputHandlers.KeyboardHandler;
import inputHandlers.MouseHandler;
import inputHandlers.MouseScrollHandler;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.DoubleBuffer;

import org.joml.Vector2f;
import org.lwjgl.BufferUtils;

public class DisplayManager {
	public static long windowID;
	private final static int width = 1920;
	private final static int height = 1080;
	private final static double aspect = (double) width / (double) height;

	private static double lastFrameTime;
	private static float delta;

	public static void createDisplay() {
		if (!glfwInit()) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		windowID = glfwCreateWindow(width, height, "Heavy Space", NULL, NULL);
		if (windowID == NULL) {
			System.err.println("Could not create GLFW window!");
			return;
		}
		GLFWVidMode videoMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowID, (videoMode.width() - width) / 2, (videoMode.height() - height) / 2);
		glfwMakeContextCurrent(windowID);
		glfwSetKeyCallback(windowID, new KeyboardHandler());
		glfwSetMouseButtonCallback(windowID, new MouseHandler());
		glfwSetScrollCallback(windowID, new MouseScrollHandler());

		glfwShowWindow(windowID);
		GL.createCapabilities();
		GLCapabilities caps = GL.getCapabilities();
		if (caps.OpenGL30) {
			System.out.println("Use GL30");
		} else if (caps.GL_ARB_framebuffer_object) {
			System.out.println("Use ARBFramebufferObject");
		} else if (caps.GL_EXT_framebuffer_object) {
			System.out.println("Use EXTFramebufferObject");
		} else
			throw new UnsupportedOperationException();

		glViewport(0, 0, width, height);
		glfwSwapInterval(1);
		lastFrameTime = getCurrentTime();
		disableCursor();
	}

	public static void updateDisplay() {
		delta = calculateDelta();
		glfwPollEvents();
		KeyboardHandler.poll();
		MouseHandler.poll();
		MouseScrollHandler.poll();
		updateMouse();
		glfwSwapBuffers(DisplayManager.windowID);
	}

	private static boolean cursorEnabled;
	public static Vector2f mouseDeltas = new Vector2f();
	public static Vector2f mousePos = new Vector2f();

	public static void updateMouse() {
		DoubleBuffer x = BufferUtils.createDoubleBuffer(1);
		DoubleBuffer y = BufferUtils.createDoubleBuffer(1);
		glfwGetCursorPos(windowID, x, y);
		x.rewind();
		y.rewind();
		mousePos.x = (float) x.get();
		mousePos.y = (float) y.get();
		if (!cursorEnabled) {
			float deltaX = (float) (mousePos.x - getWidth() / 2);
			float deltaY = (float) (mousePos.y - getHeight() / 2);
			mouseDeltas.x = deltaX * getDelta();
			mouseDeltas.y = deltaY * getDelta();
			glfwSetCursorPos(windowID, getWidth() / 2, getHeight() / 2);
		}
	}

	public static void disableCursor() {
		cursorEnabled = false;
		glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
		glfwSetCursorPos(windowID, getWidth() / 2, getHeight() / 2);
		mouseDeltas = new Vector2f();

	}

	public static void enableCursor() {
		cursorEnabled = true;
		glfwSetInputMode(windowID, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
	}

	public static void toggleCursor() {
		if (cursorEnabled)
			disableCursor();
		else
			enableCursor();
	}
	
	public static void closeDisplay() {
		glfwDestroyWindow(DisplayManager.windowID);
		glfwTerminate();
		System.out.println("Space Pirates terminated properly!");
	}

	public static float getDelta() {
		return delta;
	}

	private static float calculateDelta() {
		double time = getCurrentTime();
		float delta = (float) (time - lastFrameTime);
		lastFrameTime = time;
		return delta;
	}

	private static double getCurrentTime() {
		return glfwGetTime();
	}

	public static int getHeight() {
		return width;
	}

	public static int getWidth() {
		return height;
	}
	
	public static double getAspectRatio() {
		return aspect;
	}

}
