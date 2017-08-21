package test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.nio.ByteBuffer;

import javax.swing.JFrame;

public class UDPClientTest {
	public static void main(String args[]) {
		// BufferedReader inFromUser = new BufferedReader(new
		// InputStreamReader(System.in));
		JFrame frame = new JFrame("FrameDemo");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);

		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket(6061);
			// InetAddress serverIPAddress =
			// InetAddress.getByName("5.186.124.218");
			InetAddress serverIPAddress = InetAddress.getByName("spacepirates.server.test");
			int serverPort = 6060;
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
				System.out.println("Send to server.");
				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, serverIPAddress, serverPort);
				clientSocket.send(sendPacket);
				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				packageReceiveCounter++;
				String modifiedSentence = new String(receivePacket.getData());
				int value = IntToByteConversionTest.byteArrayToInt(receivePacket.getData(), 0);
				System.out.println("FROM SERVER:" + value);
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
