package p6.test1;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import p6.*;

/**
 * Is used for starting the first test environment
 * 
 * @author Mattias Jönsson, Markus Masalkovski, Rasmus Öberg, Christoffer Palvin, Ramy Behnam, Isak Eklund
 *
 */
public class TestUI {
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
	public static void main( String[] args ) {
		test();
	}
}
