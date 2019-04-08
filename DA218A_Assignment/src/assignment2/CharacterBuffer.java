package assignment2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.concurrent.locks.*;

public class CharacterBuffer {
	private LinkedList<Character> buffer = new LinkedList<Character>();
	private boolean HasCharacter = false, sync;
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();
	
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

	public char get(int i, boolean sync){
		if(sync) {
			readLock.lock();
			try{
				return get(i);
			}
			finally{
				readLock.unlock();
			}
		}
		else {
			return get(i);
		}
	}

	private char get(int i) {
		char c = buffer.get(i);
		System.out.println("Printing elements("+c+") by thread "+ Thread.currentThread().getName());
		return c;
	}

	public void set(char c) {
		buffer.add(c);
		System.out.println("Adding element \"" + c + "\" by thread " + Thread.currentThread().getName());
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
