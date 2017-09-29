package renderEngine;

import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import entities.Entity;
import models.RawModel;
import models.TexturedModel;
import textures.ModelTexture;
import utils.Maths;

public class EntityRenderer {

	private StaticShader shader;

	public EntityRenderer(StaticShader shader, Matrix4f projectionMatrix) {
		this.shader = shader;
		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TexturedModel, List<Entity>> entities) {
		for (TexturedModel model : entities.keySet()) {
			prepareTexturedModel(model);
			List<Entity> batch = entities.get(model);
			for (Entity entity : batch) {
				prepareInstance(entity);
				// Draw model.
				GL11.glDrawElements(GL11.GL_TRIANGLES, model.getRawModel().getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
			}
			unbindTexturedModel();
		}
	}

	private void prepareTexturedModel(TexturedModel texturedModel) {
		RawModel rawModel = texturedModel.getRawModel();
		ModelTexture modelTexture = texturedModel.getModelTexture();
		// Bind VAO to use it.
		GL30.glBindVertexArray(rawModel.getVaoID());
		// Bind attribute list.
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		// Pass texture model atlas size.
		shader.loadAtlasSize(modelTexture.getAtlasSize());
		// Enable back culling for entities that has transparency.
		if (modelTexture.isHasTransparency())
			MasterRenderer.disableBackCulling();
		// Enable lighting for back side of polygons (opposite side of normals)
		shader.loadAllowBackLighting(modelTexture.isAllowBackLighting());
		// Load specular lighting.
		shader.loadSpecularLighting(modelTexture);
		// Activate textures.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, modelTexture.getTextureID());
	}

	private void unbindTexturedModel() {
		// Disable back culling just in case.
		MasterRenderer.enableBackCulling();
		// Unbind attributes.
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		// Unbind VAO.
		GL30.glBindVertexArray(0);
	}

	private void prepareInstance(Entity entity) {
		// Model matrix
		Matrix4f modelMatrix = Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
		shader.loadModelMatrix(modelMatrix);
		shader.loadTextureOffset(entity.getTextureOffset());
	}

	/**
	 * Renders the model.
	 */
	/*
	 * public void render(Entity entity, StaticShader shader) {
	 * TexturedModel texturedModel = entity.getModel();
	 * RawModel rawModel = texturedModel.getRawModel();
	 * // Bind VAO to use it.
	 * GL30.glBindVertexArray(rawModel.getVaoID());
	 * // Bind attribute list.
	 * GL20.glEnableVertexAttribArray(0);
	 * GL20.glEnableVertexAttribArray(1);
	 * GL20.glEnableVertexAttribArray(2);
	 * // Model matrix
	 * Matrix4f modelMatrix = Maths.createModelMatrix(entity.getPosition(), entity.getRotation(), entity.getScale());
	 * shader.loadModelMatrix(modelMatrix);
	 * shader.loadSpecularLighting(texturedModel.getModelTexture());
	 * // Activate textures.
	 * GL13.glActiveTexture(GL13.GL_TEXTURE0);
	 * GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getTextureID());
	 * // Draw model.
	 * GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	 * // Unbind attributes.
	 * GL20.glDisableVertexAttribArray(0);
	 * GL20.glDisableVertexAttribArray(1);
	 * GL20.glDisableVertexAttribArray(2);
	 * // Unbind VAO.
	 * GL30.glBindVertexArray(0);
	 * }
	 */

	/**
	 * Renders the model.
	 */
	/*
	 * public void render(TexturedModel texturedModel) {
	 * RawModel rawModel = texturedModel.getRawModel();
	 * // Bind VAO to use it.
	 * GL30.glBindVertexArray(rawModel.getVaoID());
	 * // Bind attribute list.
	 * GL20.glEnableVertexAttribArray(0);
	 * GL20.glEnableVertexAttribArray(1);
	 * // Activate textures.
	 * GL13.glActiveTexture(GL13.GL_TEXTURE0);
	 * GL11.glBindTexture(GL11.GL_TEXTURE_2D, texturedModel.getModelTexture().getTextureID());
	 * // Draw model.
	 * GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	 * // Unbind attributes.
	 * GL20.glDisableVertexAttribArray(0);
	 * GL20.glDisableVertexAttribArray(1);
	 * // Unbind VAO.
	 * GL30.glBindVertexArray(0);
	 * }
	 */

	/**
	 * Renders raw models.
	 */
	/*
	 * public void renderRawModel(RawModel model) {
	 * // Bind VAO to use it.
	 * GL30.glBindVertexArray(model.getVaoID());
	 * // Bind attribute list.
	 * GL20.glEnableVertexAttribArray(0);
	 * // Draw model.
	 * GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCount(), GL11.GL_UNSIGNED_INT, 0);
	 * // Unbind attribute list.
	 * GL20.glDisableVertexAttribArray(0);
	 * // Unbind VAO.
	 * GL30.glBindVertexArray(0);
	 * }
	 */

}
