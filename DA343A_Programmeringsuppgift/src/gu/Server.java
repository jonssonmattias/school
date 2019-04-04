//package gu;
//
//import java.io.*;
//import java.net.*;
//import java.text.*;
//import java.util.*;
//import javax.swing.*;
//
///**
// * Is used to the create a server
// * 
// * @author Markus Masalkovski, Mattias Jönsson, Ramy Behnam, Lukas Rosberg, SofieLjungcrantz
// *
// */
//public class Server {
//	private ArrayList<ClientThread> al;
//	private ArrayList<Message> messages;
//	private ArrayList<User> users;
//	private ArrayList<User> userList;
//	private HashMap<Socket,Message> unsentMessages;
//	private ServerUI serverUI;
//	private SimpleDateFormat sdf;
//	private int port;
//	private boolean running;
//	private HashMap<String, Socket> clients;
//
//	/**
//	 * Constructs a Server-object
//	 * 
//	 * @param port the port of the server
//	 */
//	public Server(int port) {
//		this(port, null);
//	}
//	/**
//	 * Constructs a Server-object
//	 * 
//	 * @param port the port of the server
//	 * @param serverUI the serverUI
//	 */
//	public Server(int port, ServerUI serverUI){
//		this.serverUI = serverUI;
//		this.port = port;
//		sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
//		al = new ArrayList<ClientThread>();
//		messages = new ArrayList<Message>();
//		users = new ArrayList<User>();
//		clients = new HashMap<>();
//		unsentMessages = new HashMap<>();
//		userList = new ArrayList<>();
//	}
//	/**
//	 * Starts the server
//	 */
//	public void start() {
//		running = true;
//		try {
//			ServerSocket serverSocket = new ServerSocket(port);
//			deleteFile("files/messages.dat");
//			deleteFile("files/users.dat");
//			display("Server started. Port: " + port + ".");
//			while(running) {
//				Socket socket = serverSocket.accept(); 
//				if(!running) 
//					break;
//				ClientThread t = new ClientThread(socket); 
//				al.add(t);						
//				t.start();
//			}
//			try {
//				serverSocket.close();
//				for(int i = 0; i < al.size(); ++i) {
//					ClientThread tc = al.get(i);
//					try {
//						tc.sInput.close();
//						tc.sOutput.close();
//						tc.socket.close();
//					}
//					catch(IOException e) {
//					}
//				}
//			}
//			catch(Exception e) {
//				display("Exception closing the server and clients: " + e);
//			}
//		}
//		catch (IOException e) {
//			String message = sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n";
//			display(message);
//		}
//	}		
//	private void deleteFile(String filename) {
//		File file = new File(filename);
//		file.delete();
//	}
//	/**
//	 * Stops the server
//	 */
//	public void stop() {
//		running = false;
//		try {
//			new Socket("localhost", port);
//		}
//		catch(Exception e) {
//		}
//	}
//	private void display(Object obj) {
//		String time = sdf.format(new Date()) + " " + obj;
//		serverUI.appendEvent(time + "\n");
//	}
//	private synchronized void broadcast(Message message) throws InterruptedException {
//		String time = sdf.format(new Date());
//		message.setTimeSent(time);
//		serverUI.appendRoom(message);  
//		for(int i=0;i<al.size();i++) {
//			if(al.get(i).user.getUsername().equals(message.getSender().getUsername())) {
//				ClientThread ct = al.get(i);
//				ct.sendMessage(message);
//			}
//		}
//	}
//	private synchronized void broadcastUserList() throws IOException {
//		for(int i=0;i<al.size();i++) {
//			ClientThread ct = al.get(i);
//			ct.sendUserList();
//		}
//	}
//	public static void main(String[] args) {
//		new ServerUI(1500);
//	}
//	private void writeMessageToFile(Message message, String filename) throws IOException {
//		messages.add(message);
//		try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
//			oos.writeInt(messages.size());
//			for(Message m:messages) {
//				oos.writeObject(m);
//				oos.flush();
//			}
//		}
//	}
//	private void writeUserToFile(User user, String filename) throws IOException {
//		users.add(user);
//		try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
//			oos.writeInt(users.size());
//			for(User u:users) {
//				oos.writeObject(u);
//				oos.flush();
//			}
//		}
//	}
//	/**
//	 * Shows the messages
//	 * 
//	 * @throws FileNotFoundException
//	 * @throws IOException
//	 * @throws ParseException
//	 */
//	public void showMessages() throws FileNotFoundException, IOException, ParseException {
//		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");  
//		try {
//			Date dateFrom = formatter.parse(JOptionPane.showInputDialog(null, "Date from: yyyy.MM.dd"));
//			Date dateTo = formatter.parse(JOptionPane.showInputDialog("Date to: yyyy.MM.dd"));
//			Message message = null;
//			try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("files/messages.dat")))) {
//				int n = ois.readInt();
//				String msg = "";
//				for(int i=0; i<n; i++) {
//					message = (Message) ois.readObject();
//					String str = message.getTimeRecived().substring(0,10);
//					Date messageDate = formatter.parse(str);
//					if(dateFrom.compareTo(messageDate) < 0) {
//						if(dateTo.compareTo(messageDate) > 0) {
//							msg+=message.getText()+" sent from "+message.getSender().getUsername()+". Recived "+message.getTimeRecived()+"\n";
//						}
//					}
//				}
//				JOptionPane.showMessageDialog(null, msg);
//			}
//		}catch(Exception e) {
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * Removes all clients
//	 */
//	public void removeAllClients() {
//		for(int i = al.size(); --i >= 0;) {
//			serverUI.removeUsers(al.get(i).user.getUsername());
//			al.removeAll(al);
//		}
//	}
//	private class ClientThread extends Thread {
//		Socket socket;
//		ObjectInputStream sInput;
//		ObjectOutputStream sOutput;
//		Message message;
//		User user;
//		ClientThread(Socket socket) {
//			this.socket = socket;
//			try {
//				sInput  = new ObjectInputStream(socket.getInputStream());
//				user = (User) sInput.readObject();
//				if(userList.size()>0) {
//					for(User u:userList) {
//						if(user.getUsername().equals(u.getUsername())&&u.getStatus()==1) {
//							sendErrorMessage(user.getUsername()+" is taken");
//							return;
//						}
//					}
//				}
//				user.setStatus(1);
//				userList.add(user);
//				sendUserList();
//				clients.put(user.getUsername(), socket);
//				display(user.getUsername()+" just connected.");
//				writeUserToFile(user,"files/users.dat");
//				serverUI.appendUsers(user.getUsername());
//			}
//			catch (IOException e) {
//				display("Exception creating new Input/output Streams: " + e);
//				return;
//			}
//			catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//		}
//		public void run() {
//			boolean running = true;
//			while(running) {
//				try {
//					broadcastUserList();
//					String time = sdf.format(new Date());
//					Object obj = sInput.readObject();
//					if(obj instanceof Message) {
//						message=(Message)obj;
//						message.setTimeRecived(time);
//						writeMessageToFile(message,"files/messages.dat");
//					}
//				}
//				catch (IOException e) {
//					display(user.getUsername()+ " Exception reading Streams: " + e);
//					break;				
//				}
//				catch(ClassNotFoundException e2) {
//					break;
//				}
//				if(message!=null) {
//					switch(message.getType()) {
//					case Message.MESSAGE:
//						try {
//							broadcast(message);
//						} catch (InterruptedException e) {
//							e.printStackTrace();
//						}
//						break;
//					case Message.LOGOUT:
//						display(user.getUsername()+" has disconnected from the server.");
//						user.setStatus(0);
//						serverUI.removeUsers(user.getUsername());
//						running = false;
//						break;
//					}
//				}
//			}
//			close();
//		}
//		private void close() {
//			try {
//				sInput.close();
//				socket.close();
//			}
//			catch(Exception e) {
//				e.printStackTrace();
//			}
//		}
//
//		private void sendUserList() throws IOException {
//			sOutput = new ObjectOutputStream(socket.getOutputStream());
//			sOutput.writeObject(userList);
//		}
//
//		private void sendErrorMessage(String str) throws IOException {
//			sOutput = new ObjectOutputStream(socket.getOutputStream());
//			sOutput.writeObject(str);
//		}
//		
//		private boolean sendMessage(Message message) throws InterruptedException {
//			try {
//				for(User user:message.getReciverList()){
//					Socket socket = clients.get(user.getUsername());
//					if(!socket.isClosed()) {
//						sOutput = new ObjectOutputStream(socket.getOutputStream());
//						sOutput.writeObject(message);
//					}
//					else {
//						unsentMessages.put(socket, message);
//					}
//				}
//			}
//			catch(IOException e) {
//				display("Error sending message to " + user.getUsername()+"\n"+e.toString());
//			}
//			return true;
//		}
//		
//	}
//}
//
//

