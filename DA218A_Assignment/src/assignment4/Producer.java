package assignment4;

public class Producer extends Thread{
	private BoundedBuffer buffer;
	private String text;

	public Producer(BoundedBuffer buffer, String text) {
		this.buffer = buffer;
		this.text=text;
	}

	public synchronized void run(){
		String[] textArray = text.split(" ");
		int i = 0;
		while (i<textArray.length){
			try{				
				buffer.add(textArray[i]);
				i++;
			}catch (InterruptedException ex){
				System.out.println("Producer Read INTERRUPTED");
			}
		}
	}
}