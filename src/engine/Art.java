package engine;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class Art {
	// public static BufferedImage[][] guys = split(load("/guys.png"), 6, 6);
	// public static BufferedImage[][] _stars = split2d(load("stars.png"), 1,
	// 1);
//	public static BufferedImage[] _ANT_RUN = split(load("ant.png"), 32, 32, 0);
	// public static BufferedImage bot = load("bot.png");

	// public static BufferedImage[][] player2 =
	// mirrorsplit(load("/player.png"), 16, 32);
	// public static BufferedImage[][] walls = split(load("/walls.png"), 10,
	// 10);
	// public static BufferedImage[][] gremlins = split(load("/gremlins.png"),
	// 30, 30);
	 public static BufferedImage TILE_GRASS = load("grass.png");
	 public static BufferedImage RESOURCE_LUMBER = load("lumber.png");
	 public static BufferedImage RESOURCE_STONE = load("stone.png");
	 public static BufferedImage RESOURCE_IRON = load("iron.png");
	 public static BufferedImage RESOURCE_TRUESILVER = load("truesilver.png");
	 public static BufferedImage RESOURCE_CRYSTALS = load("crystals.png");
	 public static BufferedImage RESOURCE_GEMS = load("gems.png");
	 public static BufferedImage RESOURCE_MITHRIL = load("mithril.png");
	 public static BufferedImage RESOURCE_ADAMANTINE = load("adamantine.png");
	 public static BufferedImage RESOURCE_TREASURE = load("gold.png");

	// public static BufferedImage level = load("/levels.png");
	// public static BufferedImage titleScreen = load("/titlescreen.png");
	// public static BufferedImage winScreen1 = load("/winscreen1.png");;
	// public static BufferedImage winScreen2 = load("/winscreen2.png");;

	public static BufferedImage load(String name) {
		try {
//			BufferedImage org = ImageIO.read(Art.class.getResource(name));
			BufferedImage org = ImageIO.read(new File("res/" + name));
			BufferedImage res = new BufferedImage(org.getWidth(), org.getHeight(), BufferedImage.TYPE_INT_ARGB);
			Graphics g = res.getGraphics();
			g.drawImage(org, 0, 0, null, null);
			g.dispose();
			return res;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	static BufferedImage scale(BufferedImage src, int scale) {
		int w = src.getWidth() * scale;
		int h = src.getHeight() * scale;
		BufferedImage res = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		Graphics g = res.getGraphics();
		g.drawImage(src.getScaledInstance(w, h, Image.SCALE_AREA_AVERAGING), 0, 0, null);
		g.dispose();
		return res;
	}

	private static BufferedImage[][] mirrorsplit2d(BufferedImage src, int xs, int ys) {
		int xSlices = src.getWidth() / xs;
		int ySlices = src.getHeight() / ys;
		BufferedImage[][] res = new BufferedImage[xSlices][ySlices];
		for (int x = 0; x < xSlices; x++) {
			for (int y = 0; y < ySlices; y++) {
				res[x][y] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
				Graphics g = res[x][y].getGraphics();
				g.drawImage(src, xs, 0, 0, ys, x * xs, y * ys, (x + 1) * xs, (y + 1) * ys, null);
				g.dispose();
			}
		}
		return res;
	}

	private static BufferedImage[][] split2d(BufferedImage src, int xs, int ys) {
		int xSlices = src.getWidth() / xs;
		int ySlices = src.getHeight() / ys;
		BufferedImage[][] res = new BufferedImage[xSlices][ySlices];
		for (int y = 0; y < xSlices; y++) {
			for (int x = 0; x < ySlices; x++) {
				res[y][x] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
				Graphics g = res[y][x].getGraphics();
				g.drawImage(src, -y * xs, -x * ys, null);
				g.dispose();
			}
		}
		return res;
	}

	private static BufferedImage[] split(BufferedImage src, int xs, int ys, int row) {
		int xSlices = src.getWidth() / xs;
		BufferedImage[] res = new BufferedImage[xSlices];
		for (int x = 0; x < xSlices; x++) {
			res[x] = new BufferedImage(xs, ys, BufferedImage.TYPE_INT_ARGB);
			Graphics g = res[x].getGraphics();
			g.drawImage(src, -x * xs, ys * -row, null);
			g.dispose();
		}
		return res;
	}

	private static BufferedImage[] mirrorsplit(BufferedImage src, int xs, int ys, int row) {
		int xSlices = src.getWidth() / xs;
		BufferedImage[] res = new BufferedImage[xSlices];
		for (int x = 0; x < xSlices; x++) {
			res[x] = new BufferedImage(xs, ys * (row + 1), BufferedImage.TYPE_INT_ARGB);
			Graphics g = res[x].getGraphics();
			g.drawImage(src, xs, 0, 0, ys,
							 x * xs, ys * row,
							 (x + 1) * xs, ys * row + ys, null);
//			dx1 - the x coordinate of the first corner of the destination rectangle.
//			dy1 - the y coordinate of the first corner of the destination rectangle.
//			dx2 - the x coordinate of the second corner of the destination rectangle.
//			dy2 - the y coordinate of the second corner of the destination rectangle.
//			sx1 - the x coordinate of the first corner of the source rectangle.
//			sy1 - the y coordinate of the first corner of the source rectangle.
//			sx2 - the x coordinate of the second corner of the source rectangle.
//			sy2 - the y coordinate of the second corner of the source rectangle.
			g.dispose();
			// int dx1, dy1, dx2, dy2, sx1, sy1, sx2, sy2;
			// g.drawImage(src, xs, 0, 0, ys, x * xs, y * ys, (x + 1) * xs, (y +
			// 1) * ys, null);
		}
		return res;
	}
}
