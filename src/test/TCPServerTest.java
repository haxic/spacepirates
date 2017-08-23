package test;

import java.awt.Color;
import java.awt.Font;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TCPServerTest implements Runnable {
	final String serverIP = "192.168.1.215";
	final int serverPort = 6031;
	InetAddress serverAddress;
	ServerSocket serverSocket;
	JFrame frame;
	List<TCPObject> connections = new ArrayList<TCPObject>();
	Thread thread;
	JTextArea textArea;

	public TCPServerTest() {
		textArea = new JTextArea(30, 80);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.LIGHT_GRAY);
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		JScrollPane scrolltxt = new JScrollPane(textArea);
		frame = new JFrame("Server Console");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.add(textArea);
		frame.add(scrolltxt);
		frame.pack();
		frame.setVisible(true);

		try {
			serverAddress = InetAddress.getByName(serverIP);
		} catch (UnknownHostException e) {
			e.printStackTrace();
			textArea.append(e + "\n");
			return;
		}
		try {
			serverSocket = new ServerSocket(serverPort, 100, serverAddress);
		} catch (IOException e) {
			e.printStackTrace();
			textArea.append(e + "\n");
			return;
		}
		textArea.append("TCP server started. Using port: " + serverPort + ". Awaiting connection." + "\n");
		thread = new Thread(this);
		thread.start();
		while (true) {
			Socket clientSocket;
			try {
				System.out.println("Awaiting connection.");
				clientSocket = serverSocket.accept();
				System.out.println("Client connected. " + clientSocket.getInetAddress());
				textArea.append("Client connected: " + clientSocket.getInetAddress() + "\n");
				connections.add(new TCPObject(clientSocket));
			} catch (IOException e) {
				e.printStackTrace();
				textArea.append(e + "\n");
			}
		}
	}

	@Override
	public void run() {
		while (true) {
			List<TCPObject> disconnections = new ArrayList<>();
			for (TCPObject connection : connections) {
				while (!connection.received.isEmpty()) {
					textArea.append(connection.received.poll() + "\n");
				}
				if (connection.isDisconnected()) {
					disconnections.add(connection);
					continue;
				}
			}
			for (TCPObject tcpObject : disconnections) {
				connections.remove(tcpObject);
				textArea.append(tcpObject.socket.getInetAddress() + " removed. Active connections: "
						+ connections.size() + ".\n");
			}
			try {
				thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public static void main(String[] args) {
		new TCPServerTest();
	}

}
