package temptemp;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Date;


/**
 * 
 * @author Ludvid Juelsson and Oscar Jonsson
 * @version 1.0
 * @since 2019-03-13
 *The ServerController class works as the server for the program
 */
public class ServerController {
	private ServerViewer viewer;
	private int port = 1479;
	private ServerSocket socket;
	private ServerThread thread;
	private Clients clients = new Clients();
	private UnsentMessages unsentMessages = new UnsentMessages();
	
	/**
	 * The constructor takes an implementation of serverViewer as its UI.
	 * @param viewer
	 */
	public ServerController(ServerViewer viewer) {
		this.viewer = viewer;
		this.viewer.setController(this);
		this.viewer.setPort(port);
	}
	/**
	 * The servers starts and gets a port from the UI
	 * @param newPort
	 */
	public synchronized void start(int newPort) {
		this.port = newPort;
		try {
			// Configure server
			if (viewer != null) { // Check if viewer assigned, else run without UI
				port = viewer.getPort(); // Get desired port
				log("Port set to " + port + "."); // Log
				
				String ip = "localhost";   //InetAddress.getLocalHost().getHostAddress(); // Get current IP
				viewer.setIp(ip); // Update viewer with current IP
				log("IP address is " + ip + "."); // Log
			}
			
			// Start server
			log("Starting server..."); // Log
			socket = new ServerSocket(port); // Create socket
			if (thread == null) { // Check if server already running
				thread = new ServerThread();
				thread.start(); // Dispatch server thread
			}
		} catch (IOException e) {
			log("Could not start server!"); // Log
		}
	}
	/**
	 * 
	 * Stopping and disconnecting the server
	 */
	public synchronized void stop() {
		log("Stopping server..."); // Log
		if (socket == null) {
			log("Server is not running!"); // Log
			return;
		}
		try {
			socket.close();
		} catch (IOException e) {
			log("Server is not running!"); // Log
			return;
		}
	}
	/**
	 * 
	 * log is a method that writes the log information to the ServerUI.
	 * @param log
	 */
	private synchronized void log(String log) {
		if (viewer != null) { // Check if viewer is assigned
			viewer.addText(new Date() + ": " + log); // Add log to viewer
		}
	}
	
	/**
	 * 
	 * Notifies all the connected clients of a new connected client.
	 * @param newUser
	 */
	
	private synchronized void updateClients() {
		log("Updating connected users..."); // Log
		Users users = new Users(clients.keySet());
		// Loop through all connected users
		for (User user : users) {
			try {
				ObjectOutputStream oos = clients.get(user).getOos();
				oos.writeObject(users); // Send list of users
				oos.flush();
			} catch (IOException e) {
				log(user.getUsername() + " lost connection!"); // Log
			}
		}
	}
	
	/**
	 * 
	 * Sends message to the chosen recipients.
	 * @param message
	 */
	
	private synchronized void sendMessage(Message message) {
		// Send message to all recipients
		send(message.getSender(), message);
		for (User user : message.getRecipients()) {
			send(user, message);
		}
		log(message.getSender().getUsername() + " (To " + message.getRecipients() + ") > " + message.getText() + " | " + ((message.getImage() != null) ? message.getImage() : "No image")); // Log
	}
	
	/**
	 * 
	 * Sends message to a selected user.
	 * @param user
	 * @param message
	 */
	private synchronized void send(User user, Message message) {
		try {
			Client client = clients.get(user);
			// User online
			if (client != null) {
				message.sent();
				client.getOos().writeObject(message);
				client.getOos().flush();
			// User offline
			} else {
				log(user.getUsername() + " is offline. Message stored for later."); // Log
				unsentMessages.put(user, message);
			}
		} catch (IOException e) {
			log(user.getUsername() + " lost connection!"); // Log
		}
	}
	
	/**
	 * 
	 * 
	 * @author Oscar Jonsson & Ludvig Juelsson
	 * Each client that is accepted is given a separate thread in ClientHandler.
	 * Starts server thread, writes server information in ServerUI.
	 *
	 */
	private class ServerThread extends Thread {
		public ServerThread() {
			super("Server Thread (" + port + ")");
		}
		
		public void run() {
			log("Server started."); // Log
			while(!socket.isClosed()) {
				try {
					Client client = new Client(socket.accept()); // Accept incoming connections
					log(client.getSocket().getInetAddress().getHostAddress() + " is connecting..."); // Log
					new ClientHandler(client).start(); // Dispatch client thread
				} catch (IOException e) {
					log("A problem occured. The server probably stopped."); // Log
				}
			}
			thread = null;
			log("Server stopped."); // Log
		}
	}
	
	/**
	 * 
	 * @author Oscar Jonsson & Ludvig Juelsson
	 * Every online client is given a seperate thread. This is to handle a multitude 
	 * of clients at the same time.
	 * This inner class also handles the ObjectStreams and logging to the ServerUI.
	 *
	 */
	private class ClientHandler extends Thread {
		private User user;
		private final Client client;
		
		public ClientHandler(Client client) {
			super("Client Thread");
			this.client = client;
		}
		
		public void run() {
			final ObjectOutputStream oos = client.getOos();
			try {
				oos.flush();
			} catch (IOException e2) {
				log("User lost connection!");
			}
			final ObjectInputStream ois = client.getOis();
			try {
				user = (User)ois.readObject(); // Wait for client to identify itself
				if (clients.get(user) != null) { // Check if username available
					oos.writeUTF("USER_TAKEN");
					oos.close();
					log("Username " + user.getUsername() + " already taken! Closed connection with client."); // Log
				} else { // Username okay
					clients.put(user, client);
					oos.writeUTF("USER_OK");
					oos.flush();
					log(user.getUsername() + " connected with address " + client.getSocket().getInetAddress().getHostAddress() + "."); // Log
					updateClients(); // Notify everyone with an updated online list
					
					// Check if user has pending messages
					ArrayList<Message> unsent = unsentMessages.get(user);
					if (unsent != null && unsent.size() != 0) {
						log(user.getUsername() + " has pending messages."); // Log
						for (Message message : unsent) {
							send(user, message);
						}
					}
				}
			} catch (ClassNotFoundException e) {
				try {
					oos.writeUTF("ERROR");
					oos.close();
					log("Unable to receive user information!"); // Log
				} catch (IOException e1) {
					log("User lost connection!"); // Log
				}
				log("Unable to receive user information!"); // Log
			} catch (IOException e) {
				e.printStackTrace();
				log("User lost connection!3"); // Log
			}
			while (!client.getSocket().isClosed() && !socket.isClosed()) {
				try {
					Object o = ois.readObject();
					if (o instanceof User) {
						clients.remove(user);
						user = (User)o;
						System.out.println("Update image: " + user.getImage());
						clients.put(user, client);
						updateClients();
					} else if (o instanceof Message) {
						Message message = (Message)o;
						System.out.println("Message image: " + message.getImage());
						message.received(); // Time received
						sendMessage(message); // Send message to recipients
					}
				} catch (ClassNotFoundException e) {
					log("Unable to receive message!"); // Log
				} catch (IOException e) {
					log("User lost connection!4"); // Log
					try {
						oos.close(); // Flush and close connection
					} catch (IOException e1) {}
				}
			}
			try {
				oos.close(); // Flush and close connection
			} catch (IOException e) {}
			clients.remove(user); // Remove user from online list
			updateClients(); // Notify clients of updated online list
			log(user.getUsername() + " disconnected."); // Log
		}
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public static void main(String[] args) {
		ServerController server = new ServerController(new ServerUI());
	}
}
