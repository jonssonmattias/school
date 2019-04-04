package p6.test1;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import p6.*;

/**
 * Design the interface of a JPanel in which the
 * user interact with. Also controls which button
 * is clicked.
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Input extends JPanel {
	private Controller controller;
	private JPanel pnl = new JPanel(new BorderLayout(15,15));
	private JPanel pnlSouth = new JPanel(new BorderLayout(15,0));
	private JPanel pnlCenter = new JPanel();
	private JPanel pnlGridRow = new JPanel(new GridLayout(0,7));
	private JPanel pnlGridCol = new JPanel(new GridLayout(7,0));
	private JPanel pnlCol = new JPanel();
	private JPanel pnlRow = new JPanel();
	private JPanel pnlFuse = new JPanel();
	private Border blackline = BorderFactory.createCompoundBorder();
	private JTextField[] arrCol = new JTextField[7];
	private JTextField[] arrRow = new JTextField[7];
	private JButton btnReadCol = new JButton("Read Column");
	private JButton btnWriteCol = new JButton("Write Column");
	private JButton btnReadRow = new JButton("Read Row");
	private JButton btnWriteRow = new JButton("Write Row");
	private JTextField txtCol = new JTextField();
	private JTextField txtRow = new JTextField();
	private JLabel lblEmpty = new JLabel(" ");
	private JLabel lblCol = new JLabel("Column nbr:");
	private JLabel lblRow = new JLabel("Row nbr:");

	public Input(Controller controller) {
		this.controller=controller;
		setPreferredSize (new Dimension(500,500));
		lblEmpty.setPreferredSize(new Dimension(40,0));
		setInputRow();
		setInputCol();
		setColPanel();
		setRowPanel();
		setPnlFuse();
		pnlSouth.add(lblEmpty, BorderLayout.EAST);
		pnlSouth.add(pnlGridRow, BorderLayout.CENTER);
		pnl.add(pnlGridCol, BorderLayout.EAST);
		pnl.add(pnlCenter, BorderLayout.CENTER);
		pnl.add(pnlSouth, BorderLayout.SOUTH);
		add(pnl);
		setButton();
	}	
	/**
	 * Sets the panel which is used for the column read and write
	 */
	private void setColPanel() {
		JPanel pnl = new JPanel(new GridLayout(1,2));
		lblCol.setHorizontalAlignment(SwingConstants.RIGHT);
		lblCol.setFont(new Font("Monospaced",Font.BOLD,20));
		txtCol.setFont(new Font("Monospaced",Font.BOLD,30));
		pnl.add(lblCol);
		pnl.add(txtCol);
		pnlCol.setLayout(new GridLayout(3,1));
		pnlCol.add(pnl);
		pnlCol.add(btnReadCol);
		pnlCol.add(btnWriteCol);
		pnlCol.setBorder(blackline);
	}
	/**
	 * Sets the panel which is used for the row read and write
	 */
	private void setRowPanel() {
		JPanel pnl = new JPanel(new GridLayout(1,2));
		txtRow.setFont(new Font("Monospaced",Font.BOLD,30));
		lblRow.setHorizontalAlignment(SwingConstants.RIGHT);
		lblRow.setFont(new Font("Monospaced",Font.BOLD,20));
		pnl.add(lblRow);
		pnl.add(txtRow);
		pnlRow.setLayout(new GridLayout(3,1));
		pnlRow.add(pnl);
		pnlRow.add(btnReadRow);
		pnlRow.add(btnWriteRow);
		pnlRow.setBorder(blackline);
	}
	/**
	 * Sets the panel which is fuses pnlCol and pnlRow together 
	 */
	private void setPnlFuse() {
		pnlFuse.setLayout(new GridLayout(2,1));
		pnlFuse.setPreferredSize(new Dimension(350,350));
		pnlFuse.add(pnlCol);
		pnlFuse.add(pnlRow);
		pnlFuse.setBorder( LineBorder.createBlackLineBorder());
		pnlCenter.add(pnlFuse);
	}
	/**
	 * Sets the panel which is used for writing the values of a row
	 */
	private void setInputRow() {
		for(int i=0; i<arrRow.length; i++) {
			arrRow[i] = new JTextField();
			arrRow[i].setText("0");
			arrRow[i].setFont(new Font("Monospaced",Font.BOLD,20));
			arrRow[i].setPreferredSize(new Dimension(0,40));
			arrRow[i].setHorizontalAlignment(SwingConstants.CENTER);
			arrRow[i].setBorder( LineBorder.createBlackLineBorder());
			pnlGridRow.add(arrRow[i]);
		}

	}
	/**
	 * Sets the panel which is used for writing the values of a row
	 */
	private void setInputCol() {
		for(int i=0; i<arrCol.length; i++) {
			arrCol[i] = new JTextField();
			arrCol[i].setText("0");
			arrCol[i].setFont(new Font("Monospaced",Font.BOLD,20));
			arrCol[i].setPreferredSize(new Dimension(40,40));
			arrCol[i].setHorizontalAlignment(SwingConstants.CENTER);
			arrCol[i].setBorder( LineBorder.createBlackLineBorder());
			pnlGridCol.add(arrCol[i]);
		}
	}
	/**
	 *  Sets the buttons
	 */
	private void setButton() {
		isClicked clicked = new isClicked();
		btnWriteCol.addActionListener( clicked );
		btnWriteRow.addActionListener( clicked );
		btnReadRow.addActionListener( clicked );
		btnReadCol.addActionListener( clicked );
	}
	/**
	 * Checks whether or not the input is valid
	 * 
	 * @param input the number of row/column
	 * @return	if the input is valid or not
	 */
	private boolean checkInput(int input) {
		return (input>=0&&input<7);
	}
	/**
	 * Displays a error message 
	 * 
	 * @param msg the message
	 */
	private void errorMessage(String msg) {
		JOptionPane.showMessageDialog(pnlFuse, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	/**
	 * Converts the values in the JTextField to integers
	 * 
	 * @param tf the array of JTextField
	 * @return an array of integers
	 */
	private int[] toIntArray(JTextField[] tf) {
		int[] arr = new int[tf.length];
		for(int i=0;i<tf.length;i++) {
			arr[i]=Integer.parseInt(tf[i].getText());
		}
		return arr;
	}
	private class isClicked implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if( e.getSource() == btnWriteRow ) {
				try {
					int row = Integer.parseInt(txtRow.getText());
					if(checkInput(row)){
						Array7 arr7 = new Array7();
						arr7.setArray(toIntArray(arrRow));
						controller.writeRow(row, arr7);
					}
					else errorMessage(new OutOfArrayExeption("The number is out of bounds of the 7x7-array.\nNumber: "+row).toString());
				}catch(NumberFormatException err) {
					errorMessage(err.toString());
				}
			}
			if( e.getSource() == btnWriteCol ) {
				try {
					int col = Integer.parseInt(txtCol.getText());
					if(checkInput(col)){
						Array7 arr7 = new Array7();
						arr7.setArray(toIntArray(arrCol));
						controller.writeCol(col, arr7);
					}
					else errorMessage(new OutOfArrayExeption("The number is out of bounds of the 7x7-array.\nNumber: "+col).toString());
				}catch(NumberFormatException err) {
					errorMessage(err.toString());
				}
			}
			if(e.getSource()==btnReadRow) {
				try {
					int row = Integer.parseInt(txtRow.getText());
					if(checkInput(row))controller.readRow(row);
					else errorMessage(new OutOfArrayExeption("The number is out of bounds of the 7x7-array.\nNumber: "+row).toString());
				}catch(NumberFormatException err) {
					errorMessage(err.toString());
				}
			}
			if(e.getSource()==btnReadCol) {
				try {
					int col = Integer.parseInt(txtCol.getText());
					if(checkInput(col))controller.readCol(col);
					else errorMessage(new OutOfArrayExeption("The number is out of bounds of the 7x7-array.\nNumber: "+col).toString());
				}catch(NumberFormatException err) {
					errorMessage(err.toString());
				}
			}
		}
	}
	public static void test() {
		Viewer viewer = new Viewer();
		Array7x7 arr7x7 = new Array7x7();
		Controller controller = new Controller(viewer, arr7x7);		
		Input userInput = new Input(controller);
		
		JFrame frame1 = new JFrame( "Viewer" );
		frame1.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame1.add( viewer );
		frame1.pack();
		frame1.setVisible( true );
		
		JFrame frame2 = new JFrame( "Input" );
		frame2.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		frame2.add( userInput );
		frame2.pack();
		frame2.setVisible( true );			
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int)Math.round((screenSize.getWidth()/2)-(frame1.getWidth()*1.5));
		int y = (int)Math.round((screenSize.getHeight()/2)-(frame1.getHeight()/1.5));
		frame1.setLocation(x, y);
		frame2.setLocation(frame1.getX()+frame1.getWidth(), frame1.getY());
	}
	public static void main(String[] args) {
		test();
	}

}
