package gu;

import java.util.HashMap;
import java.util.Set;

public class ListOfClients {
	private HashMap<User, Client> clients = new HashMap<User, Client>();

	public synchronized void put (User user, Client client) {
		clients.put(user, client);     
	}
	public synchronized Client get(User user) {
		return clients.get(user);
	}
	public synchronized Set<User> keySet() {
		return clients.keySet();
	}
	public synchronized Client remove(User user) {
		return clients.remove(user);
	}
	public synchronized void clear() {
		clients.clear();
	}
}
