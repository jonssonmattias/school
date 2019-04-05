package assignment2;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.concurrent.locks.*;

public class GUI{
	private JFrame frame;
	private JTextField tfTransfer;
	private Writer writer;
	private Reader reader;
	private DefaultListModel<String> writerListModel = new DefaultListModel<String>();
	private DefaultListModel<String> readerListModel = new DefaultListModel<String>();
	private final ReadWriteLock readWriteLock = new ReentrantReadWriteLock();
	private final Lock readLock = readWriteLock.readLock();
	private final Lock writeLock = readWriteLock.writeLock();

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
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
		
		JList<String> listWriter = new JList<String>(writerListModel);
		listWriter.setSelectionModel(new DisabledItemSelectionModel());
		scrollPaneWriter.setViewportView(listWriter);
		
		JLabel lblTransmitted = new JLabel("Transmitted:");
		lblTransmitted.setBounds(22, 393, 124, 16);
		pnlWrite.add(lblTransmitted);
		
		JLabel lblTransmission = new JLabel("");
		lblTransmission.setBounds(22, 422, 134, 16);
		pnlWrite.add(lblTransmission);
	}
	
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
				String txt = tfTransfer.getText();
				boolean sync = rdbtnSync.isSelected();
				write(txt, sync);
				try {
					read(sync);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
//				
//				if(rdbtnSync.isSelected()) {
//					putInWriterList(txt);
//					writer.text(txt, true);
//				}
//				else {
//					writer.text(txt, false);
//				}
//				
			}
		});
		btnRun.setBounds(29, 172, 106, 25);
		pnlTester.add(btnRun);
		
		JLabel lblStatus = new JLabel("Running Status");
		lblStatus.setHorizontalAlignment(SwingConstants.CENTER);
		lblStatus.setBounds(29, 210, 106, 16);
		pnlTester.add(lblStatus);
		
		JPanel panel = new JPanel();
		panel.setBackground(Color.GRAY);
		panel.setBounds(29, 239, 106, 59);
		pnlTester.add(panel);
		
		JLabel lblMatch = new JLabel("");
		lblMatch.setBounds(29, 311, 106, 16);
		pnlTester.add(lblMatch);
		
		JButton btnClear = new JButton("Clear");
		btnClear.setBounds(29, 340, 106, 25);
		pnlTester.add(btnClear);
	}
	
	public void setReader() {
		JPanel pnlReader = new JPanel();
		frame.getContentPane().add(pnlReader);
		pnlReader.setLayout(null);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 43, 144, 338);
		pnlReader.add(scrollPane);
		
		JList<String> listReader = new JList<String>();
		scrollPane.setViewportView(listReader);
		
		JLabel lblReader = new JLabel("Reader Thread Logger");
		lblReader.setHorizontalAlignment(SwingConstants.CENTER);
		lblReader.setBounds(12, 14, 144, 16);
		pnlReader.add(lblReader);
		
		JLabel lblReceived = new JLabel("Received:");
		lblReceived.setBounds(22, 393, 56, 16);
		pnlReader.add(lblReceived);
		
		JLabel lblReceipt = new JLabel("");
		lblReceipt.setBounds(12, 422, 144, 16);
		pnlReader.add(lblReceipt);
	}

	public void write(String txt, boolean sync) {
		writer=new Writer(txt, sync);
		writeLock.lock();
		try{
			writer.start();
			
		}
		finally{
			writeLock.unlock();
		}
		for(char c : txt.toCharArray())
			writerListModel.addElement("Writting "+c);
	}
	public void read(boolean sync) throws InterruptedException {
		reader=new Reader(sync);
		readLock.lock();
		try {
//			String txt=reader.display(sync);
//			readerListModel.addElement(txt);
			reader.start();
		}
		finally{
			readLock.unlock();
		}
	}
	private class DisabledItemSelectionModel extends DefaultListSelectionModel {
		private static final long serialVersionUID = 1L;
		public void setSelectionInterval(int index0, int index1) {
			super.setSelectionInterval(-1,-1);
		}
	}
}