package gu;

import java.io.*;
import java.net.*;
import java.text.*;
import java.util.*;
import javax.swing.*;

/**
 * Is used to the create a server
 * 
 * @author Markus Masalkovski, Mattias Jönsson, Ramy Behnam, Lukas Rosberg, SofieLjungcrantz
 *
 */
public class Server {
	private ArrayList<ClientThread> al;
	private ArrayList<User> users;
	private ArrayList<User> userList;
	private ServerUI serverUI;
	private SimpleDateFormat sdf;
	private int port;
	private boolean running;
	private HashMap<String, ClientThread> clients;
	private UnsentMessage unsentMessages = new UnsentMessage(Message.MESSAGE);
	private static int uniqueId;

	/**
	 * Constructs a Server-object
	 * 
	 * @param port the port of the server
	 */
	public Server(int port) {
		this(port, null);
	}
	/**
	 * Constructs a Server-object
	 * 
	 * @param port the port of the server
	 * @param serverUI the serverUI
	 */
	public Server(int port, ServerUI serverUI){
		this.serverUI = serverUI;
		this.port = port;
		sdf = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
		al = new ArrayList<ClientThread>();
		users = new ArrayList<User>();
		clients = new HashMap<>();
		userList = new ArrayList<User>();
	}
	
	public static void main(String[] args) {
		new ServerUI(1500);
	}
	/**
	 * Starts the server
	 */
	public void start() {
		running = true;
		try {
			ServerSocket serverSocket = new ServerSocket(port);
			deleteFile("files/messages.dat");
			display("Server started. Port: " + port + ".");
			while(running) {
				Socket socket = serverSocket.accept(); 
				if(!running) break;
				ClientThread t = new ClientThread(socket); 
				t.start();
			}
			try {
				serverSocket.close();
				for(int i = 0; i < al.size(); ++i) {
					ClientThread tc = al.get(i);
					try {
						tc.sInput.close();
						tc.sOutput.close();
						tc.socket.close();
					}
					catch(IOException e) {}
				}
			}
			catch(Exception e) {
				display("Exception closing the server and clients: " + e);
			}
		}
		catch (IOException e) {
			display(sdf.format(new Date()) + " Exception on new ServerSocket: " + e + "\n");
		}
	}		
	private void deleteFile(String filename) {
		File file = new File(filename);
		file.delete();
	}
	/**
	 * Stops the server
	 */
	public void stop() {
		running = false;
		try {
			new Socket("localhost", port);
		}
		catch(Exception e) {
		}
	}
	private void display(Object obj) {
		String time = sdf.format(new Date()) + " " + obj;
		serverUI.appendEvent(time + "\n");
	}
	private synchronized void broadcast(Message message) throws InterruptedException {
		String time = sdf.format(new Date());
		message.setTimeSent(time);
		serverUI.appendRoom(message); 
		for(User user:message.getReciverList()) {
			if(clients.containsKey(user.getUsername()) && !user.getUsername().isEmpty()) {
				ClientThread ct = clients.get(user.getUsername());
				if(!ct.socket.isClosed())
					ct.sendMessage(message);
				else { 
					unsentMessages.put(user.getUsername(), message);
				}
			}
		}
	}
	private synchronized void broadcastUserList() throws IOException {
		for(int i=0;i<al.size();i++) {
			ClientThread ct = al.get(i);
			ct.sendUserList(users);
		}
	}
	
