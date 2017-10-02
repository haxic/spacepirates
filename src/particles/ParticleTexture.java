package particles;

public class ParticleTexture {

	private int textureID;
	private int atlasSize;

	public ParticleTexture(int textureID, int rows) {
		this.textureID = textureID;
		this.atlasSize = rows;
	}

	public int getTextureID() {
		return textureID;
	}

	public int getAtlasSize() {
		return atlasSize;
	}

}
