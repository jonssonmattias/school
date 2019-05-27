package assignment5;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Is used to create a Server
 * 
 * @author TheComputer
 *
 */
public class Server {
	private boolean running;
	private ArrayList<ClientThread> al = new ArrayList<ClientThread>();
	private ServerGUI serverGUI;
	private GUI gui;

	public static void main(String argv[]) throws Exception {
		new Server(new GUI("Server"));
	}

	/**
	 * Constructs a ServerGUI object
	 * 
	 * @param serverGUI the GUI for the server
	 * @throws IOException
	 */
	public Server(GUI gui) throws IOException {
		this.gui=gui;
		gui.setServer(this);
		ServerSocket serverSocket = new ServerSocket(6789);
		gui.setMessage("Waiting for connections...");
		running = true;
		ExecutorService executor = Executors.newFixedThreadPool(5);//creating a pool of 5 threads  
		while(running) {
			Socket socket = serverSocket.accept(); 
			executor.execute(new ClientThread(socket));
			if(!running) executor.shutdown();
		}
	}
	/**
	 * Broadcast message to all users
	 * 
	 * @param message the message sent from the server
	 * @throws IOException
	 */
	public synchronized void broadcast(String message) throws IOException {
		for(int i=0;i<al.size();i++) {
			ClientThread ct = al.get(i);
			ct.sendMessage(message);
		}
	}

	/**
	 * Private class to handle clients
	 * 
	 * @author Mattias Jönsson
	 *
	 */
	private class ClientThread extends Thread{
		ObjectInputStream inFromClient;
		ObjectOutputStream outToClient;
		public ClientThread(Socket socket) {
			al.add(this);
			try {
				inFromClient = new ObjectInputStream(socket.getInputStream());
				outToClient = new ObjectOutputStream(socket.getOutputStream());
				outToClient.flush();
				gui.setMessage("New user connected");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		public synchronized void run() {
			boolean running = true;
			while(running) {
				try {
					Object obj = inFromClient.readObject();
					gui.setMessage(obj.toString());
					broadcast(obj.toString());
				}
				catch (IOException e) {
					System.out.println("Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
			}
		}
		private void sendMessage(String message) {
			try {
				outToClient.writeObject(message);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}


