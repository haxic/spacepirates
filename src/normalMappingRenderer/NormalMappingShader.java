package normalMappingRenderer;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import entities.Camera;
import entities.Light;
import renderEngine.ShaderProgram;
import textures.ModelTexture;
import utils.Maths;

public class NormalMappingShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/normal.vert";
	private static final String FRAGMENT_FILE = "shaders/normal.frag";
	private static final float AMBIENT_LIGHT = 0.0f;
	private static final int MAX_LIGHTS = 64;

	// Model View Projection matrixes
	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;
	// Lighting
	private int[] location_lightPosition;
	private int[] location_lightColor;
	private int[] location_attenuation;
	private int location_numberOfLights;
	private int location_shineDamper;
	private int location_reflectivity;
	private int location_ambientLight;
	private int location_allowBackLighting;
	// Texture atlas
	private int location_atlasSize;
	private int location_textureOffset;
	// Normal mapping
	private int location_modelTexture;
	private int location_normalMap;
	
	public NormalMappingShader() {
		super(VERTEX_FILE, FRAGMENT_FILE);
	}

	@Override
	protected void bindAttributes() {
		// Bind index 0 in VAO to the position.
		super.bindAttribute(0, "position");
		// Bind index 1 in VAO to the texture coordinate.
		super.bindAttribute(1, "textureCoordinate");
		// Bind index 2 in VAO to the normal.
		super.bindAttribute(2, "normal");
		// Bind index 2 in VAO to the normal.
		super.bindAttribute(3, "tangent");
	}

	@Override
	protected void getAllUniformLocations() {
		location_modelMatrix = super.getUniformLocation("model");
		location_viewMatrix = super.getUniformLocation("view");
		location_projectionMatrix = super.getUniformLocation("projection");
		location_lightPosition = new int[MAX_LIGHTS];
		location_lightColor = new int[MAX_LIGHTS];
		location_attenuation = new int[MAX_LIGHTS];
		for (int i = 0; i < MAX_LIGHTS; i++) {
			location_lightPosition[i] = super.getUniformLocation("lightPosition[" + i + "]");
			location_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
			location_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
		}
		location_numberOfLights = super.getUniformLocation("numberOfLights");
		location_shineDamper = super.getUniformLocation("shineDamper");
		location_reflectivity = super.getUniformLocation("reflectivity");
		location_ambientLight = super.getUniformLocation("ambientLight");
		location_allowBackLighting = super.getUniformLocation("allowBackLighting");
		location_atlasSize = super.getUniformLocation("atlasSize");
		location_textureOffset = super.getUniformLocation("textureOffset");
        location_modelTexture = super.getUniformLocation("modelTexture");
        location_normalMap = super.getUniformLocation("normalMap");
	}
	
	protected void bindTextureUnits() {
		super.loadInt(location_modelTexture, 0);
		super.loadInt(location_normalMap, 1);
	}

	public void loadTextureOffset(Vector2f textureOffset) {
		super.loadVector2f(location_textureOffset, textureOffset);
	}

	public void loadAtlasSize(float atlasSize) {
		super.loadFloat(location_atlasSize, atlasSize);
	}

	public void loadAllowBackLighting(boolean allowBackLighting) {
		super.loadFloat(location_allowBackLighting, allowBackLighting ? 1 : 0);
	}

	public void loadAmbientLight() {
		super.loadFloat(location_ambientLight, AMBIENT_LIGHT);
	}

	public void loadSpecularLighting(ModelTexture modelTexture) {
		super.loadFloat(location_shineDamper, modelTexture.getShineDamper());
		super.loadFloat(location_reflectivity, modelTexture.getReflectivity());
	}

	public void loadLights(List<Light> lights) {
		int numberOfLights = lights.size() < MAX_LIGHTS ? lights.size() : MAX_LIGHTS;
		super.loadInt(location_numberOfLights, numberOfLights);
		for (int i = 0; i < MAX_LIGHTS && i < numberOfLights; i++) {
			super.loadVector3f(location_lightPosition[i], lights.get(i).getPosition());
			super.loadVector3f(location_lightColor[i], lights.get(i).getColor());
			super.loadVector3f(location_attenuation[i], lights.get(i).getAttenuation());
		}
	}

	public void loadModelMatrix(Matrix4f matrix) {
		super.loadMatrixf(location_modelMatrix, matrix);
	}

	public void loadViewMatrix(Matrix4f matrix) {
		super.loadMatrixf(location_viewMatrix, matrix);
	}

	public void loadProjectionMatrix(Matrix4f matrix) {
		super.loadMatrixf(location_projectionMatrix, matrix);
	}
	
    private Vector3f getEyeSpacePosition(Light light, Matrix4f viewMatrix){
        Vector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.x,position.y, position.z, 1f);
        viewMatrix.transform(eyeSpacePos);
        return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
    }
}
