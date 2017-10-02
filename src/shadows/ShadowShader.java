package shadows;

import org.joml.Matrix4f;

import renderEngine.ShaderProgram;

public class ShadowShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/shadow.vert";
	private static final String FRAGMENT_FILE = "shaders/shadow.frag";

	private int location_mvpMatrix;

	protected ShadowShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");

	}

	protected void loadMvpMatrix(Matrix4f mvpMatrix) {
		super.loadMatrixf(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
	}

}
