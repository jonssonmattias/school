package test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;


public class ServerUI extends JFrame implements ActionListener, WindowListener {

	private static final long serialVersionUID = 1L;
	private JButton stopStart;
	private JTextArea chat, event;
//	private JTextField tPortNumber;
	private Server server;
	private JButton btnList;
	private int port = 1500;

	public ServerUI(int port) {
		super("Chat Server");
		server = null;
		JPanel north = new JPanel();
//		north.add(new JLabel("Port number: "));
//		tPortNumber = new JTextField("  " + port);
//		north.add(tPortNumber);
		stopStart = new JButton("Start server");
		btnList =  new JButton("Show list of users");
		stopStart.addActionListener(this);
		btnList.addActionListener(this);
		north.add(stopStart);
		north.add(btnList);
		add(north, BorderLayout.NORTH);

		JPanel center = new JPanel(new GridLayout(2,1));
		chat = new JTextArea(80,80);
		chat.setEditable(false);
		appendRoom("Chat room.\n");
		center.add(new JScrollPane(chat));
		event = new JTextArea(80,80);
		event.setEditable(false);
		appendEvent("Events log.\n");
		center.add(new JScrollPane(event));	
		add(center);

		// need to be informed when the user click the close button on the frame
		addWindowListener(this);
		setSize(400, 600);
		setVisible(true);
	}		

	// append message to the two JTextArea
	// position at the end
	public void appendRoom(String str) {
		chat.append(str);
		chat.setCaretPosition(chat.getText().length() - 1);
	}
	public void appendEvent(String str) {
		event.append(str);
		event.setCaretPosition(chat.getText().length() - 1);

	}

	// start or stop where clicked
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==stopStart) {
			if(server != null) {
				server.stop();
				server = null;
//				tPortNumber.setEditable(true);
				stopStart.setText("Start server");
				return;
			}	
//			int port;
//			try {
//				port = Integer.parseInt(tPortNumber.getText().trim());
//			}
//			catch(Exception er) {
//				appendEvent("Invalid port number");
//				return;
//			}
			server = new Server(port, this);
			new ServerRunning().start();
			stopStart.setText("Stop server");
//			tPortNumber.setEditable(false);
		}
		if(e.getSource()==btnList) {
			server.showList();
		}
	}

	public static void main(String[] arg) {
		new ServerUI(1500);
	}

	public void windowClosing(WindowEvent e) {
		if(server != null) {
			try {
				server.stop();
			}
			catch(Exception eClose) {
			}
			server = null;
		}
		dispose();
		System.exit(0);
	}
	public void windowClosed(WindowEvent e) {}
	public void windowOpened(WindowEvent e) {}
	public void windowIconified(WindowEvent e) {}
	public void windowDeiconified(WindowEvent e) {}
	public void windowActivated(WindowEvent e) {}
	public void windowDeactivated(WindowEvent e) {}
	
	class ServerRunning extends Thread {
		public void run() {
			server.start();     
			stopStart.setText("Start");
//			tPortNumber.setEditable(true);
			appendEvent("Server crashed\n");
			server = null;
		}
	}

}

