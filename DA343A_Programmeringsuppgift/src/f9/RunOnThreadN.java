package f9;

import java.util.LinkedList;

public class RunOnThreadN {
	private Buffer<Runnable> buffer = new Buffer<Runnable>();
	private LinkedList<Worker> workers;
	private int n;
	
	public RunOnThreadN(int n) {
		this.n = Math.max(n, 1);
	}
	
	public synchronized void start() {
		Worker worker;
		if(workers==null) {
			workers = new LinkedList<Worker>();	
			for(int i=0; i<n; i++) {
				worker = new Worker();
				worker.start();
				workers.add(worker);
			}
		}
	}
	
	public synchronized void execute(Runnable runnable) {
		buffer.put(runnable);
	}
	
	public synchronized void stop() {
		if(workers!=null) {
			buffer.clear();
			for(Worker worker : workers) {
				worker.interrupt();
			}
			workers = null;
		}
	}
	
//	public synchronized void stop() {
//		if(workers!=null) {
//			for(int i=0; i<n; i++) {
//				execute(new StopWorker());
//			}
//			workers.clear();
//			workers = null;
//		}
//	}
//	
//	private class StopWorker implements Runnable {
//		public void run() {
//		    Thread.currentThread().interrupt();
//		}
//		
//		public String toString() {
//			return Thread.currentThread() + " StopWorker";
//		}
//	}
	
	private class Worker extends Thread {
		public void run() {
			String message = "";
			while(!Thread.interrupted()) {
				try {
				    buffer.get().run();
				} catch(InterruptedException e) {
					message += e.toString() + ", ";
					break;
				}
			}
			message += this + " finished";
			System.out.println(message);
		}
	}
	
	private class Buffer<T> {
		private LinkedList<T> buffer = new LinkedList<T>();
		
		public synchronized void put(T obj) {
			buffer.addLast(obj);
			notifyAll();
		}
		
		public synchronized T get() throws InterruptedException {
			while(buffer.isEmpty()) {
				wait();
			}
			return buffer.removeFirst();
		}
			
		public synchronized void clear() {
			buffer.clear();
		}
		
		public int size() {
			return buffer.size();
		}
	}
	
	public static void main(String[] args) {
		RunOnThreadN pool = new RunOnThreadN(20);
		pool.start();
		for(int i=0; i<100; i++) {
			pool.execute(new Task(i));
		}
		pool.stop(); // Testa även alternativ stop()-metod, "mjukt stopp"
	}
}

class Task implements Runnable {
	private int a;
	
	public Task(int a) {
		this.a = a;
	}
	
	public void run() {
		System.out.println(Thread.currentThread() + ": " + a);
	}
	
	public String toString() {
		return ""+a;
	}
}
