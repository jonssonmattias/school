package p6.finalProduct;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import p6.*;
import p6.Color;
import p6.ColorDisplay;

/**
 * Design the interface of a JPanel in which displays
 * an field of 21x63 squares in which displays the text
 * the user can write in the JTextField. Also displays 
 * buttons for movement to the left and to the right as well
 * as continuously running text both to the left and to the right
 * and a button for stopping the text from moving. 
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class Viewer extends JPanel {
	private JPanel pnlMain = new JPanel(new BorderLayout(15,15));
	private JPanel pnlSouth = new JPanel(new GridLayout(1,3));
	private JPanel pnlSW = new JPanel(new GridLayout(2,1));
	private JPanel pnlSC = new JPanel(new GridLayout(3,1));
	private JPanel pnlSE = new JPanel(new GridLayout(2,1));

	private JButton btnLeft = new JButton("To the left!");
	private JButton btnRight= new JButton("To the right!");
	private JButton btnText = new JButton("Roll credits!");
	private JButton btnStartLeft = new JButton("Start Left");
	private JButton btnStartRight = new JButton("Start Right");
	private JButton btnStop = new JButton("Stop");
	private JTextField txtF = new JTextField();
	
	private int fc=Color.BLUE,bc=Color.WHITE;

	private ColorDisplay cd = new ColorDisplay(3,9,bc, fc,2,15);
	private Controller controller;

	public Viewer() {
		setLayout(new BorderLayout());
		setPreferredSize (new Dimension(800, 500));
		setDisplay();
		ButtonListener bl = new ButtonListener();
		btnText.addActionListener(bl);
		btnLeft.addActionListener(bl);
		btnRight.addActionListener(bl);
		btnStartLeft.addActionListener(bl);
		btnStartRight.addActionListener(bl);
		btnStop.addActionListener(bl);
		

	}
	/**
	 * Set the display
	 */
	private void setDisplay() {
		//Adds the ColorDisplay to the Center and another JPanel beneath it
		pnlMain.add(cd, BorderLayout.CENTER);
		pnlMain.add(pnlSouth, BorderLayout.SOUTH);	

		//Adds the right/left JButtons to the respective sides
		pnlSW.add(btnLeft);
		pnlSW.add(btnStartLeft);
		pnlSE.add(btnRight);
		pnlSE.add(btnStartRight);

		//Adds the three JPanels to the south border
		pnlSouth.add(pnlSW);
		pnlSouth.add(pnlSC);
		pnlSouth.add(pnlSE);

		//Adds the JTextField and JButton to the center of the south border
		pnlSC.add(txtF);
		pnlSC.add(btnText);
		pnlSC.add(btnStop);


		//Adds the main panel
		add(pnlMain);		
	}
	
	/**
	 * Sets the controller
	 * 
	 * @param controller the Controller-object being used in this program
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}
	
	/**
	 * Updates the color on the display
	 * 
	 * @param colors the color-values of each character
	 */
	public void updateDisplay(int[][] colors) {
		cd.setDisplay(colors);
		cd.updateDisplay();
	}
	/**
	 * Displays a error message 
	 * 
	 * @param msg the message
	 */
	private void errorMessage(String msg) {
		JOptionPane.showMessageDialog(null, msg, "Error", JOptionPane.ERROR_MESSAGE);
	}
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btnLeft) {
				controller.moveCharLeft(controller.getFirstColumn());
			} else if(e.getSource()==btnRight) {			
				controller.moveCharRight(controller.getLastColumn());
			} else if(e.getSource()==btnText) {
				String txt = txtF.getText();
				while(txt.length()<9) txt+=" ";
				if(txt.length()<10)
					controller.makeText(txt.toUpperCase(),Color.BLACK,Color.WHITE);
				else
					errorMessage("Skriv in maximalt 9 tecken");
					
			} else if(e.getSource()==btnStop) {
				controller.stop();
			} else if(e.getSource()==btnStartRight) {
				controller.start(6);
			} else if(e.getSource()==btnStartLeft) {
				controller.start(0);
			}
			
			
		}		
	}

}
