package temptemp;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;



/**
 * @author Linnea dahlgren & Johan Lovberg
 * @version 1.0
 * @since 2019-03-13
 *
 * The client class holds and sets up the streams for the connection to the server
 * It also holds the Socket for the connection
 */
public class Client {
	private final Socket socket;
	private ObjectOutputStream oos;
	private ObjectInputStream ois;
	
	/**
	 * @param socket
	 * Sets the socket
	 */
	public Client(Socket socket) {
		this.socket = socket;
	}
	
	public final Socket getSocket() {
		return socket;
	}
	
	/**
	 * @return an ObjectOutputStream
	 * 
	 */
	public final ObjectOutputStream getOos() {
		if (oos == null) {
			try {
				oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				oos.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return oos;
	}
	
	/**
	 * @returns an ObjectInputStream
	 */
	public final ObjectInputStream getOis() {
		if (ois == null) {
			try {
				ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return ois;
	}
}
