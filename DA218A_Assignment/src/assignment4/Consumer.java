package assignment4;

/**
 * A class that takes from the buffer
 * 
 * @author Mattias Jönsson
 *
 */
public class Consumer extends Thread{
	private BoundedBuffer buffer;
	private Controller controller;
	private int length;


	public Consumer(BoundedBuffer buffer, int length, Controller controller) {
		this.buffer = buffer;
		this.length=length;
		this.controller=controller;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public synchronized void run() {
		int i = 0;
		String text = "";
		while (i<length){
			try{				
				text+=buffer.remove()+" ";
				i++;
			}catch (InterruptedException ex){
				System.out.println("Consumer Read INTERRUPTED");
			}
		}
		controller.setDestText(text);
		controller.setNumberOfReplacements();
	}
}