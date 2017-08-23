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

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class TCPServerTest {
	public static void main(String[] args) throws IOException {
		JFrame frame = new JFrame("Server");
		JTextArea textArea = new JTextArea(30, 80);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.LIGHT_GRAY);
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));

		JScrollPane scrolltxt = new JScrollPane(textArea);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		// frame.add(textArea);
		frame.add(scrolltxt);
		frame.pack();
		frame.setVisible(true);

		// InetAddress serverAddress = InetAddress.getByName("localhost");
		 InetAddress serverAddress = InetAddress.getByName("192.168.1.215");
//		ServerSocket serverSocket = new ServerSocket(6031);
		 ServerSocket serverSocket = new ServerSocket(6061, 100, serverAddress);
		textArea.append("TCP server started. Awaiting connection." + "\n");
		Socket clientSocket = serverSocket.accept();
		textArea.append("Client connected: " + clientSocket.getInetAddress() + "\n");
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		byte[] receiveData = new byte[504];
		byte[] sendData = new byte[504];
		Pinger pinger = new Pinger();
		int packageReceiveCounter = 0;
		while (packageReceiveCounter < 1000) {

			String output = in.readLine();
			packageReceiveCounter++;
			textArea.append(pinger.onReceive() + "\n");
			textArea.setCaretPosition(textArea.getDocument().getLength());

			out.println(output);
			pinger.onSend();
		}
		clientSocket.close();
		textArea.append("Connection closed.\n");
		textArea.setCaretPosition(textArea.getDocument().getLength());
	}
}
