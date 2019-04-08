package assignment2;

import java.util.Random;

public class Reader extends Thread {
	private CharacterBuffer buffer;
	private boolean sync;
	private GUI gui;

	public Reader(boolean sync,CharacterBuffer buffer, GUI gui) {
		this.sync=sync;
		this.buffer=buffer;
		this.gui=gui;
	}

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
