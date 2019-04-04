package temptemp;

import java.io.Serializable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
/**
 * 
 * 
 * @author Oscar Jonsson,John Lindahl och Pontus Folke
 * @version 1.0
 * @since 2019-03-13
 * 
 *Users is a list that holds instances of User.
 *It implements Iterable and is therefore iterable
 */
public class Users implements Iterable<User>, Serializable {
	private ArrayList<User> users;
	
	public Users() {
		this.users = new ArrayList<User>();
	}
	
	/**
	 * 
	 * This constructor takes a Set of User-instances
	 * @param users
	 */
	public Users(Set<User> users) {
		this.users = new ArrayList<User>(users);
	}
	/**
	 * 
	 * Adds a User to list
	 * @param user
	 */
	public synchronized void add(User user) {
		users.add(user);
	}
	
	@Override
	public synchronized Iterator<User> iterator() {
		return users.iterator();
	}
	/**
	 * 
	 * 
	 * @returns the size of the list
	 */
	public synchronized int size() {
		return users.size();
	}
	/**
	 * 
	 * Gets the User at index and returns it.
	 * @param index
	 * @return the User
	 */
	public synchronized User get(int index) {
		return users.get(index);
	}
	
	public synchronized String toString() {
		String string = "";
		for (int i = 0; i < users.size(); i++) {
			string += users.get(i).getUsername();
			if (i < users.size() - 1) {
				string += ", ";
			}
		}
		return string;
	}
}
