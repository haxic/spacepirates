package normalMappingRenderer;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Camera;
import entities.Entity;
import entities.Light;
import models.RawModel;
import models.TexturedModel;
import renderEngine.MasterRenderer;
import textures.ModelTexture;
import utils.Maths;

public class NormalMappingRenderer {

	private NormalMappingShader shader;

	public NormalMappingRenderer(Matrix4f projectionMatrix) {
		this.shader = new NormalMappingShader();
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.bindTextureUnits();
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities, List<Light> lights, Camera camera) {
		shader.start();
		prepare(lights, camera);
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
		shader.stop();
	}
	
	public void cleanUp(){
		shader.cleanUp();
	}

	private void prepareTexturedModel(TexturedModel model) {
		RawModel rawModel = model.getRawModel();
		GL30.glBindVertexArray(rawModel.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		ModelTexture texture = model.getModelTexture();
		shader.loadAtlasSize(texture.getAtlasSize());
		if (texture.isHasTransparency()) {
			MasterRenderer.disableBackCulling();
		}
		shader.loadSpecularLighting(texture);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getModelTexture().getNormalMap());
	}

	private void unbindTexturedModel() {
		MasterRenderer.enableBackCulling();
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		Matrix4f modelMatrix = Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.loadModelMatrix(modelMatrix);
		shader.loadTextureOffset(entity.getTextureOffset());
	}

	private void prepare(List<Light> lights, Camera camera) {
		Matrix4f viewMatrix = Maths.createViewMatrix(camera);
		shader.loadLights(lights);
		shader.loadAmbientLight();
		shader.loadViewMatrix(viewMatrix);
	}

}