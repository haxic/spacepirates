package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import utility.SocketHandler;

public class ServerHandler extends Thread implements Runnable {
	private static ServerHandler instance = null;
	List<ClientHandler> clients = new ArrayList<ClientHandler>();
	int serverListCounter = 0;
	ServerSocket connectionSocket;

	public static ServerHandler getInstance() {
		if (instance == null) {
			instance = new ServerHandler();
		}
		return instance;
	}

	@Override
	public void run() {
		try {
			connectionSocket = new ServerSocket(5001);
		} catch (IOException e) {
			e.printStackTrace();
		}
		while (true) {
			try {
				System.out.println("awaiting connection ...");
				clients.add(new ClientHandler(new SocketHandler(connectionSocket.accept())));
			} catch (SocketException e) {
				// do nothing
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
