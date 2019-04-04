package temptemp;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * 
 * 
 * @author Ludvig Juelsson and Oscar Jonsson
 * @version 1.0
 * @since 2019-03-13
 *This class saves instances of Message that was not sendable due to an offline receiver
 */
public class UnsentMessages {
	private HashMap<User, ArrayList<Message>> messages = new HashMap<User, ArrayList<Message>>();
	/**
	 * 
	 * Puts a Message and its receiver in the HashMap
	 * @param user
	 * @param message
	 */
	public synchronized void put(User user, Message message) {
		if (get(user) == null) {
			messages.put(user, new ArrayList<Message>());
		}
		get(user).add(message);
	}
	/**
	 * 
	 * Gets a list of Messages with the key User
	 * @param user
	 * @return
	 */
	public synchronized ArrayList<Message> get(User user){
		return messages.get(user);
	}
	/**
	 * Clears the HashMap
	 */
	public synchronized void clear() {
		messages.clear();
	}
}
