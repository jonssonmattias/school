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

	private void init() {
		aLock.lock();
		try {
			for(int i=0;i<max;i++)
				status[i]=BufferStatus.Empty;
			condVar.signalAll();
		}finally {
			aLock.unlock();
		}
	}

	public void add( String elem ) throws InterruptedException {	
		aLock.lock();
		try {
			if(size == max) {
				condVar.await();
			}
			if(status[writePos]!=BufferStatus.Empty) {
				condVar.await();
			}
			elements[writePos] = elem;		
			status[writePos] = BufferStatus.New;
			writePos = (writePos+1) % max;
			size++;
			condVar.signalAll();
		}finally {
			aLock.unlock();
		}
	}

	public String remove() throws InterruptedException {
		aLock.lock();
		try {
			if(size == 0) {
				condVar.await();
			}
			if(status[readPos]!=BufferStatus.Checked){
				condVar.await();
			}
			String elem = elements[readPos];
			status[readPos]=BufferStatus.Empty;
			readPos = (readPos+1) % max;
			size--;
			condVar.signalAll();
			return elem;
		}finally {
			aLock.unlock();
		}
	}

	public void modifyData(boolean notify) throws InterruptedException {
		aLock.lock();
		try {
			boolean replace = true;
			if(size == max) {
				condVar.await();
			}
			if(status[findPos]!=BufferStatus.New) {
				condVar.await();
			}
			if(elements[findPos]!=null) {
				String elem = elements[findPos];
				if(notify) {
					replace=question(elem);
				}		
				if(elem.contains(findString)&&replace) {
					elem=elem.replace(findString, replaceString);
				}
				elements[findPos]=elem;
				status[findPos]=BufferStatus.Checked;
				findPos=(findPos+1) % max;
				condVar.signalAll();
			}
		}finally {
			aLock.unlock();
		}
	}
	public boolean question(String elem) {
		int dialogResult  = JOptionPane.showConfirmDialog(null, "Do you want to replace "+elem+" with "+replaceString+"?","Question",JOptionPane.YES_NO_OPTION);
		if(dialogResult==JOptionPane.NO_OPTION)
			return false;
		return true;
	}
}
