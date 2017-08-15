package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;

public class UDPServerTest {
	public static void main(String[] args) throws IOException {
		DatagramSocket serverSocket = new DatagramSocket(null);
		InetSocketAddress serverAddress = new InetSocketAddress("192.168.1.215", 6060);
		serverSocket.bind(serverAddress);
		byte[] receiveData = new byte[504];
		byte[] sendData = new byte[504];
		Pinger pinger = new Pinger();
		while (true) {
			DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
			serverSocket.receive(receivePacket);
			pinger.onReceive();
			String sentence = new String(receivePacket.getData());

			InetAddress IPAddress = receivePacket.getAddress();
			int port = receivePacket.getPort();
			String capitalizedSentence = sentence.toUpperCase();
			sendData = capitalizedSentence.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
			serverSocket.send(sendPacket);
			pinger.onSend();
		}
	}
}
