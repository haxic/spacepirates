package server;

import java.io.IOException;
import java.util.PriorityQueue;
import java.util.Queue;

import utility.SocketHandler;

public class ClientHandler extends Thread implements Runnable {
	private SocketHandler socketHandler;
	public Queue<String> inputBuffer;
	private final String inetAddress;

	public ClientHandler(SocketHandler socketHandler) {
		System.out.println(socketHandler.toString() + " connected!");
		this.socketHandler = socketHandler;
		this.inetAddress = socketHandler.socket.getInetAddress().toString();
		this.inputBuffer = new PriorityQueue<String>();
		setDaemon(true);
		setName("Client " + socketHandler.toString());
		start();
	}

	public String getID() {
		return inetAddress;
	}

	public void print(String msg) throws IOException {
		socketHandler.print(msg);
	}

	@Override
	public void run() {
		while (true) {
			try {
				inputBuffer.add(socketHandler.readLine());
			} catch (IOException e) {
				socketHandler.disconnect();
				ServerHandler.getInstance().clients.remove(this);
				return;
			}
		}
	}
}
