package graphics;

public class StaticShader extends ShaderProgram {
	private static final String VERT_FILE = "shaders/entity.vert";
	private static final String FRAG_FILE = "shaders/entity.frag";

	public StaticShader() {
		super(VERT_FILE, FRAG_FILE);
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
		bindAttribute(1, "textureCoords");
	}

}
