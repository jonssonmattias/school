package assignment3;

import java.util.concurrent.Semaphore;

public class Storage {
    private Object[] elements;
    private int size = 0;
    private Semaphore consumerSemaphore = new Semaphore(50);
    private Semaphore producerSemaphore = new Semaphore(0);
    private Semaphore mutex = new Semaphore(1);
    
    public Storage(int capacity) {
        elements = new Object[ capacity ];
    }
    
    public void add( Object elem ) throws InterruptedException {
        mutex.acquire();
        producerSemaphore.release();
//        System.out.println("Producer semaphore release");
        elements[ size++ ] = elem;
        consumerSemaphore.acquire();
//      System.out.println("Consumer semaphore acquire");
        mutex.release();
    }
    
    public Object remove() throws InterruptedException {
        mutex.acquire();
        producerSemaphore.acquire();
//        System.out.println("Consumer semaphore release");
        Object value = elements[ 0 ];
        size--;
        for(int i=1; i<size; i++) {
        	elements[i-1] = elements[i];
        }
        elements[size] = null;
        consumerSemaphore.release();
//      System.out.println("Consumer semaphore release");
        mutex.release();
        return value;
    }
    
    public Object element() {
        if( size==0 ) {
            throw new QueueException("peek: Queue is empty");
        }
        return elements[ 0 ];
    }
    
    public boolean isEmpty() {
        return (size==0);
    }
    
    public boolean isFull() {
    	return (size==elements.length); 
    }
    
    public int size() {
        return size;
    }
}
