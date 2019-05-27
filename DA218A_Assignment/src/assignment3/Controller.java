package assignment3;

import java.util.Random;
import java.util.concurrent.Semaphore;

public class Controller {
	private GUI gui;
	private Producer scan, arla, axFood;
	private Consumer ica, coop, citygross;
	private Storage queue = new Storage(50);

	public Controller(GUI gui) {
		this.gui=gui;
	}

	public synchronized void startScanProducer() {
		scan = new Producer(queue, this);
		scan.start();
		System.out.println("Ica start");
	}
	public synchronized void startArlaProducer() {
		arla = new Producer(queue, this);
		arla.start();
		System.out.println("Arla start");
	}
	public synchronized void startAxFoodProducer() {
		axFood = new Producer(queue, this);
		axFood.start();
		System.out.println("AxFood start");
	}
	public void stopScanProducer() {
		scan.stopThread();
	}
	public void stopArlaProducer() {
		arla.stopThread();
	}
	public void stopAxFoodProducer() {
		axFood.stopThread();
	}
	public synchronized void startIcaConsumer(boolean icaContinue) {
		int totalNumberOfItems = new Random().nextInt(20)+1;
		int totalWeight = new Random().nextInt(1000)+1;
		int totalVolume = new Random().nextInt(1000)+1;
		ica = new Consumer(1,queue, this, totalNumberOfItems, totalWeight, totalVolume, icaContinue);
		ica.start();
		gui.getIcaItems().setText(totalNumberOfItems+""); 
		gui.getIcaVolume().setText(totalWeight+"");
		gui.getIcaWeight().setText(totalVolume+""); 
	}
	public synchronized void startCoopConsumer(boolean coopContinue) {
		int totalNumberOfItems = new Random().nextInt(20)+1;
		int totalWeight = new Random().nextInt(1000)+1;
		int totalVolume = new Random().nextInt(1000)+1;
		coop = new Consumer(2,queue, this, totalNumberOfItems, totalWeight, totalVolume, coopContinue);
		coop.start();
		gui.getCoopItems().setText(totalNumberOfItems+""); 
		gui.getCoopVolume().setText(totalWeight+"");
		gui.getCoopWeight().setText(totalVolume+"");
	}
	public synchronized void startCityGrossConsumer(boolean citygrossContinue) {
		int totalNumberOfItems = new Random().nextInt(20)+1;
		int totalWeight = new Random().nextInt(1000)+1;
		int totalVolume = new Random().nextInt(1000)+1;
		citygross = new Consumer(3,queue, this, totalNumberOfItems, totalWeight, totalVolume, citygrossContinue);
		citygross.start();
		gui.getCityGrossItems().setText(totalNumberOfItems+""); 
		gui.getCityGrossVolume().setText(totalWeight+"");
		gui.getCityGrossWeight().setText(totalVolume+"");
	}
	public void stopIcaConsumer() {
		ica.stopThread();
		ica.empty();
	}
	public void stopCoopConsumer() {
		coop.stopThread();
		coop.empty();
	}
	public void stopCityGrossConsumer() {
		citygross.stopThread();
		citygross.empty();
	}
	public void setIcaList(String item) {
		gui.getIcaList().append(item+"\n");
	}
	public void setCoopList(String item) {
		gui.getCoopList().append(item+"\n");
	}
	public void setCityGrossList(String item) {
		gui.getCityGrossList().append(item+"\n");
	}
	public void setIcaStatus(String status) {
		gui.getIcaStatus().setText("Status: "+status);
	}
	public void setCoopStatus(String status) {
		gui.getCoopStatus().setText("Status: "+status);
	}
	public void setCityGrossStatus(String status) {
		gui.getCityGrossStatus().setText("Status: "+status);
	}
	public void setBufferStatus(int value) {
		if(value==100) {
			gui.setStatusIdleProducer();
		}
		else {
			gui.setStatusProducingProducer();
		}
		gui.getBufferStatus().setValue(value);
	}

	public void emptyIcaText() {
		gui.getIcaList().setText("");
	}

	public void emptyCoopText() {
		gui.getCoopList().setText("");
	}
	public void emptyCityGrossText() {
		gui.getCityGrossList().setText("");
	}
}
