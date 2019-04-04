package p6;

/**
 * Contains an 2D integer array with 7x7 elements
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Array7x7 {
	private int[][] array;

	/**
	 * Constructs a Array7x7-object as an empty 2D integer array
	 */
	public Array7x7() {
		this.array = new int[7][7];
	}

	/**
	 * Sets an element in the Array7x7-object
	 * 
	 * @param row the row-value
	 * @param col the column-value 
	 * @param value the value to be set
	 */
	public void setElement(int row, int col, int value) {
		array[row][col] = value;
	}

	/**
	 * Gets an element from the Array7x7-object
	 * 
	 * @param row the row-value
	 * @param col the column-value
	 * @return the value of a specific position in the Array7x7-object
	 */
	public int getElement(int row, int col) {
		return array[row][col];
	}

	/**
	 * Gets an Array7 object as input and the row we want to change.
	 * Iterates and sets the values of Array7 into the specific row
	 * of Array7x7
	 * 
	 * @param row what row of the 2d-array to set
	 * @param theRow the array7-object to set in the row
	 */
	public void setRow(int row, Array7 theRow) {
		for(int i = 0; i < theRow.getArray().length; i++) {
			array[row][i] = theRow.getElement(i);
		}
	}

	/**
	 * Gets an int as parameter.
	 * Returns a Array7 object with the same values as
	 * the given row of the Array7x7 object
	 * 
	 * @param row the row to get
	 * @return a Array7-object
	 */
	public Array7 getRow(int row) {
		Array7 arr = new Array7();
		for(int i = 0; i < 7; i++) {
			arr.setElement(i, this.getElement(row, i));
		}
		return arr;
	}

	/**
	 * Gets an Array7 object as input and the column we want to change.
	 * Iterates and sets the values of Array7 into the specific column
	 * of Array7x7
	 * 
	 * @param col what column of the 2d-array to set
	 * @param theCol the array7-object to set in the column
	 */
	public void setCol(int col, Array7 theCol) {
		for(int i = 0; i < theCol.getArray().length; i++) {
			array[i][col] = theCol.getElement(i);
		}
	}

	/**
	 * Gets an int as parameter.
	 * Returns a Array7 object with the same values as
	 * the given column of the Array7x7 object
	 * 
	 * @param col the column to get
	 * @return a Array7-object
	 */
	public Array7 getCol(int col) {
		Array7 arr = new Array7();
		for(int i = 0; i < 7; i++) {
			arr.setElement(i, this.getElement(i, col));
		}
		return arr;
	}

	/**
	 * Gets an Array7x7 object.
	 * Loops through and sets the value of each element
	 * to the array in this object
	 * 
	 * @param array7x7 the array to set
	 */

	public void setArray(Array7x7 array7x7) {
		for(int i = 0; i < 7; i++) {
			for(int j = 0; j < 7; j++) {
				this.array[i][j] = array7x7.getElement(i, j);
			}
		}
	}

	/**
	 * Gets a 2 dimensional array and sets it to 
	 * this array
	 * 
	 * @param arr the array to set
	 */
	public void setArray(int[][] arr) {
		this.array = arr;
	}

	/**
	 * Returns a 2 dimensional array with the same
	 * values as this array
	 * 
	 * @return a 2d-int array
	 */
	public int[][] getArray(){
		return this.array;
	}

	public Array7x7 getArray7x7() {
		return new Array7x7();
	}

	/**
	 * returns an empty array with 7x7 objects
	 * 
	 * @return a 2d-int array
	 */
	public int[][] toIntArray(){
		return new int[7][7];
	}

	/**
	 * Moves the values in the array to the right 
	 * and returns an Array7-object of the values 
	 * which was "pushed over".
	 * 
	 * @param a7 the array which will be set to the left most column 
	 * given by the outer JTextFields
	 * @return an Array7-object
	 */
	public Array7 shiftRight(Array7 a7) {
		int[][] arr = new int[7][7];
		int[] overArr = new int[7];
		for(int i=0;i<7;i++) {
			for(int j=0;j<6;j++) {
				arr[i][j+1]=this.getElement(i,j);
				arr[i][0]=a7.getElement(i);
			}
			overArr[i]=this.getElement(i,6);
		}
		this.array=arr;
		Array7 arr7 = new Array7();
		arr7.setArray(overArr);
		return arr7;
	}
	/**
	 * Moves the values in the array to the left 
	 * and returns an Array7-object of the values 
	 * which was "pushed over".
	 * 
	 * @param a7 a7 the array which will be set to the right most column 
	 * given by the outer JTextFields
	 * @return an Array7-object
	 */
	public Array7 shiftLeft(Array7 a7) {
		int[][] arr = new int[7][7];
		int[] overArr = new int[7];
		for(int i=0;i<7;i++) {
			for(int j=1;j<7;j++) {
				arr[i][j-1]=this.getElement(i,j);
				arr[i][6]=a7.getElement(i);
			}
			overArr[i]=this.getElement(i,0);
		}
		this.array=arr;
		Array7 arr7 = new Array7();
		arr7.setArray(overArr);
		return arr7;
	}
}