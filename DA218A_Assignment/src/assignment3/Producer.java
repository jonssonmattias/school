package assignment3;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Producer extends Thread{
	private Storage queue;
	private FoodItem[] foodBuffer;
	private Controller controller;
	private	boolean running=true;

	public Producer(Storage queue, Controller controller) {
		this.queue = queue;
		this.controller=controller;
		initFoodItem();
	}

	public synchronized void run(){
		while (running){
			try{		
//				if(queue.isFull()) {	
//					wait();
//				}			
				queue.add(foodBuffer[new Random().nextInt(20)]);
				controller.setBufferStatus(getProcentage());
				Thread.sleep(1000);

			}catch (InterruptedException ex){
				System.out.println("Producer Read INTERRUPTED");
			}
		}
	}
	public void stopThread() {
		running=false;	
		System.out.println(currentThread().getName()+": running="+running);
	}
	private int getProcentage() {	
		return (int)Math.round(((double)queue.size()/50)*100);
	}
	private void initFoodItem() {
		foodBuffer = new FoodItem[20];
		foodBuffer[0] = new FoodItem("Milk",0.5,1.1);
		foodBuffer[1] = new FoodItem("Cream",0.1,0.6);
		foodBuffer[2] = new FoodItem("Youghurt",0.5,1.1);
		foodBuffer[3] = new FoodItem("Butter",0.66,2.24);
		foodBuffer[4] = new FoodItem("Flour",1.2,3.4);
		foodBuffer[5] = new FoodItem("Sugar",1.8,3.7);
		foodBuffer[6] = new FoodItem("Salt",1.55,0.27);
		foodBuffer[7] = new FoodItem("Almonds",0.19,0.6);
		foodBuffer[8] = new FoodItem("Bread",0.75,1.98);
		foodBuffer[9] = new FoodItem("Dounuts",0.5,1.4);
		foodBuffer[10] = new FoodItem("Jam",1.5,1.3);
		foodBuffer[11] = new FoodItem("Ham",2.5,4.1);
		foodBuffer[12] = new FoodItem("Chicken",6.8,3.9);
		foodBuffer[13] = new FoodItem("Salat",0.55,0.87);
		foodBuffer[14] = new FoodItem("Orange",0.29,2.46);
		foodBuffer[15] = new FoodItem("Apple",0.4,2.44);
		foodBuffer[16] = new FoodItem("Pear",0.77,1.2);
		foodBuffer[17] = new FoodItem("Soda",2.0,2.98);
		foodBuffer[18] = new FoodItem("Beer",1.5,3.74);
		foodBuffer[19] = new FoodItem("Hotdogs",1.38,2.0);		
	}
}
