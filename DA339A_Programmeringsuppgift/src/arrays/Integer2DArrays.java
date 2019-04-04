package arrays;

/**
 * This class contains the solutions for exercise 4b
 * 
 * @author Mattias Jönsson
 * Date 14/11-2018
 *
 */
public class Integer2DArrays {
	
	/**
	 * Prints out the number in the array
	 * 
	 * @param array		the array of numbers
	 * @return a string of the numbers
	 */
	public static String toString(int[][] array) {		
		String str ="{";		
		for (int i=0;i<array.length;i++) {
			str+="{";
			for (int j=0;j<array[i].length;j++) {
				str+=array[i][j];
				if(j<array[i].length-1)str+= ",";				
			}	
			str+="}";
			if(i<array.length-1)str+=",";	
		}
		str+="}";		
		return str;		
	}

	/**
	 * Checks the amount of elements in the array
	 * 
	 * @param array		the array of numbers
	 * @return the number of elements
	 */
	public static int elements(int[][] array) {
		int counter=0;
		for(int i=0;i<array.length;i++)
			for(int j=0;j<array[i].length;j++)
				counter++;
		return counter;
	}

	/**
	 * Checks the highest number in the array
	 * 
	 * @param array		the array of numbers
	 * @return a number
	 */
	public static int max(int[][] array) {
		int max=Integer.MIN_VALUE;
		for(int i=0;i<array.length;i++)
			for(int j=0;j<array[i].length;j++)
				if(array[i][j]>max)
					max=array[i][j];
		return max;
	}

	/**
	 * Checks the lowest number in the array
	 * 
	 * @param array		the array of numbers
	 * @return a number
	 */
	public static int min(int[][] array) {
		int min=Integer.MAX_VALUE;
		for(int i=0;i<array.length;i++)
			for(int j=0;j<array[i].length;j++)
				if(array[i][j]<min)
					min=array[i][j];
		return min;
	}

	/**
	 * Checks the sum of the numbers in the array
	 * 
	 * @param array		the array of numbers
	 * @return the sum
	 */
	public static int sum(int[][] array) {
		int sum=0;
		for(int i=0;i<array.length;i++)
			for(int j=0;j<array[i].length;j++)
				sum+=array[i][j];
		return sum;
	}

	/**
	 * Checks the average of the numbers in the array
	 * using the sum method
	 * 
	 * @param array		the array of numbers
	 * @return the average
	 */
	public static Double average(int[][] array) {			
		return (double)sum(array)/elements(array);
	}
}
