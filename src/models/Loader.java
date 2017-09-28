package models;

import static org.lwjgl.opengl.GL11.GL_RGBA;
import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_BYTE;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL11.glGenTextures;
import static org.lwjgl.opengl.GL11.glTexImage2D;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import utils.ModelData;

public class Loader {

	// Used to keep track of created VAOs and VBOs.
	private List<Integer> vaos = new ArrayList<Integer>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	public RawModel loadToVAO(ModelData modelData) {
		return loadToVAO(modelData.getVertices(), modelData.getTextureCoordinates(), modelData.getNormals(), modelData.getIndices());
	}
	
	private RawModel loadToVAO(float[] vertices, float[] textureCoordinates, float[] normals, int[] indices) {
		int vaoID = createVAO();
		// Bind index buffer, positions and texture coordinates.
		bindIndexBuffer(indices);
		storedDataInAttributeList(0, 3, vertices);
		storedDataInAttributeList(1, 2, textureCoordinates);
		storedDataInAttributeList(2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	/**
	 * Deletes all VAOs and VBOs from memory.
	 */
	public void cleanUp() {
		for (int vaoID : vaos)
			GL30.glDeleteVertexArrays(vaoID);
		for (int vboID : vbos)
			GL15.glDeleteBuffers(vboID);
		for (int textureID : textures)
			GL11.glDeleteTextures(textureID);
	}

	/**
	 * Generates an unique VAO id and binds it.
	 * 
	 * @return the unique VAO id.
	 */
	private int createVAO() {
		// Generate unique VAO id.
		int vaoID = GL30.glGenVertexArrays();
		vaos.add(vaoID);
		// Bind VAO.
		GL30.glBindVertexArray(vaoID);
		return vaoID;
	}

	/**
	 * Stores data in an attribute list.
	 */
	private void storedDataInAttributeList(int attributeNumber, int size, float[] data) {
		// Create VBO.
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		// Bind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// Convert data into a float buffer.
		FloatBuffer buffer = getDataAsFloatBuffer(data);
		// Store data in VBO.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
		// Store VBO in a VAO attribute list.
		GL20.glVertexAttribPointer(attributeNumber, size, GL11.GL_FLOAT, false, 0, 0);
		// Unbind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Binds indices to VAO.
	 */
	private void bindIndexBuffer(int[] indices) {
		// Create VBO.
		int vboID = GL15.glGenBuffers();
		vbos.add(vboID);
		// Bind VBO.
		GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
		// Convert data into a float buffer.
		IntBuffer buffer = getDataAsIntBuffer(indices);
		// Store data in VBO.
		GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);
	}

	public int loadTexture(String fileName) {
		int[] pixels = null;
		int width = 0;
		int height = 0;
		try {
			BufferedImage image = ImageIO.read(new FileInputStream("res/" + fileName + ".png"));
			width = image.getWidth();
			height = image.getHeight();
			pixels = new int[width * height];
			image.getRGB(0, 0, width, height, pixels, 0, width);
		} catch (IOException e) {
			e.printStackTrace();
		}

		int[] data = new int[width * height];
		for (int i = 0; i < width * height; i++) {
			int a = (pixels[i] & 0xff000000) >> 24;
			int r = (pixels[i] & 0xff0000) >> 16;
			int g = (pixels[i] & 0xff00) >> 8;
			int b = (pixels[i] & 0xff);

			data[i] = a << 24 | b << 16 | g << 8 | r;
		}
		
		// Generate unique texture id.
		int result = glGenTextures();
		textures.add(result);
		// Bind texture.
		glBindTexture(GL_TEXTURE_2D, result);
		// Create texture.
		glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, width, height, 0, GL_RGBA, GL_UNSIGNED_BYTE, getDataAsIntBuffer(data));
		// Specify how the texture should be rendered on model.
		GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
		GL11.glTexParameteri(GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
		// Unbind texture
		glBindTexture(GL_TEXTURE_2D, 0);
		return result;
	}

	/**
	 * Unbinds the current VAO.
	 */
	private void unbindVAO() {
		// Unbind VAO when done.
		GL30.glBindVertexArray(0);
	}

	/**
	 * Converts float array to float buffer.
	 */
	private FloatBuffer getDataAsFloatBuffer(float[] data) {
		// Create float buffer.
		FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length);
		// Store data in buffer.
		buffer.put(data);
		// Prepare buffer to be read from.
		buffer.flip();
		return buffer;
	}

	/**
	 * Converts float array to integer buffer.
	 */
	private IntBuffer getDataAsIntBuffer(int[] data) {
		// Create integer buffer.
		IntBuffer buffer = BufferUtils.createIntBuffer(data.length);
		// Store data in buffer.
		buffer.put(data);
		// Prepare buffer to be read from.
		buffer.flip();
		return buffer;
	}


}
