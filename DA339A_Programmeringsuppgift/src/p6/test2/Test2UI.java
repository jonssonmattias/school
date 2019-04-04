package p6.test2;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import p6.*;

/**
 * Design the interface of a JPanel in which displays
 * an 7x7 grid for JLabels which displays the numbers 
 * the user writes in the Input-class. The class also
 * contains two rows of JLabels on either side of the 
 * 7x7 grid which contains what the user chooses to move
 * into the 7x7 grid or out of the 7x7 grid. The class
 * also controls which button is clicked.
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Test2UI extends JPanel implements ActionListener {
	private Controller controller;
	private JPanel pnl = new JPanel (new BorderLayout(15,15));
	private JPanel pnlNorth = new JPanel (new BorderLayout());
	private JPanel pnlSouth = new JPanel (new BorderLayout(15,0));
	private JPanel pnlGridArr = new JPanel (new GridLayout(7,7));
	private JPanel pnlGridColRight = new JPanel (new GridLayout(7,0));
	private JPanel pnlGridColLeft = new JPanel (new GridLayout(7,0));
	private JLabel lblCol= new JLabel ("Column");
	private JLabel lblRow= new JLabel ("Row");
	private JLabel lblArr7x7= new JLabel ("Array 7x7");
	private JLabel[][] arr7x7 = new JLabel[7][7];
	private JTextField[] arrColLeft = new JTextField[7];
	private JTextField[] arrColRight = new JTextField[7];
	private JButton btnMoveLeft = new JButton("<-- Move Left");
	private JButton btnMoveRight = new JButton("Move Right -->");
	
	public Test2UI(Controller controller) {	
		this.controller=controller;
		this.controller.setUserInput(this);
		
		setPreferredSize (new Dimension(450,450));
		setAlignment();
		lblRow.setPreferredSize(new Dimension(40,0));
		lblArr7x7.setPreferredSize(new Dimension(0,50));
		setBackground();
		setGrid();
		setStartColRight();
		setStartColLeft();
		pnlNorth.add(lblArr7x7, BorderLayout.CENTER);
		pnlSouth.add(btnMoveLeft, BorderLayout.WEST);
		pnlSouth.add(btnMoveRight, BorderLayout.EAST);
		pnl.add(pnlGridColLeft, BorderLayout.WEST);
		pnl.add(pnlGridArr, BorderLayout.CENTER);
		pnl.add(pnlGridColRight, BorderLayout.EAST);
		pnl.add(pnlSouth, BorderLayout.SOUTH);
		add(pnl);
		setButton();
	}
	/**
	 * Sets the buttons
	 */
	private void setButton() {
		btnMoveLeft.addActionListener( this );
		btnMoveRight.addActionListener( this );
	}
	/**
	 * Sets the background
	 */
	private void setBackground() {
		pnlGridArr.setBackground(java.awt.Color.LIGHT_GRAY);
		pnlGridColRight.setBackground(java.awt.Color.WHITE);
		pnlGridColLeft.setBackground(java.awt.Color.WHITE);
	}
	/**
	 * Sets the alignment
	 */
	private void setAlignment() {
		lblCol.setHorizontalAlignment(SwingConstants.CENTER);
		lblCol.setVerticalAlignment(SwingConstants.CENTER);
		lblRow.setHorizontalAlignment(SwingConstants.CENTER);
		lblRow.setVerticalAlignment(SwingConstants.CENTER);
		lblArr7x7.setHorizontalAlignment(SwingConstants.CENTER);
		lblArr7x7.setVerticalAlignment(SwingConstants.CENTER);
	}
	/**
	 * Sets the left column in the beginning with only 0:s
	 */
	private void setStartColLeft() {
		for(int i=0; i<arrColLeft.length; i++) {
			arrColLeft[i] = new JTextField("0");
			arrColLeft[i].setPreferredSize(new Dimension(40,40));
			arrColLeft[i].setHorizontalAlignment(SwingConstants.CENTER);
			arrColLeft[i].setBorder( LineBorder.createBlackLineBorder());
			pnlGridColLeft.add(arrColLeft[i]);
		}
	}
	/**
	 * Sets the right column in the beginning with only 0:s
	 */
	private void setStartColRight() {
		for(int i=0; i<arrColRight.length; i++) {
			arrColRight[i] = new JTextField("0");
			arrColRight[i].setPreferredSize(new Dimension(40,40));
			arrColRight[i].setHorizontalAlignment(SwingConstants.CENTER);
			arrColRight[i].setBorder( LineBorder.createBlackLineBorder());
			pnlGridColRight.add(arrColRight[i]);
		}
	}
	/**
	 * Sets the grid in the beginning with only 0:s
	 */
	private void setGrid() {
		for(int i=0; i<arr7x7.length; i++) {
			for(int j=0; j<arr7x7[i].length; j++) {
				if(arr7x7[i][j]==null) arr7x7[i][j] = new JLabel();
				if(arr7x7[i][j].getText().isEmpty())arr7x7[i][j].setText("0");
				arr7x7[i][j].setPreferredSize(new Dimension(40,40));
				arr7x7[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				arr7x7[i][j].setVerticalAlignment(SwingConstants.CENTER);
				arr7x7[i][j].setBorder( LineBorder.createBlackLineBorder());
				pnlGridArr.add(arr7x7[i][j]);

			}
		}
	}

	/**
	 * Sets the right column with the written numbers
	 * 
	 * @param a7 the column to be written
	 */
	public void setColumnRight(Array7 a7) {
		int[] arr = a7.getArray();
		for(int i=0;i<arr.length;i++) {
			arrColRight[i].setText(Integer.toString(arr[i]));
		}
	}
	/**
	 * Sets the right column with the written numbers
	 * 
	 * @param a7 the column to be written
	 */
	public void setColumnLeft(Array7 a7) {
		int[] arr = a7.getArray();
		for(int i=0;i<arr.length;i++) {
			arrColLeft[i].setText(Integer.toString(arr[i]));
		}
	}
	/**
	 * Sets the grid with the numbers
	 * 
	 * @param array the grid to be written 
	 */
	public void setArray7x7(Array7x7 array) {
		for(int i=0;i<arr7x7.length;i++) {
			for (int j=0;j<arr7x7.length;j++) {
				arr7x7[i][j].setText(Integer.toString(array.getElement(i, j)));
			}
		}
	}
	/**
	 * Turns the grid to a 2D integer array
	 * 
	 * @param lbl the grid
	 * @return a 2D integer array
	 */
	private int[][] toInt2DArray(JLabel[][] lbl) {
		int[][] arr = new int[lbl.length][lbl.length];
		for(int i=0;i<lbl.length;i++) {
			for (int j = 0; j < arr.length; j++) {
				arr[i][j]=Integer.parseInt(lbl[i][j].getText());
			}
		}
		return arr;
	}
	/**
	 * Displays a error message 
	 * 
	 * @param msg the message
	 */
	private void errorMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	private int[] toIntArray(JTextField[] tf) {
		int[] arr = new int[tf.length];
		for(int i=0;i<tf.length;i++) {
			try {
				arr[i]=Integer.parseInt(tf[i].getText());
				arr[i]=Integer.parseInt(tf[i].getText());
			}catch(NumberFormatException e) {
				errorMessage("Använd endast siffor!\n"+e);
			}	
		}
		return arr;
	}

	public void actionPerformed(ActionEvent e) {
		if( e.getSource() == btnMoveRight ) {
			Array7x7 a7x7 = new Array7x7();
			Array7 a7 = new Array7();
			a7x7.setArray(toInt2DArray(arr7x7));
			a7.setArray(toIntArray(arrColLeft));
			controller.moveRight(a7);
		}
		else if( e.getSource() == btnMoveLeft ) {
			Array7x7 a7x7 = new Array7x7();
			Array7 a7 = new Array7();
			a7x7.setArray(toInt2DArray(arr7x7));
			a7.setArray(toIntArray(arrColRight));
			controller.moveLeft(a7);
		}
	}
	public static void test2() {
		Array7x7 arr7x7 = new Array7x7();
		Array7 leftColumn = new Array7();
		Array7 rightColumn = new Array7();
		Controller controller = new Controller(null, rightColumn, leftColumn, arr7x7);
		Test2UI ui = new Test2UI(controller);
		JFrame frame = new JFrame( "Test2" );
		frame.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame.add( ui );
		frame.pack();
		frame.setVisible( true );
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int)Math.round((screenSize.getWidth()/2)-(frame.getWidth()/1.5));
		int y = (int)Math.round((screenSize.getHeight()/2)-(frame.getHeight()/1.5));
		frame.setLocation(x, y);
	}
	public static void main(String[] args) {
		test2();
	}
}


