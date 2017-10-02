package renderEngine;

import java.util.List;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import entities.Camera;
import entities.Entity;
import entities.Light;
import textures.ModelTexture;
import utils.Maths;

public class StaticShader extends ShaderProgram {

	private static final String VERTEX_FILE = "shaders/shader.vert";
	private static final String FRAGMENT_FILE = "shaders/shader.frag";
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

	public StaticShader() {
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

}
