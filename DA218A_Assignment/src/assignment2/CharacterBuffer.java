package assignment2;

import java.util.LinkedList;

public class CharacterBuffer {
	private LinkedList<Character> buffer = new LinkedList<Character>();
	
	public void nonSyncPut(char c) {
		put(c);
	}
	public char nonSyncGet() throws InterruptedException {
		return get();
	}
	public synchronized void syncPut(char c) {
		put(c);
	}
	public synchronized char syncGet() throws InterruptedException {
		return get();
	}
	
	private char get() throws InterruptedException {
		return buffer.remove();
	}
	
	private void put(char c) {
		if(buffer.size()<1) {
			buffer.add(c);
		}
	}
	
}