	public synchronized void remove(int id) {
		for(int i = 0; i < al.size(); ++i) {
			ClientThread ct = al.get(i);
			if(ct.id == id) {
				al.remove(i);
				return;
			}
		}
	}

	private void writeMessageToFile(Message message, String filename) throws IOException {
		ArrayList<Message> messages = new ArrayList<>();
		messages.add(message);
		try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
			oos.writeInt(messages.size());
			for(Message m:messages) {
				oos.writeObject(m);
				oos.flush();
			}
		}
	}
	private void writeUserToFile(User user, String filename) throws IOException {
		userList.add(user);
		try(ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))) {
			oos.writeInt(userList.size());
			for(User u:userList) {
				oos.writeObject(u);
				oos.flush();
			}
		}
	}
	/**
	 * Shows messages between two dates
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ParseException
	 */
	public void showMessages() throws FileNotFoundException, IOException, ParseException {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy.MM.dd");  
		try {
			Date dateFrom = formatter.parse(JOptionPane.showInputDialog(null, "Date from: yyyy.MM.dd"));
			Date dateTo = formatter.parse(JOptionPane.showInputDialog("Date to: yyyy.MM.dd"));
			Message message = null;
			try(ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream("files/messages.dat")))) {
				int n = ois.readInt();
				String msg = "";
				for(int i=0; i<n; i++) {
					message = (Message) ois.readObject();
					String str = message.getTimeRecived().substring(0,10);
					Date messageDate = formatter.parse(str);
					if(dateFrom.compareTo(messageDate) < 0) {
						if(dateTo.compareTo(messageDate) > 0) {
							msg+=message.getText()+" sent from "+message.getSender().getUsername()+". Recived "+message.getTimeRecived()+"\n";
						}
					}
				}
				JOptionPane.showMessageDialog(null, msg);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * Removes all clients
	 */
	public void removeAllClients() {
		for(int i = al.size(); --i >= 0;) {
			serverUI.removeUsers(al.get(i).user.getUsername());
			al.removeAll(al);
		}
	}

	private class ClientThread extends Thread {
		Socket socket;
		ObjectInputStream sInput;
		ObjectOutputStream sOutput;
		Message message;
		User user;
		int id;
		ClientThread(Socket socket) {
			this.socket = socket;
			id = ++uniqueId;
			al.add(this);	
			try {
				sOutput = new ObjectOutputStream(socket.getOutputStream());
				sOutput.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			try {
				sInput  = new ObjectInputStream(socket.getInputStream());
				user = (User) sInput.readObject();
				connectUser();
			}
			catch (IOException e) {
				display("Exception creating new Input/output Streams: " + e);
				return;
			}
			catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		public void run() {
			boolean running = true;
			while(running) {
				try {
					String time = sdf.format(new Date());
					Object obj = sInput.readObject();
					if(obj instanceof Message) {
						message=(Message)obj;
						message.setTimeRecived(time);
						writeMessageToFile(message,"files/messages.dat");
					}
				}
				catch (IOException e) {
					display(user.getUsername()+ " Exception reading Streams: " + e);
					break;				
				}
				catch(ClassNotFoundException e2) {
					break;
				}
				if(message!=null) {
					switch(message.getType()) {
					case Message.MESSAGE:
						System.out.println(message.getText());
						try {
							broadcast(message);
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
						break;
					case Message.LOGOUT:
						display(user.getUsername()+" has disconnected from the server.");
						serverUI.removeUsers(user.getUsername());
						users.remove(user);
						remove(id);
						try {
							broadcastUserList();
						} catch (IOException e) {
							e.printStackTrace();
						}
						running = false;
						break;
					}
				}
			}
			remove(id);
			close();
		}
		private void close() {
			try {
				sInput.close();
				sOutput.close();
				socket.close();
			}
			catch(Exception e) {}
		}

		private synchronized void sendUserList(ArrayList<User> list) throws IOException {
			for(User u : list) {
				sOutput.writeObject(u);
			}
		}

		private synchronized void sendTextMessage(String str) throws IOException {
			sOutput.writeObject(str);
		}

		private synchronized void sendMessage(Message message) throws InterruptedException {
			try {
				sOutput.writeObject(message);
			}
			catch(IOException e) {
				display("Error sending message to " + user.getUsername()+"\n"+e.toString());
			}
		}
		
		private synchronized void sendUnsentMessages() throws IOException, InterruptedException {
			ArrayList<Message> unsent = unsentMessages.get(user.getUsername());
			if (unsent != null && unsent.size() != 0) {
				for (Message message : unsent) {
					ArrayList<User> list = new ArrayList<User>();
					list.add(user);
					message.setReciverList(list);
					sendMessage(message);
				}
				unsentMessages.remove(user.getUsername());
			}
		}
		
		private synchronized void connectUser() throws IOException, InterruptedException {
			if(userExists(user)) {
				sendTextMessage(user.getUsername()+" is taken");
				sOutput.close();
			}
			else {
				clients.put(user.getUsername(), this);
				users.add(user);
				broadcastUserList();
				sendTextMessage("You connected to the server");
				display(user.getUsername()+" just connected.");
				serverUI.appendUsers(user.getUsername());
				sendUnsentMessages();
			}
		}
		
		private boolean userExists(User user) {
			for(User u: users)
				if(u.equals(user))
					return true;
			return false;
		}

	}
}


