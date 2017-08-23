package test;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import javax.swing.JFrame;
import javax.swing.JTextArea;

public class UDPClientTest {
	public static void main(String args[]) {
		// BufferedReader inFromUser = new BufferedReader(new
		// InputStreamReader(System.in));
		JFrame frame = new JFrame("FrameDemo");
		JTextArea textArea = new JTextArea(30, 80);
		textArea.setBackground(Color.BLACK);
		textArea.setForeground(Color.LIGHT_GRAY);
		textArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(textArea);
		frame.pack();
		frame.setVisible(true);
		// frame.setSize(new Dimension(500, 500));
		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();

			String serverIP = "5.186.147.73";
			// String serverIP = "192.168.1.215";
			// String serverIP = "localhost";
			int serverPort = 6030;
			InetAddress serverIPAddress = InetAddress.getByName(serverIP);

			textArea.append("Server ip: " + serverIPAddress + "\n");
			byte[] sendData = new byte[504];
			byte[] receiveData = new byte[504];
			int packageReceiveCounter = 0;
			int packageSendCounter = 0;
			while (packageSendCounter < 1000) {
				// String sentence = inFromUser.readLine();
				byte[] sendPSC = IntToByteConversionTest.intToByteArray(++packageSendCounter, 0);
				sendData[0] = sendPSC[0];
				sendData[1] = sendPSC[1];
				sendData[2] = sendPSC[2];
				sendData[3] = sendPSC[3];
				textArea.append("Send data to server: " + packageSendCounter + "\n");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverPort);
				clientSocket.send(sendPacket);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				packageReceiveCounter++;
				String modifiedSentence = new String(receivePacket.getData());
				int value = IntToByteConversionTest.byteArrayToInt(receivePacket.getData(), 0);
				textArea.append("Received from server: " + value + "\n");
			}
		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		clientSocket.close();
	}

	public static void asd() {
		byte[] sendData = new byte[504];
		int value = Integer.parseInt("4");
		System.out.println(value);
		byte[] inInt = intToByteArray(value);
		System.out.println(inInt.length);
		for (int i = 0; i < 3; i++)
			sendData[i] = inInt[i];

		int test = ByteBuffer.wrap(new byte[] { sendData[0], sendData[1], sendData[2], sendData[3] }).getInt();
	}

	public static byte[] intToByteArray(int value) {
		return new byte[] { (byte) (value >>> 24), (byte) (value >>> 16), (byte) (value >>> 8), (byte) value };
	}
}
