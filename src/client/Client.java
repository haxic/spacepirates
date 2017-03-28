package client;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import engine.Engine;
import engine.KeyboardInput;
import engine.RenderObject;
import engine.SystemLoop;
import engine.V3;
import game.Room;
import game.Unit;
import server.ClientHandler;
import utility.SocketHandler;

public class Client implements Engine {
	Unit player;
	public int width = 20;
	public int height = 20;
	public Map<Integer, Unit> units;
	ClientHandler clientHandler;
	int selectedHero = 0;
	String name = "Haxic";

	public Client(int width, int height) {
		units = new HashMap<Integer, Unit>();
		try {
			clientHandler = new ClientHandler(new SocketHandler(new Socket("localhost", 5001)));
			clientHandler.print("name " + name);
		} catch (IOException e) {
			System.out.println("Failed to connect to server...");
		}
		SystemLoop systemLoop = new SystemLoop(this);
		systemLoop.start();

	}

	@Override
	public void update(double delta) {
		if (clientHandler != null)
			while (!clientHandler.inputBuffer.isEmpty()) {
				String msg = clientHandler.inputBuffer.poll();
				if (msg == null)
					continue;
				String[] words = msg.split(" ");
				// System.out.println("from server: " + words.length + " ---> "+
				// msg);
				int i = 0;
				while (i < words.length)
					switch (words[i++]) {
					case "new": {
						int id = Integer.parseInt(words[i++]);
						i++;
						String name = words[i++];
						i++;
						double x = Double.parseDouble(words[i++]);
						double y = Double.parseDouble(words[i++]);
						double z = Double.parseDouble(words[i++]);
						V3 position = new V3(x, y, z);
						Unit newUnit = new Unit(id, name, position);
						if (name.equals(this.name)) {
							player = newUnit;
							System.out.println("THIS IS ME !");
						}
						units.put(id, newUnit);
					}
						break;
					case "move": {
						int id = Integer.parseInt(words[i++]);
						if (!units.containsKey(id))
							return;
						Unit unit = units.get(id);
						i++;
						double x = Double.parseDouble(words[i++]);
						double y = Double.parseDouble(words[i++]);
						double z = Double.parseDouble(words[i++]);
						unit.position.x = x;
						unit.position.y = y;
						unit.position.z = z;
						i++;
						boolean onGround = Boolean.parseBoolean(words[i++]);
						unit.onGround = onGround;
					}
						break;
					}
			}
	}

	@Override
	public void input(KeyboardInput input, RenderObject renderObject) {
		if (player == null)
			return;
		try {
			if (input.kb_keyDown(KeyEvent.VK_W) && player.onGround) {
				clientHandler.print("move up");
			} else if (input.kb_keyDown(KeyEvent.VK_A)) {
				clientHandler.print("move left");
			} else if (input.kb_keyDown(KeyEvent.VK_D)) {
				clientHandler.print("move right");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void render(RenderObject r) {
		r.render(this, player);
	}

	public static void main(String[] args) {
		new Client(10, 10);
	}
}
