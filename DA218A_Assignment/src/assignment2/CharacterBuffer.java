package assignment2;

import java.util.*;
import java.util.concurrent.locks.*;

public class CharacterBuffer {
	private final LinkedList<Character> list = new LinkedList<>();
	private boolean hasCharacter = false;

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

	private char get() {
		char c = list.removeFirst();
		hasCharacter=false;
		return c;
	}

	private void set(char c) {
		if(!hasCharacter) {
			list.addLast(c);
			hasCharacter=true;
		}
	}

	public int size() {
		return list.size();
	}
	//	public CharacterBuffer(boolean sync) {
	//	this.sync=sync;
	//}
	//public char get() throws InterruptedException {
	//	char c;
	//	if(sync) {
	//		synchronized(this) {
	//			c = buffer.remove();
	//			HasCharacter = false;
	//		}
	//	}
	//	else {
	//		c = buffer.remove();
	//		HasCharacter = false;
	//	}
	//	return c;
	//}
	//
	//public void put(char c) {
	//	System.out.println(c);
	//	if(sync) {
	//		synchronized(this) {
	//			if(!HasCharacter) {
	//				buffer.add(c);
	//				
	//				HasCharacter = true;
	//			}
	//		}
	//	}
	//	else {
	//		if(!HasCharacter) {
	//			buffer.add(c);
	//			HasCharacter = true;
	//		}
	//	}
	//}
}
