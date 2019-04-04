package temptemp;

import java.util.HashMap;
import java.util.Set;


/**
 * 
 * @author Johan Lövberg
 *This class holds a HashMap that uses a User as the key for the value Client 
 */
public class Clients {
	private HashMap<User, Client> clients = new HashMap<User, Client>();
	
	/**
	 * puts a new pair in the HashMap
	 * @param user
	 * @param client
	 */
	public synchronized void put (User user, Client client) {
		clients.put(user, client);     
	}
	
	/**
	 * 
	 * Gets a client from the HashMap
	 * @param user is the key
	 * @returns the value Client
	 */
	public synchronized Client get(User user) {
		return clients.get(user);
	}
	/**
	 * 
	 * @returns a Set of its User-instances 
	 */
	public synchronized Set<User> keySet() {
		return clients.keySet();
	}
	/**
	 * Removes a pair from the HashMap
	 * @param user is the key for the pair to be removed
	 * @return the Client that is removed
	 */
	public synchronized Client remove(User user) {
		return clients.remove(user);
	}
	/**
	 * 
	 * Clears the HashMap
	 */
	public synchronized void clear() {
		clients.clear();
	}
}

