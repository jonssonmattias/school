package gu;

import javax.swing.*;
import javax.swing.filechooser.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

/**
 * Is used to create a ClientUI
 * 
 * @author Markus Masalkovski, Mattias Jönsson, Ramy Behnam, Lukas Rosberg, Sofie Ljungcrantz
 *
 */
public class ClientUI extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	private final int port = 1500;
	private final String ipAdress = "127.0.0.1";
	private Client client;
	private boolean connected, imageChoosed=false;
	private User user;
	private ImageIcon profileImage, img;
	private ArrayList<User> reciverList = new ArrayList<User>();
	private static Contacts contacts;

	private JPanel panelNorth = new JPanel();
	private JPanel panelNorthCenter = new JPanel();
	private JPanel panelNorthSouth = new JPanel();
	private JPanel panelCenter = new JPanel();
	private JPanel panelSouth = new JPanel();

	private JButton btnChoose = new JButton("Choose");
	private JButton btnConnect = new JButton("Connect to server");
	private JButton btnSend = new JButton("Send");
	private JButton btnImage = new JButton("+");
	private JButton btnDisconnect = new JButton("Disconnect");
	private JButton btnContacts = new JButton("Contact list");

	private JTextPane image = new JTextPane();

	private JTextField tfUsername = new JTextField();
	private JList<Object> textPaneViewer;
	private JTextPane textPaneMessage = new JTextPane();

	private DefaultListModel<Object> messageListModel;

	private JLabel lblName = new JLabel("Username: ", SwingConstants.RIGHT);
	private JLabel lblTo = new JLabel("Send to:");

	/**
	 * Constructs a ClientUI
	 */
	public ClientUI() {
		contacts = new Contacts(this);
		connected(false);
		actionListener();
		setLayout();
		setPreferredSize();
		add(panelNorth, BorderLayout.NORTH);
		add(panelCenter, BorderLayout.CENTER);
		add(panelSouth, BorderLayout.SOUTH);
		panelNorthCenter.add(btnChoose);
		panelNorthCenter.add(image);
		panelNorthCenter.add(lblName);
		panelNorthCenter.add(tfUsername);
		panelNorthSouth.add(btnConnect);
		panelNorthSouth.add(btnDisconnect);

		panelNorth.add(panelNorthCenter, BorderLayout.CENTER);
		panelNorth.add(panelNorthSouth, BorderLayout.SOUTH);

		messageListModel = new DefaultListModel<Object>();
		textPaneViewer = new JList<Object>(messageListModel);
		textPaneViewer.setSelectionModel(new DisabledItemSelectionModel());
		textPaneViewer.setBackground(new Color(220,220,220));
		panelCenter.add(new JScrollPane(textPaneViewer),BorderLayout.CENTER);
		panelCenter.add(lblTo, BorderLayout.SOUTH);

		panelSouth.add(btnSend, BorderLayout.EAST);
		panelSouth.add(textPaneMessage, BorderLayout.CENTER);
		panelSouth.add(btnImage,BorderLayout.WEST);
		panelSouth.add(btnContacts, BorderLayout.SOUTH);

		image.setEnabled(false);
	}

	private void actionListener() {
		btnChoose.addActionListener(this);
		btnConnect.addActionListener(this);
		btnSend.addActionListener(this);
		btnImage.addActionListener(this);
		btnDisconnect.addActionListener(this);
		btnContacts.addActionListener(this);
	}
	private void setLayout() {
		setLayout(new BorderLayout());
		panelNorthCenter.setLayout(new GridLayout(1,4));
		panelNorthSouth.setLayout(new GridLayout(0,2));
		panelNorth.setLayout(new BorderLayout());
		panelCenter.setLayout(new BorderLayout());
		panelSouth.setLayout(new BorderLayout());
	}
	private void setPreferredSize() {
		btnChoose.setPreferredSize(new Dimension(80,30));
		btnChoose.setOpaque(true);
		btnConnect.setPreferredSize(new Dimension(80,30));
		btnConnect.setOpaque(true);
		btnSend.setPreferredSize(new Dimension(80,30));
		btnSend.setOpaque(true);
		btnImage.setPreferredSize(new Dimension(80,30));
		btnImage.setOpaque(true);
		btnContacts.setPreferredSize(new Dimension(80,30));
		btnContacts.setOpaque(true);
		tfUsername.setPreferredSize(new Dimension(150, 40));
	}
	/**
	 * Appends a object to the list
	 * 
	 * @param obj the object
	 */
	public void append(Object obj) {
		if(obj instanceof Message) {
			Message m = (Message)obj;
			messageListModel.addElement(m.getIcon());
			messageListModel.addElement(m.getText()+" sent from "+m.getSender().getUsername()+". Sent "+m.getTimeSent());
		}	
		else {
			messageListModel.addElement(obj);
		}

	}
	public void connected(boolean connected) {
		this.connected = connected;
		btnDisconnect.setEnabled(connected);
		btnConnect.setEnabled(!connected);
		btnSend.setEnabled(connected);
		btnImage.setEnabled(connected);
		btnChoose.setEnabled(!connected);
		btnContacts.setEnabled(connected);
		textPaneMessage.setEnabled(connected);
		tfUsername.setEnabled(!connected);
		if(messageListModel!=null && !connected) {
			messageListModel.removeAllElements();
			textPaneViewer.setModel(messageListModel);
		}
	}
	/**
	 * Sets the receiver list
	 * 
	 * @param reciverList the reciverlist 
	 */
	public void setReciverList(ArrayList<User> reciverList) {
		for(User u : reciverList) {
			this.reciverList.add(u);
		}
		for(User u:reciverList) {
			if(!lblTo.getText().contains(u.getUsername())) {
				lblTo.setText(lblTo.getText()+u.getUsername()+",");
			}
		}
	}
	private ImageIcon resizeImage(String ImagePath, JTextPane tp){
		ImageIcon MyImage = new ImageIcon(ImagePath);
		Image img = MyImage.getImage();
		Image newImg = img.getScaledInstance(tp.getHeight(), tp.getHeight(), Image.SCALE_SMOOTH);
		ImageIcon image = new ImageIcon(newImg);
		return image;
	}
	private ImageIcon imageChooser() {
		ImageIcon img = null;
		JFileChooser file = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","gif","png");
		file.addChoosableFileFilter(filter);
		int result = file.showSaveDialog(null);
		if(result == JFileChooser.APPROVE_OPTION){
			File selectedFile = file.getSelectedFile();
			String path = selectedFile.getAbsolutePath();
			img = resizeImage(path, image);
		}
		return img;
	}
	/**
	 * @return the Contacts-object
	 */
	public static Contacts getContacts() {
		return contacts;
	}

	/**
	 * @return the Client-object
	 */
	public Client getClient() {
		return client;
	}
	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==btnChoose) {
			profileImage=imageChooser();
			if(profileImage!=null)
				image.insertIcon(profileImage);
		}
		if(e.getSource()==btnConnect) {
			String username = tfUsername.getText().trim();
			if(username!=""&& profileImage!=null) {
				user = new User(username, profileImage);
				client = new Client(ipAdress, port, user, this);
				if(!client.start()) return;
				connected(true);
			}
			else {
				JOptionPane.showMessageDialog(null, "Please fill in your name and chose a picture");
			}
		}
		if(e.getSource()==btnSend) {
			if(!textPaneMessage.getText().isEmpty()) {
				if(imageChoosed) client.sendMessage(new Message(Message.MESSAGE, textPaneMessage.getText(),img,user,reciverList));
				else {
					client.sendMessage(new Message(Message.MESSAGE, textPaneMessage.getText(),null,user,reciverList));
				}
				textPaneMessage.setText("");
				textPaneMessage.repaint();
				lblTo.setText("Send to:");
				for(int i=0;i<reciverList.size();i++) {
					System.out.println(reciverList.get(i));
					reciverList.remove(i);
				}
				img=null;
			}
		}
		if(e.getSource()==btnImage) {
			img=imageChooser();
			imageChoosed=true;
			textPaneMessage.insertIcon(img);
		}
		if(e.getSource()==btnDisconnect) {
			try {
				contacts.writeContacts();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			contacts.clearContactList();
			client.logout();
			connected(false);
		}
		if(e.getSource() == btnContacts) {
			client.clearUserlist();
			contacts.createFrame();
		}
	}
	private class DisabledItemSelectionModel extends DefaultListSelectionModel {
		private static final long serialVersionUID = 1L;
		public void setSelectionInterval(int index0, int index1) {
			super.setSelectionInterval(-1,-1);
		}
	}
	public static void main(String[] args) {
		ClientUI chatUI = new ClientUI();
		JFrame frame = new JFrame("Chat");
		frame.setPreferredSize(new Dimension(450, 600));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.pack();
		frame.getContentPane().add(chatUI);
	}
}