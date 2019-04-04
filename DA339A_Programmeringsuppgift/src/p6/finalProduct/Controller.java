package p6.finalProduct;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import p6.*;

/**
 * Controls what is happening in the final product
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Controller {
	private Timer timer;
	private Array7x7 array;
	private Viewer demo;
	private int[][] stamp = new int[21][63];
	private int[][][] word = new int [9][7][7];
	private Array7x7[] w = new Array7x7[9];
	private int backgroundColor;
	
	public Controller(Viewer demo,Array7x7 array) {
		this.demo = demo;
		this.array=array;
		demo.setController(this);
	}
	/**
	 * Moves char to the left
	 * 
	 * @param arr7
	 */
	public void moveCharLeft(Array7 arr7) {
		for(int i=8;i>=0;i--) {
			arr7=w[i].shiftLeft(arr7);
			word[i]=toInt2dArray(w[i].getArray());
		}
		display(word);
	}
	/**
	 * Moves char to the right
	 * 
	 * @param arr7
	 */
	public void moveCharRight(Array7 arr7) {
		for(int i=0;i<9;i++) {
			arr7=w[i].shiftRight(arr7);
			word[i]=toInt2dArray(w[i].getArray());
		}
		display(word);
	}	

	/**
	 * Shows the character in ColorDisplayTest
	 * 
	 * @param arr the color values of the 
	 */
	private int[][] toInt2dArray(int[][] arr) {
		int[][] ch = new int[7][7];
		for(int row=0; row<ch.length; row++) {
			for(int col=0; col<ch[row].length; col++) {
				ch[row][col] = arr[row][col];
			}
		}
		return ch;
	}
	/**
	 * Sets the text in the right place in "stamp". and
	 * 
	 * @param arr a 3d-array of every color value of every 
	 * character being displayed
	 */
	private void display(int[][][] arr) {
		for(int i=0;i<arr.length;i++) {
			for(int row=0; row<21; row++) {
				for(int col=0; col<7; col++) {
					if(row>6&&row<14)
						stamp[row][col+i*7] = arr[i][row-7][col];
					else
						stamp[row][col+i*7] = this.backgroundColor;
				}
			}
		}
		demo.updateDisplay(stamp);
	}
	/**
	 * Starts the timer with 100 ms between every move
	 * Stops the timer if a Timer-object exists
	 * 
	 * @param way in which way the text is moving
	 */
	public void start(int dir) {
		if(timer!=null)stop();
		timer = new Timer();
		timer.schedule(new TimerMove(dir), 0, 100);
	}
	
	/**
	 * Stops the timer
	 */
	public void stop() {
		if(timer!=null)timer.cancel();
	}
	
	/**
	 * Gets the last column of each Array7x7-element
	 * 
	 * @return an Array7-object
	 */
	public Array7 getLastColumn() {
		Array7 a7 = new Array7();
		a7 = w[8].getCol(6);
		return a7;
	}
	
	/**
	 * Gets the last column of each Array7x7-element
	 * 
	 * @return an Array7-object
	 */
	public Array7 getFirstColumn() {
		Array7 a7 = new Array7();
		a7 = w[0].getCol(0);
		return a7;
	}
	/**
	 * Converts the String-object to an Array7x7-object ("array)
	 * with corresponding colors as well as adds the 
	 * Array7x7-array ("w"). Afterwards it displays the text.
	 * Also sets the background color.
	 * 
	 * @param text the text being converted
	 * @param fontColor the color of the text
	 * @param backgroundColor the color of the background
	 */
	public void makeText(String text, int fontColor, int backgroundColor) {
		this.backgroundColor = backgroundColor;
		for(int i=0;i<text.length();i++) {
			char c = text.charAt(i);
			array = new Characters().displayChar(fontColor, backgroundColor, c);
			word[i]=toInt2dArray(array.getArray());
			w[i]=array;
		}
		display(word);
	}
	
	private class TimerMove extends TimerTask {
		/**
		 * This private class is used to make the timer movement possible
		 */
		private int counter=0;
		private int way;
		public TimerMove(int way) {
			this.way=way;
		}
		public void run() {
			if(counter>-1) {
				counter++;
				if(way==6)moveCharRight(getLastColumn());
				if(way==0)moveCharLeft(getFirstColumn());
			}
		}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Array7x7 arr7x7 = new Array7x7();
				Viewer demo = new Viewer();
				new Controller(demo,arr7x7);
				JFrame frame = new JFrame();
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(demo);
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
