package gu;

import java.util.ArrayList;
import java.util.HashMap;
public class UnsentMessage extends Message{
	public UnsentMessage(int type) {
		super(type);
	}
	private HashMap<String, ArrayList<Message>> messages = new HashMap<String, ArrayList<Message>>();
	
	public synchronized void put(String username, Message message) {
		if (get(username) == null) {
			messages.put(username, new ArrayList<Message>());
		}
		get(username).add(message);
	}
	public synchronized ArrayList<Message> get(String username){
		return messages.get(username);
	}
	public synchronized void clear() {
		messages.clear();
	}
	public synchronized void remove(String username) {
		messages.remove(username);
	}
}
