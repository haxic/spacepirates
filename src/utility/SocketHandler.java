package utility;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.Socket;

public class SocketHandler {
	public final Socket socket;
	private final BufferedReader in;
	private final DataOutputStream out;


	public SocketHandler(Socket socket) throws IOException {
		this.socket = socket;
		this.out = new DataOutputStream(socket.getOutputStream());
		this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	}

	public void print(String msg) throws IOException {
		out.writeBytes(msg + "\r\n");
	}

	public void println(String msg) throws IOException {
		print(msg + "\r\n");
	}

	public String readLine() throws IOException {
		return in.readLine();
	}

	public final void disconnect() {
		System.out.println("DICONNECT");
		try {
			if (!socket.isClosed()) {
				socket.shutdownOutput();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}