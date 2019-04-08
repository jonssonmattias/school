package assignment2;

public class Writer extends Thread {
	private CharacterBuffer buffer;
	private String txt;
	private boolean sync;

	public Writer(String txt, boolean sync) {
		this.txt=txt;
		this.sync=sync;
		buffer = new CharacterBuffer();
	}

	public void run() {
		for(int i=0;i<txt.length();i++) {
			write(txt.charAt(i),sync);
			try {
				sleep(300);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void write(char c, boolean sync) {
		buffer.set(c, sync);
	}
}
