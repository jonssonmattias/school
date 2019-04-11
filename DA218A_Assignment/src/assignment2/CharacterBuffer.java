package assignment2;

import java.util.*;
import java.util.concurrent.locks.*;

/**
 * A buffer containing a character
 * 
 * @author Mattias Jönsson
 *
 */
public class CharacterBuffer {
	private final LinkedList<Character> list = new LinkedList<>();
	private boolean hasCharacter = false;

	/**
	 * Sets the character to the buffer
	 * 
	 * @param c the character
	 * @param sync if the set should be synchronized or not
	 * @throws InterruptedException
	 */
	public void set(char c, boolean sync) throws InterruptedException{
		if(sync) {
			synchronized(this){
				while(!list.isEmpty())
					wait();
				set(c);
				notifyAll();
			}
		}
		else {
			set(c);
		}
	}

	/**
	 * Gets the character from the buffer
	 * 
	 * @param sync if the set should be synchronized or not
	 * @return the character
	 * @throws InterruptedException
	 */
	public char get(boolean sync) throws InterruptedException{
		if(sync) {
			synchronized(this){
				while(list.isEmpty())
					wait();
				char c = get();
				notifyAll();
				return c;
			}
		}
		else {
			char c=0;
			try {
				c=get();
			}catch(Exception e) {}
			return c;
		}
	}

	/**
	 * Gets the first character from the buffer 
	 * 
	 * @return the character
	 */
	private char get() {
		char c = list.removeFirst();
		hasCharacter=false;
		return c;
	}

	/**
	 * Sets a character to the buffer
	 * 
	 * @param c the character
	 */
	private void set(char c) {
		if(!hasCharacter) {
			list.addLast(c);
			hasCharacter=true;
		}
	}

	/**
	 * Returns the number of elements in this list.
	 * 
	 * @return the number of elements in this list
	 */
	public int size() {
		return list.size();
	}
}
