package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UDPServerTest {
	public static void main(String[] args) throws IOException {
		DatagramSocket serverSocket = new DatagramSocket(null);
		int serverPort = 6030;
		InetSocketAddress serverAddress = new InetSocketAddress("192.168.1.215", serverPort);
//		InetSocketAddress serverAddress = new InetSocketAddress("localhost", serverPort);
		System.out.println("Server started.");
		serverSocket.bind(serverAddress);
		byte[] receiveData = new byte[504];
		byte[] sendData = new byte[504];
		Pinger pinger = new Pinger();
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			System.out.println(pinger.onReceive());
			sendData = receivePacket.getData();

			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();

			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			pinger.onSend();
		}
	}
}
