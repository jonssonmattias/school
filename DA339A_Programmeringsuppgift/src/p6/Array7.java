package p6;

/**
 * Contains an integer array with 7 elements
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Array7 {
	private int[] array;
	
	/**
	 * Constructs an Array7-object as an empty integer array
	 */
	public Array7() {
		this.array = new int[7];
	}
	
	/**
	 * Constructs an Array7 object as another Array7-object
	 * 
	 * @param arr7 the other Array7-object
	 */
	public Array7(Array7 arr7) {
		this(arr7.getArray());
	}
	
	/**
	 * Constructs an Array7 object as another integer array
	 * 
	 * @param array the other integer array
	 */
	public Array7(int[] array) {
		this.array = array;
	}
	
	/**
	 * Sets the value to the given position
	 * 
	 * @param pos the position in the array
	 * @param value the value to be set in the position
	 */
	public void setElement(int pos, int value){
		this.array[pos] = value;
	}
	
	/**
	 * Returns the value of the given position in the array
	 * 
	 * @param pos the position in the array which will be returned
	 * @return the int in the given postion of the array
	 */
	public int getElement(int pos) {
		return array[pos];
	}

	/**
	 * Gets an Array7 object and gets that objects array.
	 * Sets this array to the same as that array
	 * 
	 * @param arr7 the array
	 */
	public void setArray(Array7 arr7) {
		setArray(arr7.getArray());
	}
	
	/**
	 * Sets the value of this array to the parameter array
	 * 
	 * @param arr the array
	 */
	public void setArray(int[] arr) {
		this.array = arr;
	}
	
	/**
	 * Returns a new Array7 object
	 * 
	 * @return a new Array7 object
	 */
	public Array7 getArray7() {
		return new Array7();
	}
	
	/**
	 * Returns this array
	 * 
	 * @return an int array
	 */
	public int[] getArray() {
		return this.array;
	}
	 
	/**
	 * Returns a new empty array with 7 objects
	 * 
	 * @return an int array
	 */
	public int[] toIntArray() {
		return new int[7];
	}

}