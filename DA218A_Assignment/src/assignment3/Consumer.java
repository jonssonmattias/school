package assignment3;

import java.util.concurrent.Semaphore;

/**
 * A class that takes from the queue
 * 
 * @author Mattias Jönsson
 *
 */
public class Consumer extends Thread{
	private Storage queue;
	private Controller controller;
	private int totalNumberOfItems, totalWeight, totalVolume, type, items=0, weight=0, volume=0;
	private boolean running=true, cont;

	/**
	 * Creates a Consumer-object
	 * 
	 * @param type the type 
	 * @param queue the queue of fooditems
	 * @param controller the controller class
	 * @param semaphore the semaphore
	 * @param totalNumberOfItems the number of item the consumer can take
	 * @param totalWeight the weight the consumer can take
	 * @param totalVolume the volume the consumer can take
	 * @param cont if the consumer will continue when full
	 */
	public Consumer(int type, Storage queue, Controller controller, int totalNumberOfItems, int totalWeight, int totalVolume, boolean cont) {
		this.queue = queue;
		this.controller=controller;
		this.totalNumberOfItems=totalNumberOfItems;
		this.totalWeight=totalWeight;
		this.totalVolume=totalVolume;
		this.type=type;
		this.cont=cont;
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public synchronized void run() {
		while (running){
			if(items<totalNumberOfItems && weight<totalWeight && volume<totalVolume) {
				try{				
//					if(queue.isEmpty()) {
//						setStatus("Idle");
//						wait();
//					}
					FoodItem foodItem = (FoodItem) queue.remove();
					setItem(foodItem);
					items++;
					weight+=foodItem.getWeight();
					volume+=foodItem.getVolume();
					Thread.sleep(1000);
				}catch (InterruptedException ex){
					System.out.println("Consumer Read INTERRUPTED");
				}
			}
			else if(cont)
				empty();
			else
				stopThread();
		}
	}
	public void stopThread() {
		running=false;
		setStatus("Stopped");
	}
	/**
	 * Empties the consumer
	 */
	public void empty() {
		items=0;
		weight=0;
		volume=0;
		switch (type) {
		case 1:controller.emptyIcaText();break;
		case 2:controller.emptyCoopText();break;
		case 3:controller.emptyCityGrossText();break;
		}
	}
	/**
	 * Sets the fooditem
	 * 
	 * @param foodItem
	 */
	private void setItem(FoodItem foodItem) {
		switch(type) {
		case 1: controller.setIcaList(foodItem.getName()); break;
		case 2: controller.setCoopList(foodItem.getName()); break;
		case 3: controller.setCityGrossList(foodItem.getName()); break;
		}
	}
	/**
	 * Sets the status
	 * 
	 * @param status
	 */
	private void setStatus(String status) {
		switch(type) {
		case 1: controller.setIcaStatus(status); break;
		case 2: controller.setCoopStatus(status); break;
		case 3: controller.setCityGrossStatus(status); break;
		}
	}
}