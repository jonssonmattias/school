package p6.test2;
import p6.*;

/**
 * Controls what is happening in the second test environment
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Controller {
	private Array7x7 array;
	private Array7 leftCol;
	private Array7 rightCol;
	private Test2UI ui;
	
	public Controller(Test2UI ui, Array7 rightCol, Array7 leftCol, Array7x7 array) {
		this.ui=ui;
		this.rightCol=rightCol;
		this.leftCol=leftCol;
		this.array=array;
	}
	/**
	 * Moves the array to the right
	 * 
	 * @param arr7 an Array7-object
	 */
	public void moveRight(Array7 arr7) {
		Array7 a7 = array.shiftRight(arr7);
		ui.setArray7x7(array);
		
		ui.setColumnRight(a7);
	}
	/**
	 * Moves the array to the left
	 * 
	 * @param arr7 an Array7-object
	 */
	public void moveLeft(Array7 arr7) {
		Array7 a7 = array.shiftLeft(arr7);
		ui.setArray7x7(array);
		ui.setColumnLeft(a7);
	}
	public void setUserInput(Test2UI test2ui) {
		this.ui = test2ui;
	}
}
