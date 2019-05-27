package assignment4;

public class Modifier extends Thread{
    private BoundedBuffer buffer;
    private int arrayLength;
    private boolean notify;

    public Modifier(BoundedBuffer buffer, int arrayLength, boolean notify){
        this.buffer = buffer;
        this.arrayLength = arrayLength;
        this.notify=notify;
    }

    public void run() {
    	modify();
    }

    /**
     * Modify data in the buffer. Performed for each sentence in the source text.
     */
    public void modify(){
        for(int i = 0; i < arrayLength; i++) {
            try {
				buffer.modifyData(notify);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
    }
}