package assignment2;

import java.awt.Color;
import java.util.Random;

public class Writer extends Thread {
	private CharacterBuffer buffer;
	private String txt;
	private boolean sync;
	private GUI gui;

	public Writer(String txt, boolean sync, CharacterBuffer buffer, GUI gui) {
		this.txt=txt;
		this.sync=sync;
		this.buffer=buffer;
		this.gui=gui;
	}

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

	private void write(char c, boolean sync) throws InterruptedException {
		buffer.set(c, sync);
	}
}
