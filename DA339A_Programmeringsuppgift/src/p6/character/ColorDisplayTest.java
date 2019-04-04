package p6.character;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import p6.*;

/**
 * Is used for testing the display of characters and moving that character
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class ColorDisplayTest extends JPanel{
	private Controller controller;
	private ColorDisplay display;
	private JButton btnShowChar = new JButton("Show character");
	private JButton btnTimerLeft = new JButton("Timer Left");
	private JButton btnTimerRight = new JButton("Timer Right");
	private JButton btnStop = new JButton("Stop");
	private JButton btnLeft = new JButton("<--");
	private JButton btnRight = new JButton("-->");
	private JTextField tfChar = new JTextField("A");
	
	public ColorDisplayTest(int background, int grid) {
		ButtonListener bl = new ButtonListener();
		setLayout(new BorderLayout());
		display = new ColorDisplay(1, 1, background, grid, 2, 40);
		add(display, BorderLayout.CENTER);
		add(buttonPanel(), BorderLayout.SOUTH);
		btnShowChar.addActionListener(bl);
		btnTimerLeft.addActionListener(bl);
		btnTimerRight.addActionListener(bl);
		btnStop.addActionListener(bl);
		btnLeft.addActionListener(bl);
		btnRight.addActionListener(bl);
	}

	private JPanel buttonPanel() {
		JPanel panel = new JPanel(new GridLayout(1,6));
		panel.add(btnLeft);
		panel.add(btnTimerLeft);
		panel.add(btnShowChar);
		panel.add(tfChar);
		panel.add(btnStop);
		panel.add(btnTimerRight);
		panel.add(btnRight);
		return panel;
	}

	public void setController(Controller controller) {
		this.controller = controller;
	}

	public void updateDisplay(int[][] colors) {
		display.setDisplay(colors);
		display.updateDisplay();

	}

	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btnLeft) {
				controller.moveCharLeft(controller.getColumn(0));
			} else if(e.getSource()==btnRight) {			
				controller.moveCharRight(controller.getColumn(6));
			}
			else if(e.getSource()==btnShowChar) {
				char c = tfChar.getText().toUpperCase().charAt(0);
				controller.stop();
				controller.showChar(c, Color.WHITE, Color.BLUE);
			}
			else if(e.getSource()==btnTimerRight) {
				controller.start(6);
			}
			else if(e.getSource()==btnTimerLeft) {
				controller.start(0);
			}
			else if(e.getSource()==btnStop) {
				controller.stop();
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
