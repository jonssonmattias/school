package assignment4;

import java.util.concurrent.locks.*;

import javax.swing.JOptionPane;

public class BoundedBuffer {
	private String[] elements;
	private BufferStatus[] status;
	private int max, size=0, writePos=0, readPos=0, findPos=0;
	private String findString, replaceString;
	private Lock aLock = new ReentrantLock();
	private Condition condVar = aLock.newCondition();

	public BoundedBuffer(int max, String findString, String replaceString) {
		this.max=max;
		elements = new String[ max ];
		status = new BufferStatus[max];
		this.findString=findString;
		this.replaceString=replaceString;
		init();
	}

	private synchronized void init() {
		for(int i=0;i<max;i++)
			status[i]=BufferStatus.Empty;
		notifyAll();
	}

	public synchronized void add( String elem ) throws InterruptedException {	
		while(status[writePos]!=BufferStatus.Empty) {
			wait();
		}
		elements[writePos] = elem;		
		status[writePos] = BufferStatus.New;
		writePos = (writePos+1) % max;
		notifyAll();
	}

	public synchronized String remove() throws InterruptedException {
		while(status[readPos]!=BufferStatus.Checked){
			wait();
		}
		String elem = elements[readPos];
		status[readPos]=BufferStatus.Empty;
		readPos = (readPos+1) % max;
		notifyAll();
		return elem;
	}

	public synchronized void modifyData(boolean notify) throws InterruptedException {
		boolean replace = true;
		while(status[findPos]!=BufferStatus.New) {
			wait();
		}
		String elem = elements[findPos];
		if(notify) {
			replace=question(elem);
		}		
		if(elem.contains(findString)) {
			elem=elem.replace(findString, replaceString);
		}
		elements[findPos]=elem;
		status[findPos]=BufferStatus.Checked;
		findPos=(findPos+1) % max;
		System.out.println("MODIFY - "+elem);
		notifyAll();
	}
	public boolean question(String elem) {
		int dialogResult  = JOptionPane.showConfirmDialog(null, "Do you want to replace "+elem+" with "+replaceString+"?","Question",JOptionPane.YES_NO_OPTION);
		if(dialogResult==JOptionPane.NO_OPTION)
			return false;
		return true;
	}
}
