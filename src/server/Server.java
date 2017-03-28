package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import engine.Engine;
import engine.KeyboardInput;
import engine.RenderObject;
import engine.SystemLoop;
import engine.V3;
import game.Unit;
import game.Room;

public class Server extends Thread implements Runnable {
	public Room room;
	public int unitIDCounter = 0;
	public Map<String, Unit> players;
	ServerHandler serverHandler;

	public Server() {
		players = new HashMap<String, Unit>();
		room = new Room();
		serverHandler = new ServerHandler();
		start();
		serverHandler.start();
	}
	
	private boolean running = true;

	@Override
	public void run() {
	    double ns = 1000000000.0 / 60.0;
	    double delta = 0;

	    long lastTime = System.nanoTime();

	    while (running) {
	        long now = System.nanoTime();
	        delta += (now - lastTime) / ns;
	        lastTime = now;

	        while (delta >= 1) {
	        	update();
	            delta--;
	        }
	    }
	}

	public static void main(String[] args) {
		new Server();
	}

	V3 newPos = new V3();

	public void update() {
		String globalMessages = "";
		for (ClientHandler clientHandler : serverHandler.clients) {
			while (!clientHandler.inputBuffer.isEmpty()) {
				String msg = clientHandler.inputBuffer.poll();
				// System.out.println("From client \"" + clientHandler.getID() +
				// "\": " + msg);
				if (msg == null)
					continue;
				String[] words = msg.split(" ");
				int i = 0;
				switch (words[i++]) {
				case "name":
					String name = words[i++];
					Unit newUnit = new Unit(unitIDCounter++, name, new V3(Math.random() * 20 * 10 + 100, (Math.random() * 20 * 10 + 100), 0));
					players.put(clientHandler.getID(), newUnit);
					room.units.add(newUnit);
					if (!globalMessages.isEmpty())
						globalMessages += " ";
					globalMessages += "new " + newUnit.id + " name " + newUnit.name + " pos " + newUnit.position;
					break;
				case "move":
					Unit moveUnit = players.get(clientHandler.getID());
					String direction = words[i++];
					if (direction.equals("up") && moveUnit.onGround)
						moveUnit.velocity.y -= 5;
					if (direction.equals("left"))
						moveUnit.velocity.x -= 0.3 * (moveUnit.onGround ? 1 : 0.2);
					if (direction.equals("right"))
						moveUnit.velocity.x += 0.3 * (moveUnit.onGround ? 1 : 0.2);
					break;
				}
			}
		}

		for (Unit unit : room.units) {
			unit.velocity.y += 0.1;
			newPos.x = unit.position.x + unit.velocity.x;
			newPos.y = unit.position.y + unit.velocity.y;
			unit.velocity.mul(0.2);
			if (unit.velocity.magnitude() < 0.02) {
				unit.velocity.x = 0;
				unit.velocity.y = 0;
			}
			if (newPos.x - RenderObject.tileDimensionHalf.x < 0) {
				newPos.x = RenderObject.tileDimensionHalf.x;
				unit.velocity.x = 0;
			}
			if (newPos.x + RenderObject.tileDimensionHalf.x > room.width * RenderObject.tileDimension.x) {
				newPos.x = room.width * RenderObject.tileDimension.x - RenderObject.tileDimensionHalf.x;
				unit.velocity.x = 0;
			}
			if (newPos.y - RenderObject.tileDimensionHalf.y < 0) {
				newPos.y = RenderObject.tileDimensionHalf.y;
				unit.velocity.y = 0;
			}
			if (newPos.y + RenderObject.tileDimensionHalf.y > room.width * RenderObject.tileDimension.y) {
				newPos.y = room.width * RenderObject.tileDimension.y - RenderObject.tileDimensionHalf.y;
				unit.velocity.y = 0;
				unit.onGround = true;
			} else {
				unit.onGround = false;
			}
			if (newPos.x == unit.position.x && newPos.y == unit.position.y)
				continue;
			unit.position.x = newPos.x;
			unit.position.y = newPos.y;
			//System.out.println(newPos + " " + unit.position);
			if (!globalMessages.isEmpty())
				globalMessages += " ";
			globalMessages += "move " + unit.id + " pos " + unit.position + " ground " + unit.onGround;
		}
		if (!globalMessages.isEmpty()) {
			//System.out.println("To clients: " + globalMessages);
			for (ClientHandler clientHandler : serverHandler.clients) {
				try {
					clientHandler.print(globalMessages);
					// System.out.println("To client \"" + clientHandler.getID()
					// + "\": " + msg);
				} catch (IOException e) {
					// System.out.println("To client (FAILED!) \"" +
					// clientHandler.getID() + "\": " + msg);
					e.printStackTrace();
				}
			}
		}
	}
}
