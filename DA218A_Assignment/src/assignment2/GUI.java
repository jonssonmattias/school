package assignment2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.locks.*;

/**
 * The GUI to the assignment
 * 
 * Date: 2019-04-11
 * @author Mattias Jönsson
 *
 */
public class GUI{
	private JFrame frame;
	private JTextField tfTransfer;
	private Writer writer;
	private Reader reader;
	private DefaultListModel<String> writerListModel = new DefaultListModel<String>();
	private DefaultListModel<String> readerListModel = new DefaultListModel<String>();;
	private CharacterBuffer buffer = new CharacterBuffer();
	private String transmitted="", received="";
	private boolean clear = true;
	private JLabel lblReceipt = new JLabel("");
	private JLabel lblTransmission = new JLabel("");
	private JList<String> listWriter, listReader;
	private JPanel pnlStatus = new JPanel();
	private JLabel lblMatch = new JLabel("");

	/**
	 * Launch the application.
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GUI window = new GUI();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GUI() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Concurrent Read/Write");
		frame.setBounds(100, 100, 523, 519);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new GridLayout(1, 0, 0, 0));

		setWriter();
		setTester();
		setReader();
	}

	/**
	 * Sets the writer panel
	 */
	public void setWriter() {
		JPanel pnlWrite = new JPanel();
		frame.getContentPane().add(pnlWrite);
		pnlWrite.setLayout(null);

		JLabel lblwriter = new JLabel("Writer Thread Logger");
		lblwriter.setHorizontalAlignment(SwingConstants.CENTER);
		lblwriter.setBounds(12, 13, 144, 16);
		pnlWrite.add(lblwriter);

		JScrollPane scrollPaneWriter = new JScrollPane();
		scrollPaneWriter.setBounds(12, 42, 144, 338);
		pnlWrite.add(scrollPaneWriter);

		listWriter = new JList<String>(writerListModel);
		listWriter.setSelectionModel(new DisabledItemSelectionModel());
		scrollPaneWriter.setViewportView(listWriter);

		JLabel lblTransmitted = new JLabel("Transmitted:");
		lblTransmitted.setBounds(22, 393, 124, 16);
		pnlWrite.add(lblTransmitted);

		lblTransmission.setBounds(22, 422, 134, 16);
		pnlWrite.add(lblTransmission);
	}

	/**
	 * Sets the tester panel
	 */
	public void setTester() {
		JPanel pnlTester = new JPanel();
		frame.getContentPane().add(pnlTester);
		pnlTester.setLayout(null);

		JLabel lblTester = new JLabel("Tester");
		lblTester.setHorizontalAlignment(SwingConstants.CENTER);
		lblTester.setBounds(8, 13, 147, 16);
		pnlTester.add(lblTester);

		ButtonGroup bg = new ButtonGroup();

		JRadioButton rdbtnNonSync = new JRadioButton("Non-synchronous");
		rdbtnNonSync.setBounds(8, 68, 127, 25);
		rdbtnNonSync.setSelected(true);
		bg.add(rdbtnNonSync);
		pnlTester.add(rdbtnNonSync);

		JRadioButton rdbtnSync = new JRadioButton("Synchronous ");
		rdbtnSync.setBounds(8, 38, 127, 25);
		bg.add(rdbtnSync);
		pnlTester.add(rdbtnSync);

		JLabel lblTransfer = new JLabel("String To Transfer");
		lblTransfer.setHorizontalAlignment(SwingConstants.LEFT);
		lblTransfer.setBounds(29, 118, 106, 16);
		pnlTester.add(lblTransfer);

		tfTransfer = new JTextField("It's Spring now!");
		tfTransfer.setBounds(8, 137, 147, 22);
		pnlTester.add(tfTransfer);
		tfTransfer.setColumns(10);

		JButton btnRun = new JButton("Run");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(clear) {
					String txt = tfTransfer.getText();
					boolean sync = rdbtnSync.isSelected();
					write(txt, sync);
					try {
						read(sync);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}		
					clear=false;
				}
			}
		});
		btnRun.setBounds(29, 172, 106, 25);
		pnlTester.add(btnRun);

		JLabel lblStatus = new JLabel("Running Status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(29, 210, 106, 16);
		pnlTester.add(lblStatus);

		pnlStatus.setBackground(Color.GRAY);
		pnlStatus.setBounds(29, 239, 106, 59);
		pnlTester.add(pnlStatus);

		lblMatch.setBounds(29, 311, 106, 16);
		pnlTester.add(lblMatch);

		JButton btnClear = new JButton("Clear");
		btnRun.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				readerListModel.clear();
				writerListModel.clear();
				writer=null;
				reader=null;
				transmitted="";
				received="";
				clear=true;
			}
		});
		btnClear.setBounds(29, 340, 106, 25);
		pnlTester.add(btnClear);
	}

	/**
	 * Sets the reader panel
	 */
	public void setReader() {
		JPanel pnlReader = new JPanel();
		frame.getContentPane().add(pnlReader);
		pnlReader.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 43, 144, 338);
		pnlReader.add(scrollPane);

		listReader = new JList<String>(readerListModel);
		listReader.setSelectionModel(new DisabledItemSelectionModel());
		scrollPane.setViewportView(listReader);

		JLabel lblReader = new JLabel("Reader Thread Logger");
		lblReader.setHorizontalAlignment(SwingConstants.CENTER);
		lblReader.setBounds(12, 14, 144, 16);
		pnlReader.add(lblReader);

		JLabel lblReceived = new JLabel("Received:");
		lblReceived.setBounds(22, 393, 56, 16);
		pnlReader.add(lblReceived);

		lblReceipt.setBounds(12, 422, 144, 16);
		pnlReader.add(lblReceipt);
	}

	/**
	 * Starts the writer thread
	 * 
	 * @param txt the string to be written
	 * @param sync if it should be synchronized or not  
	 */
	public void write(String txt, boolean sync) {
		if(writer==null)
			writer=new Writer(txt, sync,buffer, this);
		writer.start();
	}
	/**
	 * Starts the reader thread
	 * 
	 * @param sync if it should be synchronized or not 
	 * @throws InterruptedException
	 */
	public void read(boolean sync) throws InterruptedException {
		if(reader==null)
			reader=new Reader(sync, buffer, this);
		reader.start();
	}
	/**
	 * Displays the read character
	 * 
	 * @param c the character read
	 */
	public void displayReader(char c) {
		received+=c;
		lblReceipt.setText(received);
		readerListModel.addElement("Reading: "+c);
	}
	/**
	 * Displays the written character
	 * 
	 * @param c the character to be written
	 */
	public void displayWriter(char c) {
		transmitted+=c;
		lblTransmission.setText(transmitted);
		writerListModel.addElement("Writting: "+c);
	}
	/**
	 * Sets the color of the status panel
	 * 
	 * @param c the color
	 */
	public void setStatusColor(Color c) {
		pnlStatus.setBackground(c);
	}
	/**
	 * Check if the transmitted string is equal to the recievied string
	 */
	public void match() {
		if(transmitted.equals(received)) lblMatch.setText("Match");
		else lblMatch.setText("No match");
	}
	private class DisabledItemSelectionModel extends DefaultListSelectionModel {
		private static final long serialVersionUID = 1L;
		public void setSelectionInterval(int index0, int index1) {
			super.setSelectionInterval(-1,-1);
		}
	}

}
