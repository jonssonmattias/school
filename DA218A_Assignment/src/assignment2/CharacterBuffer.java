package assignment2;

import java.util.*;
import java.util.concurrent.locks.*;

public class CharacterBuffer {
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();
	private final ArrayList<Character> list = new ArrayList<>();
	private boolean hasCharacter = false;
	
	public void set(char c, boolean sync){
		if(sync) {
			writeLock.lock();
			try{
				set(c);
			}
			finally{
				writeLock.unlock();
			}
		}
		else {
			set(c);
		}
	}

	public char get(boolean sync){
		if(sync) {
			readLock.lock();
			try{
				return get();
			}
			finally{
				readLock.unlock();
			}
		}
		else {
			return get();
		}
	}

	private char get() {
		char c = list.get(0);
		System.out.println("Printing elements("+c+") by thread "+ Thread.currentThread().getName());
		hasCharacter=false;
		return c;
	}

	private void set(char c) {
		if(!hasCharacter) {
			list.add(c);
			System.out.println("Adding element \"" + c + "\" by thread " + Thread.currentThread().getName());
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
