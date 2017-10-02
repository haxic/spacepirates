package fontRendering;

import org.joml.Vector2f;
import org.joml.Vector3f;

import renderEngine.ShaderProgram;

public class FontShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/font.vert";
	private static final String FRAGMENT_FILE = "shaders/font.frag";
	
	private int location_color;
	private int location_translation;	
	
	public FontShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void getAllUniformLocations() {
		location_color = super.getUniformLocation("color");
		location_translation = super.getUniformLocation("translation");	
		
	}

	@Override
	protected void bindAttributes() {
		// Bind index 0 in VAO to the position.
		super.bindAttribute(0, "position");
		// Bind index 1 in VAO to the texture coordinate.
		super.bindAttribute(1, "textureCoordinate");
	}
	
	protected void loadColor(Vector3f color) {
		super.loadVector3f(location_color, color);
	}
	
	protected void loadTranslation(Vector2f translation) {
		super.loadVector2f(location_translation, translation);
	}


}
