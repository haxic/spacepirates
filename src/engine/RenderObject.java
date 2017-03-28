package engine;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Map;

import client.Client;
import game.Room;
import game.Unit;

public class RenderObject {
	public Graphics2D g;
	public V3 screenSize; // full screen
	public V3 screenSizeHalf; // half screen
	public V3 camera;
	public static V3 tileDimension = new V3(20, 20, 0);
	public static V3 tileDimensionHalf = new V3(10, 10, 0);

	public RenderObject(Graphics2D g, V3 screenSize, V3 screenSizeHalf) {
		this.g = g;
		this.screenSize = screenSize;
		this.screenSizeHalf = screenSizeHalf;
	}

	public void render(Client client, Unit player) {
		if (client == null || player == null)
			return;
		setCamera(player.position);
		for (int x = 0; x < client.width; x++) {
			for (int y = 0; y < client.height; y++) {
				g.setColor(Color.WHITE);
				V3 position = getCoordinateOnScreen(new V3(x * 20, y * 20, 0));
				g.fillRect((int) position.x, (int) position.y, (int) tileDimension.x, (int) tileDimension.y);
			}
		}
		
		  for (Map.Entry<Integer, Unit> entry : client.units.entrySet()) {
		        // Integer id = entry.getKey();
		        Unit unit = entry.getValue();
		        g.setColor(Color.RED);
		        V3 position = getCoordinateOnScreen(unit.position).sub(tileDimensionHalf);
		        g.fillRect((int) position.x, (int) position.y, (int) tileDimension.x, (int) tileDimension.y);
		    }
	}

	public V3 getCoordinateOnScreen(V3 p) {
		return p.sub(camera).add(screenSizeHalf);
	}

	public void setCamera(V3 position) {
		if (position == null)
			return;
		camera = position;
	}

}
