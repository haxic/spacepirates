package graphics;

import static org.lwjgl.glfw.GLFW.GLFW_RESIZABLE;
import static org.lwjgl.glfw.GLFW.glfwCreateWindow;
import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetPrimaryMonitor;
import static org.lwjgl.glfw.GLFW.glfwGetVideoMode;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwSetWindowPos;
import static org.lwjgl.glfw.GLFW.glfwShowWindow;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowHint;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.GL_VERSION;
import static org.lwjgl.opengl.GL11.glClearColor;
import static org.lwjgl.opengl.GL11.glGetString;
import static org.lwjgl.opengl.GL11.glViewport;
import static org.lwjgl.system.MemoryUtil.NULL;

import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;

public class DisplayManager {
	public static long windowID;
	private final static int width = 800;
	private final static int height = 800;

	public static void createDisplay() {
		if (glfwInit() != true) {
			System.err.println("Could not initialize GLFW!");
			return;
		}
		glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
		windowID = glfwCreateWindow(width, height, "Space Pirates", NULL, NULL);
		if (windowID == NULL) {
			System.err.println("Could not create GLFW window!");
			return;
		}
		System.out.println("Window id: " + windowID);
		GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
		glfwSetWindowPos(windowID, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);
		glfwMakeContextCurrent(windowID);
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
		glClearColor(1f, 1f, 1f, 1f);
		System.out.println("OpenGL: " + glGetString(GL_VERSION));
	}

	public static void closeDisplay() {
		glfwDestroyWindow(DisplayManager.windowID);
		glfwTerminate();
	}

	public static int getWidth() {
		return width;
	}

	public static int getHeight() {
		return height;
	}

	public static void updateDisplay() {
		glfwSwapBuffers(windowID);
	}
}
