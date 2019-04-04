package temptemp;

import java.io.*;
import java.net.*;
import java.util.LinkedList;

import javax.swing.ImageIcon;

public class ClientController {
	private SendWindow viewer;
	private String ip;
	private int port = 1479;
	private Socket socket;
	private ClientThread thread;
	private Client client;
	private User user;
	private Users onlineUsers = null;
	private Users contacts = new Users();
	private String filename = "files/userList";
	private final Callback callback;

	public ClientController(SendWindow viewer) {
		this.viewer = viewer;
		this.viewer.setController(this);
		callback = viewer;
	}
	
	public synchronized void connect(String name,String ip,int port){
		this.ip=ip;
		this.port=port;
		user = new User(name);
		callback.getUser(user);
		new ContactReader().start();
		try {
			socket = new Socket(ip, port);
			if (thread == null) {
				thread = new ClientThread();
				thread.start();
			}
		} catch (UnknownHostException e) {
			e.printStackTrace(); // Invalid target
		} catch (IOException e) {
			e.printStackTrace(); // Failed to connect
		}
	}
	
	public synchronized void saveContacts(LinkedList<String> contacts) {	
		disconnect();
		new ContactSaver(contacts).start();;
	}
	
	public synchronized void disconnect() {		
	
		if (socket != null) {
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace(); // No connection
			}
		}
	}
	public void pack(String text, Users receivers) {
		Message msg= new Message(this.user, receivers,text, null);
		send(msg);
	}
	public void pack(String text, ImageIcon icon, Users receivers) {
		Message msg= new Message(this.user, receivers,text, icon);
		send(msg);
	}
	public void pack(ImageIcon icon, Users receivers) {
		Message msg=new Message(this.user, receivers, null, icon);
		send(msg);
	}

	public void updateUser(User user) {
		if (client == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			client.getOos().writeObject(user);
			client.getOos().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Users getContacts() {
		return contacts;
	}
	
	public User getMyUser() {
		return this.user;
	}
	
	public void send(Message msg) {
		if (client == null) {
			try {
				wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		try {
			client.getOos().writeObject(msg);
			client.getOos().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	private class ClientThread extends Thread {
		public ClientThread() {
			super("Client (" + port + ")");
			client = new Client(socket);
		}
		
		public void run() {
			final ObjectOutputStream oos = client.getOos();		
			try {
				oos.flush();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			final ObjectInputStream ois = client.getOis();
			try {
				oos.writeObject(user);
				oos.flush();
				String response = ois.readUTF();
				if (response == "USER_OK") {
					notifyAll();
				}
			} catch (IOException e) {
				disconnect();
				e.printStackTrace(); // No connection
			}
			while (!socket.isClosed()) {
				try {
					Object o = ois.readObject();
					if (o instanceof Users) {
						Users newList = (Users) o;
						
						onlineUsers=newList;
						viewer.update(onlineUsers);
						
					}
					if (o instanceof Message) {
						System.out.println("Received a message: " + ((Message)o).getText());
						viewer.update((Message)o);
					}
					if(o instanceof User) {
						user = (User) o;
						callback.getUser(user);
					}
				} catch (ClassNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					disconnect();
					e.printStackTrace();
				}
			}
			thread = null;
		}
	}
	
	/**
	 * This method saves users to a local file on the computer when trying to disconnect
	 * @param users, a LinkedList received from the UI when trying to disconnect. 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private class ContactSaver extends Thread{

		private LinkedList<String> users;
		
		public ContactSaver(LinkedList<String> users) {
			this.users = users;
		}
		public void run() {
			if (users.size() > 0) {
				try(ObjectOutputStream fos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(filename)))){
					fos.writeUTF("" + users.size());

					for(String user : users) {
						fos.writeUTF(user);
					}
					fos.flush();
					fos.close();


				} catch (IOException e) {
					e.printStackTrace();
				} 
			}
		}
	}
	
	private class ContactReader extends Thread{

		public void run() {
			try(ObjectInputStream fis = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)))){
				int size = Integer.parseInt(fis.readUTF());
				for(int i = 0; i < size; i++) {
					String user =  fis.readUTF();
					contacts.add(new User(user));
				}
				fis.close();
				if (size > 0) {
					callback.getContacts(contacts);
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				
			}
		}
	}
	
	public static void main(String[] args) {
		ClientController client = new ClientController(new SendWindow());
	}
}
