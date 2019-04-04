package p6.test1;

import java.awt.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import p6.*;

/**
 * Design the interface of a JPanel in which displays
 * an 7x7 grid for JLabels which displays the numbers 
 * the user writes in the Input-class. The class also
 * contains two rows of JLabels which contains what 
 * row/column the user chooses to read.
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Viewer extends JPanel {
	private JPanel pnl = new JPanel (new BorderLayout(15,15));
	private JPanel pnlNorth = new JPanel (new BorderLayout());
	private JPanel pnlSouth = new JPanel (new BorderLayout(15,0));
	private JPanel pnlGridArr = new JPanel (new GridLayout(7,7));
	private JPanel pnlGridRow = new JPanel (new GridLayout(0,7));
	private JPanel pnlGridCol = new JPanel (new GridLayout(7,0));
	private JLabel lblCol= new JLabel ("Column");
	private JLabel lblRow= new JLabel ("Row");
	private JLabel lblArr7x7= new JLabel ("Array 7x7");
	private JLabel[][] arr7x7 = new JLabel[7][7];
	private JLabel[] arrCol = new JLabel[7];
	private JLabel[] arrRow = new JLabel[7];
	
	public Viewer() {	
		setPreferredSize (new Dimension(450,450));
		setAlignment();
		lblRow.setPreferredSize(new Dimension(40,0));
		lblArr7x7.setPreferredSize(new Dimension(0,50));
		setBackground();
		setGrid();
		setRow();
		setCol();
		pnlNorth.add(lblCol, BorderLayout.WEST);
		pnlNorth.add(lblArr7x7, BorderLayout.CENTER);
		pnlSouth.add(lblRow, BorderLayout.WEST);
		pnlSouth.add(pnlGridRow, BorderLayout.CENTER);
		pnl.add(pnlNorth, BorderLayout.NORTH);
		pnl.add(pnlGridCol, BorderLayout.WEST);
		pnl.add(pnlGridArr, BorderLayout.CENTER);
		pnl.add(pnlSouth, BorderLayout.SOUTH);
		add(pnl);
	}
	private void setBackground() {
		pnlGridArr.setBackground(java.awt.Color.WHITE);
		pnlGridRow.setBackground(java.awt.Color.WHITE);
		pnlGridCol.setBackground(java.awt.Color.WHITE);
	}
	private void setAlignment() {
		lblCol.setHorizontalAlignment(SwingConstants.CENTER);
		lblCol.setVerticalAlignment(SwingConstants.CENTER);
		lblRow.setHorizontalAlignment(SwingConstants.CENTER);
		lblRow.setVerticalAlignment(SwingConstants.CENTER);
		lblArr7x7.setHorizontalAlignment(SwingConstants.CENTER);
		lblArr7x7.setVerticalAlignment(SwingConstants.CENTER);
	}
	private void setCol() {
		for(int i=0; i<arrCol.length; i++) {
			arrCol[i] = new JLabel("0");
			arrCol[i].setPreferredSize(new Dimension(40,40));
			arrCol[i].setHorizontalAlignment(SwingConstants.CENTER);
			arrCol[i].setVerticalAlignment(SwingConstants.CENTER);
			arrCol[i].setBorder( LineBorder.createBlackLineBorder());
			pnlGridCol.add(arrCol[i]);
		}
	}
	private void setRow() {
		for(int i=0; i<arrRow.length; i++) {
			arrRow[i] = new JLabel("0");
			arrRow[i].setPreferredSize(new Dimension(0,40));
			arrRow[i].setHorizontalAlignment(SwingConstants.CENTER);
			arrRow[i].setVerticalAlignment(SwingConstants.CENTER);
			arrRow[i].setBorder( LineBorder.createBlackLineBorder());
			pnlGridRow.add(arrRow[i]);
		}
	}
	private void setGrid() {
		for(int i=0; i<arr7x7.length; i++) {
			for(int j=0; j<arr7x7[i].length; j++) {
				if(arr7x7[i][j]==null) {
					arr7x7[i][j] = new JLabel();
				}
				if(arr7x7[i][j].getText().isEmpty()) {
					arr7x7[i][j].setText("0");
				}
				arr7x7[i][j].setPreferredSize(new Dimension(40,40));
				arr7x7[i][j].setHorizontalAlignment(SwingConstants.CENTER);
				arr7x7[i][j].setVerticalAlignment(SwingConstants.CENTER);
				arr7x7[i][j].setBorder( LineBorder.createBlackLineBorder());
				
				pnlGridArr.add(arr7x7[i][j]);
				
			}
		}
	}
	public void test() {
		JFrame frame = new JFrame ("Array7x7 Viewer");
		setPreferredSize (new Dimension(500,500));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(new Viewer());
		frame.pack();
		frame.setVisible(true);	
	}
	public static void main(String[] args) {
		Viewer v = new Viewer();
		v.test();
	}

	public void setRow(Array7 a7) {
 		int[] arr = a7.getArray();
		for(int i=0;i<arr.length;i++) {
			arrRow[i].setText(Integer.toString(arr[i]));
		}
	}
	public void setColum(Array7 a7) {
		int[] arr = a7.getArray();
		for(int i=0;i<arr.length;i++) {
			arrCol[i].setText(Integer.toString(arr[i]));
		}
	}
	public void setRowInArray(int row, Array7 a7) {
		int[] arr = a7.getArray();
		for(int i=0;i<arr7x7.length;i++) {
			arr7x7[row][i].setText(Integer.toString(arr[i]));
		}
	}
	public void setColumInArray(int col, Array7 a7) {
		int[] arr = a7.getArray();
		for(int i=0;i<arr7x7.length;i++) {
			arr7x7[i][col].setText(Integer.toString(arr[i]));
		}
	}
	public void setArray7x7(Array7x7 array) {
		for(int i=0;i<arr7x7.length;i++) {
			for (int j=0;j<arr7x7.length;j++) {
				arr7x7[i][j].setText(Integer.toString(array.getElement(i, j)));
			}
		}
	}

}

