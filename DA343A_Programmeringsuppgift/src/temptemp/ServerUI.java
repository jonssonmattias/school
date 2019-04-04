package temptemp;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.logging.SimpleFormatter;


public class ServerUI extends JFrame implements ServerViewer, WindowListener{
	private ServerController controller;
	private LogPanel panel=new LogPanel();
	//private JTextArea serverLabel=new JTextArea("ServerUI startat\n");
	//private JScrollPane scroller= new JScrollPane(serverLabel);
	private JLabel ipLabel=new JLabel();
	private JPanel portPanel=new JPanel();
	private JTextField portField= new JTextField();
	private JButton portBtn=new JButton("Change Port");
	//private JLabel portLbl=new JLabel();
	//private JDatePicker fromDatePicker = new JDatePicker();
	private JTextField fromDateField = new JTextField();  
	private JTextField toDateField = new JTextField();
	private JPanel inputPanel = new JPanel(new GridLayout(2,1));
	private JPanel datePanel = new JPanel(new BorderLayout());
	private JPanel dateInputPanel = new JPanel(new GridLayout(2,2));
	private JButton dateBtn = new JButton("Get log");
	private JLabel fromDateLabel = new JLabel("From Date  (yyyy/mm/dd/hh/mm)");
	private JLabel toDateLabel = new JLabel("To Date  (yyyy/mm/dd/hh/mm)");
	private JButton startBtn = new JButton("Start");
	private JPanel btnPanel = new JPanel(new GridLayout(1,2));
	
	public ServerUI()  {
		super("Server");
		setPreferredSize(new Dimension(500,600));
		setLayout(new BorderLayout());
		
		ipLabel.setPreferredSize(new Dimension(300,50));
		ipLabel.setOpaque(true);
//		try {
//			ipLabel.setText("Din IP Ã¤r: " + InetAddress.getLocalHost().getHostAddress());
//		} catch (UnknownHostException e) {
//			e.printStackTrace();
//		}
		
		
		ipLabel.setText("Din IP är: 127.0.0.1");
		
		fromDateLabel.setHorizontalAlignment(0);
		toDateLabel.setHorizontalAlignment(0);
		
//		panel.setPreferredSize(new Dimension (480,450));
//		scroller.setPreferredSize(new Dimension(480,450));
//		scroller.setOpaque(true);
//		panel.add(scroller);
//		serverLabel.setEditable(false);
//		serverLabel.setBackground(Color.lightGray);
//		serverLabel.setOpaque(true);
		
		//portPanel.setPreferredSize(new Dimension(250,100));
		portPanel.setOpaque(true);
		portPanel.setLayout(new BorderLayout());
		//portField.setPreferredSize(new Dimension (150,50));
		//portBtn.setPreferredSize(new Dimension(150,50));
		//portLbl.setPreferredSize(new Dimension(250,50));
		//portPanel.add(portLbl,BorderLayout.NORTH);
		dateInputPanel.add(fromDateLabel);
		dateInputPanel.add(toDateLabel);		
		dateInputPanel.add(fromDateField);
		dateInputPanel.add(toDateField);
		
		//btnPanel.add(portBtn);
		//btnPanel.add(startBtn);
		startBtn.setPreferredSize(new Dimension(100,50));
		dateBtn.setPreferredSize(new Dimension(100,50));
		
		
		datePanel.add(dateInputPanel, BorderLayout.CENTER);
		datePanel.add(dateBtn, BorderLayout.EAST);
		
		portPanel.add(portField,BorderLayout.CENTER);
		portPanel.add(startBtn,BorderLayout.EAST);
		inputPanel.add(datePanel);
		inputPanel.add(portPanel);
		
		portBtn.addActionListener(new btnListener());
		startBtn.addActionListener(new btnListener());
		dateBtn.addActionListener(new btnListener());
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(ipLabel,BorderLayout.NORTH);
		add(panel,BorderLayout.CENTER);
		add(inputPanel,BorderLayout.SOUTH);
		pack();
		setVisible(true);
		
		
		
	}
	public void addText(String text) {
		panel.addText(text);
	}
	private class btnListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==portBtn) {
				controller.setPort(Integer.parseInt(portField.getText()));
			}
			if(e.getSource()==startBtn) {
				controller.start(Integer.parseInt(portField.getText()));
			}
			if(e.getSource()==dateBtn) {
				try {
					Date dFrom = new SimpleDateFormat("yyyy/MM/dd/HH/mm").parse(fromDateField.getText());
					Date dTo = new SimpleDateFormat("yyyy/MM/dd/HH/mm").parse(toDateField.getText());
					System.out.println(dFrom);
					System.out.println(dTo);
					String logs[] = panel.getText().split("\n");
					LogPanel matches = new LogPanel();
					for (int i = 1; i < logs.length; i++) {
						Date dLog = new SimpleDateFormat("EEE MMM d HH:mm:ss zzz yyyy", new Locale("en")).parse(logs[i].split(": ")[0]);
						if (dLog.compareTo(dFrom) >= 0 && dLog.compareTo(dTo) <= 0) {
							matches.addText(logs[i]);
						}
					}
					JFrame logFrame = new JFrame(fromDateField.getText() + " - " + toDateField.getText());
					logFrame.add(matches);
					logFrame.pack();
					logFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
					logFrame.setVisible(true);
					System.out.println(matches);
				} catch (ParseException e1) {
					e1.printStackTrace();
				}
			}
		}
	}
	@Override
	public void setController(ServerController controller) {
		this.controller = controller;
		
	}
	@Override
	public void setIp(String ip) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void setPort(int port) {
		portField.setText(Integer.toString(port));
	}
	@Override
	public int getPort() {
		return Integer.parseInt(portField.getText());
	}
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent e) {
		controller.stop();
		
	}
	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String [] args)  {
	 ServerUI ui=new ServerUI();
	}
}
