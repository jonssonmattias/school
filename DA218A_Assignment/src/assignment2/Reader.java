package assignment2;

import java.util.Random;

/**
 * Is used to read a character from a CharacterBuffer
 * 
 * @author Mattias Jönsson
 *
 */
public class Reader extends Thread {
	private CharacterBuffer buffer;
	private boolean sync;
	private GUI gui;

	/**
	 * Constructs a Reader-object
	 * 
	 * @param sync if the reader should be synchronized or not
	 * @param buffer the buffer
	 * @param gui the GUI
	 */
	public Reader(boolean sync,CharacterBuffer buffer, GUI gui) {
		this.sync=sync;
		this.buffer=buffer;
		this.gui=gui;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while(!Thread.interrupted()) {
			try {
				char c = buffer.get(sync);
				if(c!=0)
					gui.displayReader(c);
				sleep(new Random().nextInt(1000));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
