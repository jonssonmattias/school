package assignment1;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.File;
import java.util.Random;
import javax.swing.*;
import javax.swing.filechooser.*;

public class Controller {
	private DisplayText dt;
	private RotateTriangle rt;
	private MusicPlayer mp;
	private CatchMe cm;
	private boolean dtRunning, rtRunning, mpRunning, cmRunning;
	private File file;
	private Sound sound;
	private GUI gui;
	private int hits=0;
	private JTextArea txtHits;

	/**
	 * Constructs a Controller-object
	 * 
	 * @param gui the GUI
	 */
	public Controller(GUI gui) {
		this.gui=gui;
	}

	/**
	 * Toggles between starting or stopping the displaying of the text. Starts
	 * the thread if the there is none.
	 * 
	 * @param toggle if the display should start or stop
	 */
	public void toggleDisplay(boolean toggle) {
		if(dt==null) {
			dt = new DisplayText();
			dt.start();
		}
		dtRunning=toggle;
	}

	/**
	 * Toggles between starting or stopping the rotating triangle. Starts
	 * the thread if the there is none.
	 * 
	 * @param toggle if the rotating triangle should start or stop
	 */
	public void toggleRotate(boolean toggle) {
		if(rt==null) {
			rt= new RotateTriangle();
			rt.start();
		}
		rtRunning=toggle;
	}

	/**
	 * Updates the hit points
	 */
	public synchronized void imageHit() {
		if(cm!=null) {
			cm.updateHits();
		}
	}

	/**
	 * Sets file to a chosen file and display that file's name 
	 */
	public void setAudioFile(){
		File chosenfile = chooseFile();
		if(chosenfile!=null) {
			file=chosenfile;
			gui.getAduioText().setText(file.getName());
		}
	}
	/**
	 * Toggles between start or stop the music playing. Starts
	 * the thread if the there is none.
	 * 
	 * @param toggle if the music should start or stop
	 */
	public void toggleMusic(boolean toggle) {
		if(mp==null) {
			mp= new MusicPlayer();
			mp.start();
		}
		mpRunning=toggle;
	}	

	/**
	 * Starts the "Catch Me"-game
	 */
	public void startCatchMe() {
		if(cm==null) {
			gui.getTxtHits().setText("0");
			cm = new CatchMe(gui.getCmbSkill().getSelectedItem().toString());
			cm.start();
			cmRunning=true;
		}
	}

	/**
	 * Opens the file manager
	 * 
	 * @return A audio file
	 */
	private File chooseFile() {
		JFileChooser file = new JFileChooser();
		file.addChoosableFileFilter(new FileNameExtensionFilter("*.Audio", "mp3"));
		return (file.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) ? file.getSelectedFile() : null;
	}

	/**
	 * @param bound the upper bound (exclusive). Must be positive.
	 * @return a randomized number between zero and bound 
	 */
	private int randomizer(int bound) {
		return new Random().nextInt(bound);
	}
	/**
	 * @param boundOver the upper bound (exclusive). Must be positive.
	 * @param boundUnder the under bound. Must be positive.
	 * @return a randomized number between boundUnder and boundOver
	 */
	private int randomizer(int boundOver, int boundUnder) {
		return new Random().nextInt(boundOver)-boundUnder;
	}
	/**
	 * Creates a image and resizes that image to 50x50 pixels
	 * 
	 * @return a resized image icon
	 */
	public ImageIcon resizedImage() {
		ImageIcon MyImage = new ImageIcon("image/gubbe.jpg");
		Image img = MyImage.getImage();
		Image newImg = img.getScaledInstance(50, 50, Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImg);
		return image;
	}

	private class DisplayText extends Thread {
		public void run(){
			gui.getDisplayText().setText("Display Text");
			while(!Thread.interrupted()) {
				while(dtRunning) {
					int x = randomizer(120), y = randomizer (180,90);
					gui.getDisplayText().setBounds(x, y,  200,  200);
					try {
						sleep(400);
					} catch (InterruptedException e) {}
				}
			}
		}
	}

	private class MusicPlayer extends Thread{
		public void run() {
			while(!Thread.interrupted()) {
				sound = Sound.getSound(file.getPath());
				gui.getLblPlaying().setText(gui.getLblPlaying().getText()+file.getName());
				while(mpRunning) if(sound!=null) sound.play();
				if(sound!=null) sound.stop();
			}
		}
	}

	private class RotateTriangle extends Thread {
		public synchronized void run(){
			int i=0;
			while(!Thread.interrupted()) {
				while(rtRunning) {
					Triangle triangle = new Triangle(i++);
					gui.getPnlRotate().add(triangle);
					gui.getPnlRotate().repaint();
					try {
						sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					gui.getPnlRotate().remove(triangle);
				}
			}
		}
	}

	private class Triangle extends JComponent {
		private static final long serialVersionUID = 1L;
		public int degrees;
		private int[] xPoints={100,70,130}, yPoints={50,100,100};

		public Triangle(int degrees) {
			super();
			setSize(new Dimension(200, 200));
			this.degrees=degrees;
		}

		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			Graphics2D g2d = (Graphics2D) g.create();
			AffineTransform at = new AffineTransform();
			at.rotate(Math.toRadians(degrees), 95, 95);
			g2d.setTransform(at);
			g2d.drawPolygon(xPoints, yPoints, 3);
			g2d.setColor(Color.BLACK);
			g2d.dispose();
		}
	}

	private class CatchMe extends Thread{
		int level;
		JTextArea txtHits=gui.getTxtHits();
		JLabel lblPic=gui.getLblPic();
		int hits=0;
		public CatchMe(String level) {
			this.level=levelConverter(level);
			lblPic.setBounds(0, 0, 50, 50);
			lblPic.setVisible(true);
		}
		public synchronized void run() {
			while(!Thread.interrupted()) {
				while(cmRunning) {
					lblPic.setBounds(randomizer(235), randomizer(235), lblPic.getIcon().getIconWidth(), lblPic.getIcon().getIconHeight());			
					try {
						sleep(800/level);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}

		}
		private void updateHits() {
			System.out.println(++hits);
			txtHits.setText(Integer.toString(hits));
		}
		/**
		 * Converts the string level to a integer level
		 * 
		 * @param level the level as a string
		 * @return a integer of the level
		 */
		private int levelConverter(String level) {
			switch(level) {
			case "Easy": return 1;
			case "Medium": return 2;
			case "Hard": return 3;
			}
			return 0;
		}
	}
}
