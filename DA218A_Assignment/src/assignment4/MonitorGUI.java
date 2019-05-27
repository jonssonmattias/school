package assignment4;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;

public class MonitorGUI implements ActionListener{
	/**
	 * These are the components you need to handle.
	 * You have to add listeners and/or code
	 */
	private JFrame frame;				// The Main window
	private JMenu fileMenu;				// The menu
	private JMenuItem openItem;			// File - open
	private JMenuItem saveItem;			// File - save as
	private JMenuItem exitItem;			// File - exit
	private JTextField txtFind;			// Input string to find
	private JTextField txtReplace; 		// Input string to replace
	private JCheckBox chkNotify;		// User notification choise
	private JLabel lblInfo;				// Hidden after file selected
	private JButton btnCreate;			// Start copying
	private JButton btnClear;			// Removes dest. file and removes marks
	private JLabel lblChanges;			// Label telling number of replacements
	private JTabbedPane tabbedPane;
	private JTextPane txtPaneSource, txtPaneDest;
	private JScrollPane scrollSource, scrollDest;
	private Controller controller;

	/**
	 * Starts the application
	 */
	public void Start(){
		controller = new Controller(this);
		frame = new JFrame();
		frame.setBounds(0, 0, 714,600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Text File Copier - with Find and Replace");
		InitializeGUI();					// Fill in components
		frame.setVisible(true);
		frame.setResizable(false);			// Prevent user from change size
		frame.setLocationRelativeTo(null);	// Start middle screen
	}

	/**
	 * Sets up the GUI with components
	 */
	private void InitializeGUI(){
		fileMenu = new JMenu("File");
		openItem = new JMenuItem("Open Source File");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		saveItem = new JMenuItem("Save Destination File As");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		saveItem.setEnabled(false);
		exitItem = new JMenuItem("Exit");
		exitItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		fileMenu.add(openItem);
		fileMenu.add(saveItem);
		fileMenu.addSeparator();
		fileMenu.add(exitItem);
		JMenuBar  bar = new JMenuBar();
		bar.add(fileMenu);
		frame.setJMenuBar(bar);

		JPanel pnlFind = new JPanel();
		pnlFind.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.black), "Find and Replace"));
		pnlFind.setBounds(12, 32, 436, 122);
		pnlFind.setLayout(null);
		frame.add(pnlFind);
		JLabel lab1 = new JLabel("Find:");
		lab1.setBounds(7, 30, 80, 13);
		pnlFind.add(lab1);
		JLabel lab2 = new JLabel("Replace with:");
		lab2.setBounds(7, 63, 80, 13);
		pnlFind.add(lab2);

		txtFind = new JTextField();
		txtFind.setBounds(88, 23, 327, 20);
		pnlFind.add(txtFind);
		txtReplace = new JTextField();
		txtReplace.setBounds(88, 60, 327, 20);
		pnlFind.add(txtReplace);
		chkNotify = new JCheckBox("Notify user on every match");
		chkNotify.setBounds(88, 87, 180, 17);
		pnlFind.add(chkNotify);

		lblInfo = new JLabel("Select Source File..");
		lblInfo.setBounds(485, 42, 120, 13);
		frame.add(lblInfo);

		btnCreate = new JButton("Create the destination file");
		btnCreate.setBounds(465, 119, 230, 23);
		frame.add(btnCreate);
		btnClear = new JButton("Clear Dest. file and remove marks");
		btnClear.setBounds(465, 151, 230, 23);
		frame.add(btnClear);

		lblChanges = new JLabel("No. of Replacements:");
		lblChanges.setBounds(279, 161, 200, 13);
		frame.add(lblChanges);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(12, 170, 653, 359);
		frame.add(tabbedPane);
		txtPaneSource = new JTextPane();
		scrollSource = new JScrollPane(txtPaneSource);
		tabbedPane.addTab("Source", null, scrollSource, null);
		txtPaneDest = new JTextPane();
		scrollDest = new JScrollPane(txtPaneDest);
		tabbedPane.addTab("Destination", null, scrollDest, null);

		actionListener();
	}
	private void actionListener() {
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		exitItem.addActionListener(this);
		btnCreate.addActionListener(this);
		btnClear.addActionListener(this);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==openItem) {
			File file = chooseFile();
			if(file!=null) {
				txtPaneSource.setText(controller.setScourceText(file));
				lblInfo.setText(file.getName());
				saveItem.setEnabled(true);
			}
		}
		else if(e.getSource()==saveItem) {
			try {
				File file = chooseFile();
				if(file!=null)
					controller.saveFile(getDestText(), file.getPath());
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}
		}
		else if(e.getSource()==btnCreate) {
			if(!txtPaneSource.getText().isEmpty() || !txtFind.getText().isEmpty() || !txtReplace.getText().isEmpty())
				controller.create(txtPaneSource.getText(), txtFind.getText(), txtReplace.getText(), chkNotify.isSelected());
		}
		else if(e.getSource()==exitItem) {
			System.exit(0);
		}
		else if(e.getSource()==btnClear) {
			txtPaneDest.setText("");
		}
	}
	public void setDestText(String text) {
		txtPaneDest.setText(text);
	}
	public void setNumberOfReplacements(int number) {
		lblChanges.setText("No. of Replacements: "+number);
	}
	public String getReplaceText() {
		return txtReplace.getText();
	}
	private File chooseFile() {
		JFileChooser chooser = new JFileChooser();
		int returnVal = chooser.showOpenDialog(null);
		File file = null;
		if(returnVal == JFileChooser.APPROVE_OPTION)
			file = chooser.getSelectedFile();
		return file;
	}
	public String getSourceText() {
		return txtPaneSource.getText();
	}
	public String getDestText() {
		return txtPaneDest.getText();
	}
	public void highlightWord(int start, int end) {
		Highlighter highlighter = txtPaneDest.getHighlighter();
	      HighlightPainter painter = new DefaultHighlighter.DefaultHighlightPainter(Color.pink);
	      try {
			highlighter.addHighlight(start, end, painter );
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
	}
}