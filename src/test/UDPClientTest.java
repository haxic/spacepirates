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

		// 2. Optional: What happens when the frame closes?
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// 3. Create components and put them in the frame.

		// 4. Size the frame. 
		frame.pack();

		// 5. Show it.
		frame.setVisible(true);

		DatagramSocket clientSocket = null;
		try {
			clientSocket = new DatagramSocket();
			// 5.186.124.218
			InetAddress IPAddress = InetAddress.getByName("5.186.124.218");
			//clientSocket = new DatagramSocket(null);
			//InetSocketAddress clientAddress = new InetSocketAddress("localhost", 6096);
			//clientSocket.bind(clientAddress);
			while (true) {
				byte[] sendData = new byte[504];
				byte[] receiveData = new byte[504];
				// String sentence = inFromUser.readLine();
				String sentence = "A" + Double.toString(Math.random()) + "A";
				sendData = sentence.getBytes();

				DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, 6096);
				// DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length);

				clientSocket.send(sendPacket);

				DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
				clientSocket.receive(receivePacket);
				String modifiedSentence = new String(receivePacket.getData());

				System.out.println("FROM SERVER:" + modifiedSentence);
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
