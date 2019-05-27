package assignment5;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * The GUI for assignment 5
 */
public class ClientGUI{
	/**
	 * These are the components you need to handle.
	 * You have to add listeners and/or code
	 */
	private JFrame frame;				// The Main window
	private JTextField txt;				// Input for text to send
	private JButton btnSend;			// Send text in txt
	private JTextArea lstMsg;			// The logger listbox
	private Client client;
	
	/**
	 * Starts the application
	 */
	public ClientGUI(){
		frame = new JFrame();
		frame.setBounds(100, 100, 300,300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setTitle("Client");			// Change to "Multi Chat Server" on server part and vice versa 
		InitializeGUI();					// Fill in components
		frame.setVisible(true);
		frame.setResizable(false);			// Prevent user from change size
	}
	
	/**
	 * Sets up the GUI with components
	 */
	private void InitializeGUI(){
		txt = new JTextField();
		txt.setBounds(13,  13, 177, 23);
		frame.add(txt);
		btnSend = new JButton("Send");
		btnSend.setBounds(197, 13, 75, 23);
		btnSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				if(txt.getText().isEmpty()) {
					JOptionPane.showMessageDialog(null, "Text input can't be empty");
				}
				else {
					client.sendMessage(txt.getText());
					txt.setText("");
				}
			}
		});
		frame.add(btnSend);
		lstMsg = new JTextArea();
		lstMsg.setEditable(false);
		JScrollPane pane = new JScrollPane(lstMsg);
		pane.setBounds(12, 51, 260, 199);
		pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		frame.add(pane);
	}

	/**
	 * Sets a message to the list
	 * 
	 * @param receiveMessage
	 */
	public void setMessage(String receiveMessage) {
		lstMsg.setText(lstMsg.getText()+receiveMessage+"\n");
	}

	/**
	 * Sets the client
	 * 
	 * @param client
	 */
	public void setClient(Client client) {
		this.client=client;
	}
}
