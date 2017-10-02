package textures;

public class ModelTexture {
	private int textureID;
	private int normalMap;
	
	public int getNormalMap() {
		return normalMap;
	}

	public void setNormalMap(int normalMap) {
		this.normalMap = normalMap;
	}

	private float shineDamper = 1;
	private float reflectivity = 0;
	private boolean hasTransparency = false;
	private boolean allowBackLighting = false;
	private int atlasSize = 1;

	public int getAtlasSize() {
		return atlasSize;
	}

	public void setAtlasSize(int atlasSize) {
		this.atlasSize = atlasSize;
	}

	public boolean isAllowBackLighting() {
		return allowBackLighting;
	}

	public void setAllowBackLighting(boolean allowBackLighting) {
		this.allowBackLighting = allowBackLighting;
	}

	public ModelTexture(int textureID) {
		this.textureID = textureID;
	}

	public int getTextureID() {
		return textureID;
	}

	public float getShineDamper() {
		return shineDamper;
	}

	public void setShineDamper(float shineDamper) {
		this.shineDamper = shineDamper;
	}

	public float getReflectivity() {
		return reflectivity;
	}

	public void setReflectivity(float reflectivity) {
		this.reflectivity = reflectivity;
	}

	public boolean isHasTransparency() {
		return hasTransparency;
	}

	public void setHasTransparency(boolean hasTransparency) {
		this.hasTransparency = hasTransparency;
	}

}
