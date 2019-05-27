package assignment5;
import java.net.*;
import java.io.*;

/**
 * Is used to create a Client
 * 
 * @author TheComputer
 *
 */
public class Client {
	private ObjectOutputStream outToServer;
	private ObjectInputStream inFromServer;
	private ClientGUI clientGUI;
	private GUI gui;

	public static void main(String[] args) {
		new Client("localhost", 6789, new GUI("Client"));
	}
	
	/**
	 * Constructs a Client object
	 * 
	 * @param port the port of the server
	 * @param clientGUI
	 */
	public Client(String ipAddress, int port, GUI gui) {
		this.gui=gui;
		gui.setClient(this);
		Socket clientSocket;
		try {
			clientSocket = new Socket(ipAddress, port);
			outToServer = new ObjectOutputStream(clientSocket.getOutputStream());
			inFromServer = new ObjectInputStream(clientSocket.getInputStream());
			new ListenFromServer().start();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Sends a message to the server
	 * 
	 * @param message the message to be sent
	 */
	public synchronized void sendMessage(String message) {
		try {
			outToServer.writeObject(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Private class for listen from the server
	 * 
	 * @author Mattias Jönsson
	 *
	 */
	private class ListenFromServer extends Thread{
		public synchronized void run() {
			while(true) {
				try {
					Object obj = inFromServer.readObject();
					if(obj instanceof String) {
						gui.setMessage(obj.toString());
					}
				}
				catch(IOException e) {
					break;
				}
				catch(ClassNotFoundException e) {
					e.printStackTrace();
				}
			}
		}
	}
}

