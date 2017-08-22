package test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPServerTest {
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(6061);
		System.out.println("TCP server started!");
		Socket clientSocket = serverSocket.accept();
		BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
		PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
		byte[] receiveData = new byte[504];
		byte[] sendData = new byte[504];
		Pinger pinger = new Pinger();
		while (true) {

			String output = in.readLine();
			pinger.onReceive();

			out.println(output);
			pinger.onSend();
		}
	}
}
