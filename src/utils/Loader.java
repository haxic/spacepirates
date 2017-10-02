package utils;

import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.imageio.ImageIO;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL33;

import de.matthiasmann.twl.utils.PNGDecoder;
import de.matthiasmann.twl.utils.PNGDecoder.Format;
import models.RawModel;
import textures.TextureData;

public class Loader {

	// Used to keep track of created VAOs and VBOs.
	private List<Integer> vaos = new ArrayList<Integer>();
	private Map<Integer, List<Integer>> vboReferences = new HashMap<Integer, List<Integer>>();
	private List<Integer> vbos = new ArrayList<Integer>();
	private List<Integer> textures = new ArrayList<Integer>();

	// Load model
	public RawModel loadToVAO(ModelData modelData) {
		return loadToVAO(modelData.getVertices(), modelData.getTextureCoordinates(), modelData.getNormals(), modelData.getIndices());
	}

	// Load model
	public RawModel loadToVAO(float[] vertices, float[] textureCoordinates, float[] normals, int[] indices) {
		int vaoID = createVAO();
		// System.out.println("CREATED VAO: " + vaoID);
		// Bind index buffer, positions and texture coordinates.
		bindIndexBuffer(vaoID, indices);
		storeDataInAttributeList(vaoID, 0, 3, vertices);
		storeDataInAttributeList(vaoID, 1, 2, textureCoordinates);
		storeDataInAttributeList(vaoID, 2, 3, normals);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	// Load normal mapped model
	public RawModel loadToVAO(float[] vertices, float[] textureCoordinates, float[] normals, float[] tangents, int[] indices) {
		int vaoID = createVAO();
		// System.out.println("CREATED VAO: " + vaoID);
		// Bind index buffer, positions and texture coordinates.
		bindIndexBuffer(vaoID, indices);
		storeDataInAttributeList(vaoID, 0, 3, vertices);
		storeDataInAttributeList(vaoID, 1, 2, textureCoordinates);
		storeDataInAttributeList(vaoID, 2, 3, normals);
		storeDataInAttributeList(vaoID, 3, 3, tangents);
		unbindVAO();
		return new RawModel(vaoID, indices.length);
	}

	// Load font
	public int loadToVAO(float[] vertices, float[] textureCoordinates) {
		int vaoID = createVAO();
		// Bind index buffer, positions and texture coordinates.
		storeDataInAttributeList(vaoID, 0, 2, vertices);
		storeDataInAttributeList(vaoID, 1, 2, textureCoordinates);
		unbindVAO();
		return vaoID;
	}

	// Load quad (square)
	public RawModel loadToVAO(float[] positions, int dimensions) {
		int vaoID = createVAO();
		// System.out.println("CREATED VAO: " + vaoID);
		storeDataInAttributeList(vaoID, 0, dimensions, positions);
		unbindVAO();
		return new RawModel(vaoID, positions.length / dimensions);
	}

	public void removeMesh(int vaoID) {
		for (Integer vboID : vboReferences.get(vaoID)) {
			GL30.glDeleteVertexArrays(vboID);
			// System.out.println("DELETED VBO: " + vboID + " in VAO: " + vaoID);
		}
		vboReferences.remove(vaoID);
		GL30.glDeleteVertexArrays(vaoID);
		vaos.remove(vaoID);
		// System.out.println("DELETED VAO: " + vaoID);
	}

	/**
	 * Deletes all VAOs and VBOs from memory.
	 */
	public void cleanUp() {
		int vbosCounted = 0;
		for (Entry<Integer, List<Integer>> entry : vboReferences.entrySet()) {
			for (Integer vboID : entry.getValue()) {
				vbosCounted++;
				GL30.glDeleteVertexArrays(vboID);
				// System.out.println("DELETED VBO: " + vboID + " in VAO: " + entry.getKey());
			}
		}
		for (int vaoID : vaos) {
			GL30.glDeleteVertexArrays(vaoID);
			// System.out.println("DELETED VAO: " + vaoID);
		}
		for (int textureID : textures) {
			GL11.glDeleteTextures(textureID);
			// System.out.println("DELETED TEXTURE: " + textureID);
		}
		if (vbosCounted != vbos.size())
			System.out.println("WARNING: VBO miscounting! Counted: " + vbosCounted + ", but total list is: " + vbos.size() + "!");
		else
			System.out.println("Clean-up successful!");

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

	// Creates an empty VBO that can be used for instanced rendering
	public int createEmptyVBO(int floatCount) {
		// Create VBO.
		int vboID = GL15.glGenBuffers();
		// System.out.println("CREATED EMPTY VBO: " + vboID);
		vbos.add(vboID);
		// Bind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// Allocate memory to the VBO and notify that this VBO is going to be updated often.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, floatCount * 4, GL15.GL_STREAM_DRAW);
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		return vboID;
	}

	public void addInstancedAttribute(int vaoID, int vboID, int attributeNumber, int dataSize, int instancedDataLength, int offset) {
		// Create a reference to the VBO for the VAO.
		if (vboReferences.get(vaoID) == null)
			vboReferences.put(vaoID, new ArrayList<Integer>());
		if (!vboReferences.get(vaoID).contains(vboID)) {
			vboReferences.get(vaoID).add(vboID);
//			System.out.println("ADDED VBO: " + vboID + " to VAO: " + vaoID);
		} else {
//			System.out.println("ADDED MORE DATA TO VBO: " + vboID + " in VAO: " + vaoID);
		}
		// Bind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// Bind VAO.
		GL30.glBindVertexArray(vaoID);
		// Store VBO in a VAO attribute list.
		GL20.glVertexAttribPointer(attributeNumber, dataSize, GL11.GL_FLOAT, false, instancedDataLength * 4, offset * 4);
		// Set VBO to be a per instance attribute.
		GL33.glVertexAttribDivisor(attributeNumber, 1);
		// Unbind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
		// Unbind VAO.
		GL30.glBindVertexArray(0);
	}

	public void updateVBO(int vboID, float[] data, FloatBuffer buffer) {
		buffer.clear();
		buffer.put(data);
		buffer.flip();
		// Bind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
		// Reallocate memory to the VBO.
		GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer.capacity() * 4, GL15.GL_STREAM_DRAW);
		// Store new data.
		GL15.glBufferSubData(GL15.GL_ARRAY_BUFFER, 0, buffer);
		// Unbind VBO.
		GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
	}

	/**
	 * Stores data in an attribute list.
	 */
	private void storeDataInAttributeList(int vaoID, int attributeNumber, int size, float[] data) {
		// Create VBO.
		int vboID = GL15.glGenBuffers();
		// System.out.println("CREATED VBO: " + vboID + " in VAO: " + vaoID);
		if (vboReferences.get(vaoID) == null)
			vboReferences.put(vaoID, new ArrayList<Integer>());
		vboReferences.get(vaoID).add(vboID);
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
	private void bindIndexBuffer(int vaoID, int[] indices) {
		// Create VBO.
		int vboID = GL15.glGenBuffers();
		// System.out.println("CREATED VBO: " + vboID + " in VAO: " + vaoID);
		if (vboReferences.get(vaoID) == null)
			vboReferences.put(vaoID, new ArrayList<Integer>());
		vboReferences.get(vaoID).add(vboID);
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
		int textureID = GL11.glGenTextures();
		// System.out.println("CREATED TEXTURE: " + textureID);
		textures.add(textureID);
		// Bind texture.
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		// Create texture.
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, getDataAsIntBuffer(data));
		// Mipmapping - use down scaled resolution textures at distance
		// Generate down scaled resolution textures
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		// Use down scaled resolution textures, when texture is rendered on a small surface, using smooth transition
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		// glTexParameteri(GL11.GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);

		// Specify how the texture should be rendered on model.
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_SMOOTH);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_SMOOTH);
		// Unbind texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return textureID;
	}

	public int loadFontTexture(String fileName) {
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
		int textureID = GL11.glGenTextures();
		// System.out.println("CREATED TEXTURE: " + textureID);
		textures.add(textureID);
		// Bind texture.
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
		// Create texture.
		GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, getDataAsIntBuffer(data));
		// Mipmapping - use down scaled resolution textures at distance
		// Generate down scaled resolution textures
		GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
		// Use down scaled resolution textures, when texture is rendered on a small surface, using smooth transition
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		// glTexParameteri(GL11.GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0);

		// Specify how the texture should be rendered on model.
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		// Unbind texture
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		return textureID;
	}

	String[] cubeMapNamingOrder = { "right", "left", "top", "bottom", "back", "front" };

	public int loadCubeMap(String[] textureFiles) {
		// Generate empty texture.
		int textureID = GL11.glGenTextures();
		// System.out.println("CREATED TEXTURE: " + textureID);
		// Activate texture unit 0.
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		// Bind texture id to cube map.
		GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);
		// --T
		// L F R
		// --B
		// --B
		// Order: Right, Left, Top, Bottom, Back, Front
		for (int i = 0; i < textureFiles.length; i++) {
			TextureData data = decodeTextureFile("res/skybox/" + textureFiles[i] + ".png");
			GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0, GL11.GL_RGBA, data.width, data.height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.buffer);
		}
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		// glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_S, GL11.GL_CLAMP_TO_EDGE);
		// glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP_TO_EDGE);
		textures.add(textureID);
		return textureID;
	}

	private TextureData decodeTextureFile(String fileName) {
		int width = 0;
		int height = 0;
		ByteBuffer buffer = null;
		try {
			FileInputStream in = new FileInputStream(fileName);
			PNGDecoder decoder = new PNGDecoder(in);
			width = decoder.getWidth();
			height = decoder.getHeight();
			buffer = ByteBuffer.allocateDirect(4 * width * height);
			decoder.decode(buffer, width * 4, Format.RGBA);
			buffer.flip();
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println("Tried to load texture " + fileName + ", didn't work.");
			System.exit(-1);
		}
		return new TextureData(width, height, buffer);
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
