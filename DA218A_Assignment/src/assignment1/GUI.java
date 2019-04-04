package assignment1;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

/**
 * The GUI for assignment 1, DualThreads
 */
public class GUI {
	/**
	 * These are the components you need to handle.
	 * You have to add listeners and/or code
	 */
	private JFrame frame;		// The Main window
	private JButton btnDisplay;	// Start thread moving display
	private JButton btnDStop;	// Stop moving display thread
	private JButton btnTriangle;// Start moving graphics thread
	private JButton btnTStop;	// Stop moving graphics thread
	private JButton btnOpen;	// Open audio file 
	private JButton btnPlay;	// Start playing audio
	private JButton btnStop;	// Stop playing
	private JButton btnGo;		// Start game catch me
	private JPanel pnlMove;		// The panel to move display in
	private JPanel pnlRotate;	// The panel to move graphics in
	private JPanel pnlGame;		// The panel to play in
	private JLabel lblPlaying;	// Playing text
	private JLabel lblAudio;	// Audio file
	private JTextArea txtHits;	// Dispaly hits
	private JComboBox<Object> cmbSkill;	// Skill combo box, needs to be filled in
	private JLabel lblDisplayText = new JLabel();
	private JLabel lblPic;
	private Controller controller;

	/**
	 * Constructor
	 * 
	 * pnlRotate.add(t);
	 */
	public GUI(){
		controller=new Controller(this);
	}

	public static void main(String[] args) {
		GUI gui = new GUI();
		gui.Start();
	}

	/**
	 * Starts the application
	 */
	public void Start(){
		frame = new JFrame();
		frame.setBounds(0, 0, 819, 438);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Multiple Thread Demonstrator");
		InitializeGUI();					// Fill in components
		frame.setVisible(true);
		frame.setResizable(false);			// Prevent user from change size
		frame.setLocationRelativeTo(null);	// Start middle screen
	}

