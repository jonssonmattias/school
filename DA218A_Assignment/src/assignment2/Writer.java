package assignment2;

import java.awt.Color;
import java.util.Random;

/**
 * Is used to write a character to the CharacterBuffer
 * 
 * @author TheComputer
 *
 */
public class Writer extends Thread {
	private CharacterBuffer buffer;
	private String txt;
	private boolean sync;
	private GUI gui;

	/**
	 * Constructs a Writer-object
	 * 
	 * @param txt the string to be written
	 * @param sync if the set should be synchronized or not
	 * @param buffer the buffer
	 * @param gui the GUI
	 */
	public Writer(String txt, boolean sync, CharacterBuffer buffer, GUI gui) {
		this.txt=txt;
		this.sync=sync;
		this.buffer=buffer;
		this.gui=gui;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		gui.setStatusColor(Color.RED);
		for(int i=0;i<txt.length();i++) {
			try {
				write(txt.charAt(i),sync);
				gui.displayWriter(txt.charAt(i));
				sleep(new Random().nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		gui.setStatusColor(Color.GREEN);
		gui.match();
	}

	/**
	 * Writes to the buffer
	 * 
	 * @param c the character to be written
	 * @param sync if the set should be synchronized or not
	 * @throws InterruptedException
	 */
	private void write(char c, boolean sync) throws InterruptedException {
		buffer.set(c, sync);
	}
}
