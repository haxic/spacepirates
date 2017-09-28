package test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TCPObject implements Runnable {
	Socket socket;
	BufferedReader in;
	PrintWriter out;
	Pinger pinger;
	byte[] receiveData;
	byte[] sendData;
	Thread thread;
	Queue<String> received;
	private boolean disconnected;

	public TCPObject(Socket socket) throws IOException {
		this.socket = socket;
		in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		out = new PrintWriter(socket.getOutputStream(), true);
		pinger = new Pinger();
		receiveData = new byte[504];
		sendData = new byte[504];
		received = new ConcurrentLinkedQueue();
		thread = new Thread(this);
		thread.start();
	}

	@Override
	public void run() {
		while (!socket.isClosed() && socket.isConnected() && !socket.isInputShutdown() && !socket.isOutputShutdown()) {
			String output;
			try {
				output = in.readLine();
			} catch (IOException e) {
				e.printStackTrace();
				received.add(e.toString());
				break;
			}
			received.add(output + " " + pinger.onReceive());
			out.println(output);
			pinger.onSend();
		}
		try {
			if (!socket.isClosed())
				socket.close();
		} catch (IOException e) {
			e.printStackTrace();
			received.add(e.toString());
		}
		received.add("Connection closed.");
		disconnected = true;
	}

	public boolean isDisconnected() {
		return disconnected;
	}
}
