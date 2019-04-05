package assignment2;

public class Reader extends Thread {
	private CharacterBuffer buffer = new CharacterBuffer();
	private boolean sync;
	
	public Reader(boolean sync) {
		this.sync=sync;
	}
	
	public void run() {
//		while(!Thread.interrupted()) {
			try {
				System.out.println(display(sync));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
//		}
	}
	
	public String display(boolean sync) throws InterruptedException{
		return "Reading "+read(sync);
	}
	
	public char read(boolean sync) throws InterruptedException {
		if(sync) return buffer.syncGet();
		else return buffer.nonSyncGet();
	}
}
