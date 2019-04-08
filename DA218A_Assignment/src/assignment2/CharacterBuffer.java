package assignment2;

import java.util.LinkedList;

public class CharacterBuffer {
	private LinkedList<Character> buffer = new LinkedList<Character>();
	private boolean HasCharacter = false, sync;
	
	public CharacterBuffer(boolean sync) {
		this.sync=sync;
	}
	public char get() throws InterruptedException {
		char c;
		if(sync) {
			synchronized(this) {
				c = buffer.remove();
				HasCharacter = false;
			}
		}
		else {
			c = buffer.remove();
			HasCharacter = false;
		}
		return c;
	}

	public void put(char c) {
		System.out.println(c);
		if(sync) {
			synchronized(this) {
				if(!HasCharacter) {
					buffer.add(c);
					
					HasCharacter = true;
				}
			}
		}
		else {
			if(!HasCharacter) {
				buffer.add(c);
				HasCharacter = true;
			}
		}
	}
}
