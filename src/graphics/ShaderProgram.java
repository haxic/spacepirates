package graphics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import utility.FileUtils;

public abstract class ShaderProgram {
	private int id;
	private int vertID;
	private int fragID;

	public ShaderProgram(String vertexFile, String fragmentFile) {
		vertID = loadShader(vertexFile, GL20.GL_VERTEX_SHADER);
		fragID = loadShader(fragmentFile, GL20.GL_FRAGMENT_SHADER);
		id = GL20.glCreateProgram();
		GL20.glAttachShader(id, vertID);
		GL20.glAttachShader(id, fragID);
		GL20.glLinkProgram(id);
		GL20.glValidateProgram(id);
	}
	
	public void start() {
		GL20.glUseProgram(id);
	}
	
	public void stop() {
		GL20.glUseProgram(0);
	}
	
	public void cleanUp() {
		stop();
		GL20.glDetachShader(id, vertID);
		GL20.glDetachShader(id, fragID);
		GL20.glDeleteShader(vertID);
		GL20.glDeleteShader(fragID);
		GL20.glDeleteProgram(id);
	}
	
	protected void bindAttribute(int attribute, String name) {
		GL20.glBindAttribLocation(id, attribute, name);
	}
	
	protected abstract void bindAttributes();

	private static int loadShader(String file, int type) {
		String source = FileUtils.loadAsString(file);
		int shaderID = GL20.glCreateShader(type);
		GL20.glShaderSource(shaderID, source);
		GL20.glCompileShader(shaderID);
		if (GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE) {
			System.err.println("Failed to compile shader: " + file);
			System.err.println(GL20.glGetShaderInfoLog(shaderID));
			return -1;
		}
		return shaderID;
	}
}
