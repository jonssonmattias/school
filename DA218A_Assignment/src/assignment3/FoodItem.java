package assignment3;

public  class FoodItem {
	private String name;
	private double weight, volume;

	public FoodItem(String name, double weight, double volume) {
		this.name=name;
		this.weight=weight;
		this.volume=volume;
	}
	public synchronized String getName() {
		return name;
	}
	public synchronized double getWeight() {
		return weight;
	}
	public synchronized double getVolume() {
		return volume;
	}	
}