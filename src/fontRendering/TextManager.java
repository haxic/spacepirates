package fontRendering;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import fontMeshCreator.FontType;
import fontMeshCreator.GUIText;
import fontMeshCreator.TextMeshData;
import utils.Loader;

public class TextManager {
	private static Loader loader;
	private static Map<FontType, List<GUIText>> texts = new HashMap();
	private static FontRenderer fontRenderer;
	
	public static void init(Loader newLoader) {
		fontRenderer = new FontRenderer();
		loader = newLoader;
	}
	
	public static void loadText(GUIText text) {
		FontType font = text.getFont();
		TextMeshData data = font.loadText(text);
		int vaoID = loader.loadToVAO(data.getVertexPositions(), data.getTextureCoords());
		text.setMeshInfo(vaoID, data.getVertexCount());
		List<GUIText> textBatch = texts.get(font);
		if (textBatch == null) {
			textBatch = new ArrayList<GUIText>();
			texts.put(font, textBatch);
		}
		textBatch.add(text);
	}
	
	public static void render() {
		fontRenderer.render(texts);
	}
	
	public static void removeText(GUIText text) {
		List<GUIText> textBatch = texts.get(text.getFont());
		textBatch.remove(text);
		if (textBatch.isEmpty()) {
			texts.remove(text.getFont());
			loader.removeMesh(text.getMesh());
		}
	}
	
	public static void cleanUp() {
		fontRenderer.cleanUp();
	}
}
