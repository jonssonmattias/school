package p6.character;

import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;
import p6.*;

/**
 * Controls what is happening in the class for testing the character
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Controller {
	private Timer timer;
	private Array7x7 array;
	private ColorDisplayTest demo;
	private int[][] stamp = new int[7][7];
	public Controller(ColorDisplayTest demo,Array7x7 array) {
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
		array.shiftLeft(arr7);
		show(array.getArray());
	}
	/**
	 * Moves char to the right
	 * 
	 * @param arr7
	 */
	public void moveCharRight(Array7 arr7) {
		array.shiftRight(arr7);
		show(array.getArray());
	}
	/**
	 * Shows character
	 * 
	 * @param c the character
	 * @param fontColor the font color
	 * @param backgroundColor the background color
	 */
	public void showChar(char c, int fontColor, int backgroundColor) {
		Characters character = new Characters();
		array = character.displayChar(fontColor, backgroundColor, c);
		show(array.getArray());
	}
	
	/**
	 * Get values from stamp
	 * 
	 * @param i 
	 * @param j
	 * @return the integer value of the color 
	 */
	public int getValues(int i,int j) {
		return stamp[i][j];
	}
	
	/**
	 * Shows the character in ColorDisplayTest
	 * 
	 * @param arr the color values of the 
	 */
	private void show(int[][] arr) {
		for(int row=0; row<stamp.length; row++) {
			for(int col=0; col<stamp[row].length; col++) {
				stamp[row][col] = arr[row][col];
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
	public void start(int way) {
		if(timer!=null)stop();
		timer = new Timer();
		timer.schedule(new TimerMove(way), 0, 100);
	}
	/**
	 * Stops the timer
	 */
	public void stop() {
		if(timer!=null)timer.cancel();
	}

	/**
	 * Gets the column which "falls over the edge"
	 * as an Array7-object.
	 * 
	 * @param column 
	 * @return an Array7-object
	 */
	public Array7 getColumn(int column) {
		return array.getCol(column);
	}
	private class TimerMove extends TimerTask {
		private int counter=0;
		private int way;
		public TimerMove(int way) {
			this.way=way;
		}
		public void run() {
			if(counter>-1) {
				counter++;
				if(way==6)moveCharRight(array.getCol(6));
				if(way==0)moveCharLeft(array.getCol(0));
			}
		}
	}
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				Array7x7 arr7x7 = new Array7x7();
				ColorDisplayTest demo = new ColorDisplayTest(Color.BLACK, Color.GRAY);
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
