package assignment2;

public class Writer extends Thread{
	private CharacterBuffer buffer = new CharacterBuffer();
	private String txt;
	private boolean sync;
	
	public Writer(String txt, boolean sync) {
		this.txt=txt;
		this.sync=sync;
	}
	
	public void run() {
//		while(!Thread.interrupted()) {
			text(txt,sync);
//		}
	}
	
	public void text(String txt, boolean sync) {
		for(int i=0;i<txt.length();i++) {
			write(txt.charAt(i),sync);
		}
	}
	
	private void write(char c, boolean sync) {
		if(sync)buffer.syncPut(c);
		else buffer.nonSyncPut(c);
	}
}