	/**
	 * Sets up the GUI with components
	 */
	private void InitializeGUI(){
		setMusicPlayer();
		setDisplayText();
		setRotateTriangle();
		setCatchMe();
	}
	/**
	 * Sets the panel of the "Catch me"-game
	 */
	private void setCatchMe() {
		// The game outer panel
		JPanel pnlCatchme = new JPanel();
		Border b4 = BorderFactory.createTitledBorder("Catch Me");
		pnlCatchme.setBorder(b4);
		pnlCatchme.setBounds(468, 12, 323, 375);
		pnlCatchme.setLayout(null);

		// Add controls to this panel
		JLabel lblSkill = new JLabel("Skill:");
		lblSkill.setBounds(26, 20, 50, 13);
		pnlCatchme.add(lblSkill);
		JLabel lblInfo = new JLabel("Hit Image with Mouse");
		lblInfo.setBounds(107, 13, 150, 13);
		pnlCatchme.add(lblInfo);
		JLabel lblHits = new JLabel("Hits:");
		lblHits.setBounds(240, 20, 50, 13);
		pnlCatchme.add(lblHits);
		cmbSkill = new JComboBox<Object>();
		fillComboBox();
		cmbSkill.setBounds(19, 41, 61, 23);
		pnlCatchme.add(cmbSkill);
		btnGo = new JButton("GO");
		btnGo.setBounds(129, 41, 75, 23);
		pnlCatchme.add(btnGo);
		txtHits = new JTextArea();			// Needs to be updated
		txtHits.setBounds(233, 41, 71, 23);
		txtHits.setEditable(false);
		Border b40 = BorderFactory.createLineBorder(Color.black);
		txtHits.setBorder(b40);
		pnlCatchme.add(txtHits);
		lblPic = new JLabel(controller.resizedImage());
		lblPic.setVisible(false);
		pnlGame = new JPanel();
		pnlGame.setBounds(19, 71, 285, 283);
		pnlGame.setLayout( null );
		Border b41 = BorderFactory.createLineBorder(Color.black);
		pnlGame.setBorder(b41);
		pnlGame.add(lblPic);
		pnlCatchme.add(pnlGame);
		frame.add(pnlCatchme);

		btnGo.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.startCatchMe();
				System.out.println("hejsan");
			}
		});
		lblPic.addMouseListener(new MouseAdapter()  {  
		    public void mouseClicked(MouseEvent e)  {  	
		    	controller.imageHit();
		    }  
		}); 
	}

	/**
	 * Fills the combo box
	 */
	private void fillComboBox() {
		cmbSkill.addItem("Easy");
		cmbSkill.addItem("Medium");
		cmbSkill.addItem("Hard");
	}
	/**
	 * Sets the panel for the rotating triangle
	 */
	private void setRotateTriangle() {
		// The moving graphics outer panel
		JPanel pnlTriangle = new JPanel();
		Border b3 = BorderFactory.createTitledBorder("Triangle Thread");
		pnlTriangle.setBorder(b3);
		pnlTriangle.setBounds(240, 118, 222, 269);
		pnlTriangle.setLayout(null);

		// Add buttons and drawing panel to this panel
		btnTriangle = new JButton("Start Rotate");
		btnTriangle.setBounds(10, 226, 121, 23);
		pnlTriangle.add(btnTriangle);		
		btnTStop = new JButton("Stop");
		btnTStop.setBounds(135, 226, 75, 23);
		pnlTriangle.add(btnTStop);	
		pnlRotate = new JPanel();
		pnlRotate.setBounds(10,  19,  200,  200);
		Border b31 = BorderFactory.createLineBorder(Color.black);
		pnlRotate.setBorder(b31);
		pnlTriangle.add(pnlRotate);
		// Add this to main window
		frame.add(pnlTriangle);


		btnTriangle.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toggleRotate(true);
			}
		});
		btnTStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toggleRotate(false);
			}
		});
	}
	/**
	 * Sets the panel for the text display
	 */
	private void setDisplayText() {
		// The moving display outer panel
		JPanel pnlDisplay = new JPanel();
		Border b2 = BorderFactory.createTitledBorder("Display Thread");
		pnlDisplay.setBorder(b2);
		pnlDisplay.setBounds(12, 118, 222, 269);
		pnlDisplay.setLayout(null);

		// Add buttons and drawing panel to this panel
		btnDisplay = new JButton("Start Display");
		btnDisplay.setBounds(10, 226, 121, 23);
		pnlDisplay.add(btnDisplay);		
		btnDStop = new JButton("Stop");
		btnDStop.setBounds(135, 226, 75, 23);

		pnlDisplay.add(btnDStop);				
		pnlMove = new JPanel();
		pnlMove.setBounds(10,  19,  200,  200);
		Border b21 = BorderFactory.createLineBorder(Color.black);
		pnlMove.setBorder(b21);
		pnlMove.add(lblDisplayText);
		pnlDisplay.add(pnlMove);
		frame.add(pnlDisplay);


		btnDisplay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toggleDisplay(true);
			}
		});
		btnDStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toggleDisplay(false);
			}
		});
	}
	/**
	 * Sets the panel for the music player
	 */
	private void setMusicPlayer() {
		// The music player outer panel
		JPanel pnlSound = new JPanel();
		Border b1 = BorderFactory.createTitledBorder("Music Player");
		pnlSound.setBorder(b1);
		pnlSound.setBounds(12, 12, 450, 100);
		pnlSound.setLayout(null);

		// Add labels and buttons to this panel
		lblPlaying = new JLabel("Now Playing: ");	
		lblPlaying.setFont(new Font("SansSerif", Font.BOLD, 18));
		lblPlaying.setBounds(10, 16, 350, 20);
		pnlSound.add(lblPlaying);
		JLabel lbl1 = new JLabel("Loaded Audio File: ");
		lbl1.setBounds(10, 44, 130, 13);
		pnlSound.add(lbl1);
		lblAudio = new JLabel("...");				
		lblAudio.setBounds(115, 44, 300, 13);
		pnlSound.add(lblAudio);
		btnOpen = new JButton("Open");
		btnOpen.setBounds(6, 71, 75, 23);
		pnlSound.add(btnOpen);
		btnPlay = new JButton("Play");
		btnPlay.setBounds(88, 71, 75, 23);
		pnlSound.add(btnPlay);
		btnStop = new JButton("Stop");
		btnStop.setBounds(169, 71, 75, 23);
		pnlSound.add(btnStop);		
		frame.add(pnlSound);

		btnOpen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.setAudioFile();
			}
		});
		btnPlay.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toggleMusic(true);
			}
		});
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				controller.toggleMusic(false);
			}
		});
	}
	/**
	 * @return the label containing the chosen audio file
	 */
	public JLabel getAduioText() {
		return lblAudio;
	}
	/**
	 * @return the label for the displaying of a text
	 */
	public JLabel getDisplayText() {
		return lblDisplayText;
	}
	/**
	 * @return the panel that contains the rotating triangle
	 */
	public JPanel getPnlRotate() {
		return pnlRotate;
	}
	/**
	 * @return the label for the text showing what song is playing 
	 */
	public JLabel getLblPlaying() {
		return lblPlaying;
	}
	/**
	 * @return the text area for the hits in the "Catch Me"-game
	 */
	public JTextArea getTxtHits() {
		return txtHits;
	}
	/**
	 * @return the combo box for the skill level
	 */
	public JComboBox<Object> getCmbSkill() {
		return cmbSkill;
	}
	/**
	 * @return the label containing the picture
	 */
	public JLabel getLblPic() {
		return lblPic;
	}
	/**
	 * @return the panel for the "Catch Me"-game
	 */
	public JPanel getPnlGame() {
		return pnlGame;
	}
}


