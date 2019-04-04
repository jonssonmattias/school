package temptemp;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import javax.swing.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * 
 * @author Tove Ruman
 * This is the Ui for the client.
 *
 */

public class SendWindow extends JFrame implements WindowListener, Callback{
	private ClientController controller;
	private JTextField txtChatField= new JTextField();
	private JPanel chatHistory= new JPanel();
	private JScrollPane msgScroller= new JScrollPane(chatHistory);

	private JButton connectBtn=new JButton("Connect");
	private JButton disconnectBtn=new JButton("Disconnect");
	private JButton sendBtn=new JButton("Send");
	private JButton getIconBtn=new JButton("Add image");
	private JPanel btnPanel=new JPanel();
	private JPanel southPanel=new JPanel();
	private JPanel centerPanel=new JPanel();
	private JPanel eastPanel=new JPanel();
	private JPanel userPanels=new JPanel();
	private JScrollPane scroller= new JScrollPane(userPanels);
	private ArrayList <JCheckBox>checkBoxList=new ArrayList<JCheckBox>();
	private ArrayList<JCheckBox> contactCheckBoxes=new ArrayList<JCheckBox>();
	
	private JPanel contactPanel=new JPanel();
	private JScrollPane contactScroller=new JScrollPane(contactPanel);
	
	private JPanel myUserPanel=new JPanel();
	private JButton userImageBtn=new JButton("new profilepic");
	private JLabel myUserImage=new JLabel();
	private JLabel myUserName=new JLabel("User");
	
	private Users onlineUsers;
	
	private Users contacts = new Users();
	private ImageIcon icon;
	
	private User myUser=new User("Tove ",new ImageIcon("images/gubbe.jpg"));
	
	public SendWindow() {
		super("Clinet");

		setPreferredSize(new Dimension(900,700));

		setLayout(new BorderLayout(10,10));
		
		southPanel.setPreferredSize(new Dimension(500,150));
		southPanel.setLayout(new BorderLayout());
		txtChatField.setPreferredSize(new Dimension(500,100));
		sendBtn.setPreferredSize(new Dimension(100,100));

		centerPanel.setMaximumSize(new Dimension(400,300));
		msgScroller.setMaximumSize(new Dimension(390,300));
		chatHistory.setMaximumSize(new Dimension(390,300));

		chatHistory.setLayout(new BoxLayout(chatHistory,BoxLayout.PAGE_AXIS));
		chatHistory.setOpaque(true);
		chatHistory.setBackground(Color.WHITE);
		chatHistory.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		
		
		myUserPanel.setPreferredSize(new Dimension(200,100));
		myUserPanel.setLayout(new BorderLayout());
		myUserPanel.setOpaque(true);
		userImageBtn.setPreferredSize(new Dimension(200,30));
		userImageBtn.addActionListener(new BtnListener());
		userImageBtn.setEnabled(true);
		myUserPanel.add(userImageBtn,BorderLayout.SOUTH);
		myUserPanel.add(myUserImage,BorderLayout.CENTER);
		myUserPanel.add(myUserName,BorderLayout.NORTH);
		
		connectBtn.setPreferredSize(new Dimension(100,30));
		disconnectBtn.setPreferredSize(new Dimension(100,30));
		disconnectBtn.setEnabled(false);
		getIconBtn.setPreferredSize(new Dimension(100,30));
		btnPanel.setPreferredSize(new Dimension(100,90));
		btnPanel.setLayout(new GridLayout(3,1));
		btnPanel.add(connectBtn);
		btnPanel.add(disconnectBtn);
		btnPanel.add(getIconBtn);
		connectBtn.addActionListener(new BtnListener());
		disconnectBtn.addActionListener(new BtnListener());
		getIconBtn.addActionListener(new BtnListener());
		getIconBtn.setEnabled(false);
		sendBtn.addActionListener(new BtnListener());
		sendBtn.setEnabled(false);
		
		contactPanel.setPreferredSize(new Dimension(200,400));
		contactPanel.setBackground(Color.LIGHT_GRAY);
		contactPanel.setOpaque(true);
		contactPanel.setLayout(new BoxLayout(contactPanel, BoxLayout.PAGE_AXIS));
		
		eastPanel.setPreferredSize(new Dimension(200,400));
		userPanels.setPreferredSize(new Dimension(200,300));
		userPanels.setBackground(Color.WHITE);
		userPanels.setOpaque(true);
		userPanels.setLayout(new BoxLayout(userPanels, BoxLayout.PAGE_AXIS));
		
		
	
		eastPanel.setLayout(new BorderLayout());
		eastPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		eastPanel.add(scroller, BorderLayout.NORTH);
		eastPanel.add(btnPanel, BorderLayout.SOUTH);
		eastPanel.add(contactScroller,BorderLayout.CENTER);
		
		southPanel.add(sendBtn,BorderLayout.CENTER);
		southPanel.add(myUserPanel,BorderLayout.EAST);
		southPanel.add(txtChatField,BorderLayout.WEST);
		centerPanel.add(msgScroller,BorderLayout.CENTER);
		
		add(southPanel, BorderLayout.SOUTH);
		add(msgScroller,BorderLayout.CENTER);
		add(eastPanel,BorderLayout.EAST);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		addWindowListener(this);
		setVisible(true);
		
	}

	public ClientController getController() {
		return controller;
	}
	@Override
	public void getContacts(Users users) {
		for(User user:users) {
			contacts.add(user);
		}
		addContacts();
	}

	@Override
	public void getUser(User user) {
		myUser=user;
		myUserImage.removeAll();
		myUserName.setText(user.getUsername());
		myUserImage.setIcon(user.getImage());
		
	}
	

	public void connect() {
		try {
		int port=Integer.parseInt(JOptionPane.showInputDialog("assign port "));
		String ip=JOptionPane.showInputDialog("assign ip: ");
		String name=JOptionPane.showInputDialog("User name; ");
		name.toUpperCase();
		controller.connect(name, ip,port);
		connectBtn.setEnabled(false);
		}catch(NumberFormatException e) {
			e.printStackTrace();
		}
	
		
	}
	public void disconnect(){
		System.out.println("sending to controller...");
		controller.saveContacts(saveContacts());
		controller.disconnect();
		checkBoxList.clear(); // Change so stored users are kept
		userPanels.removeAll(); // Change so stored users are kept
		connectBtn.setEnabled(true);
		checkBoxList.clear(); 
		userPanels.removeAll();
	}
	
	public void packMsg() {
		Users listOfReceivers=new Users();
		
		for(int i=0; i<checkBoxList.size();i++) {
			if(checkBoxList.get(i).isSelected()) {
				User user=new User(checkBoxList.get(i).getText());
				listOfReceivers.add(user);
			}
		}
		for(int i=0; i<contactCheckBoxes.size();i++) {
				if(contactCheckBoxes.get(i).isSelected()) {
					User user=new User(contactCheckBoxes.get(i).getText());
					listOfReceivers.add(user);
				}
		}
		if (icon==null) {
			controller.pack(txtChatField.getText(),listOfReceivers);
		}else if(txtChatField.getText()==null) {
			controller.pack(icon,listOfReceivers);
			getIconBtn.setText("Add Image");
			icon=null;
		}else {
			controller.pack(txtChatField.getText(), icon,listOfReceivers);
			getIconBtn.setText("Add Image");
			icon=null;
		}
		txtChatField.setText("");
	}
	public void getIcon() {
		JFileChooser fileChooser= new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg & gif Images", "jpg", "gif");
	    fileChooser.setFileFilter(filter);
	    int returnVal = fileChooser.showOpenDialog(null);
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	       System.out.println("You chose to open this file: " +
	            fileChooser.getSelectedFile().getName());
	       icon = new ImageIcon(fileChooser.getSelectedFile().getPath());
	       getIconBtn.setEnabled(false);
	       getIconBtn.setText(fileChooser.getSelectedFile().getName());
	    }
	}
	public void addContacts() {
		for (User user : contacts) {
			System.out.println("test 1");
			JCheckBox checkbox=new JCheckBox(user.getUsername());
			contactCheckBoxes.add(checkbox);
			checkbox.addActionListener(new BtnListener());
			JPanel userPanel=new JPanel();
			userPanel.setMinimumSize(new Dimension(170,30));
			userPanel.setMaximumSize(new Dimension(170,30));
			userPanel.setVisible(true);
			userPanel.add(checkbox);
			
			userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			contactPanel.add(userPanel);
		}
		revalidate();
		repaint();
	}
		
	

	public void update(Message msg) {
		
		JPanel msgPanel = new JPanel(new BorderLayout());
		JLabel senderLbl=new JLabel();
		System.out.println(msg.getTimeSent().toString());
		JLabel dateLabel=new JLabel(msg.getTimeSent().toString());
		senderLbl.setPreferredSize(new Dimension(380,20));
		dateLabel.setPreferredSize(new Dimension(380,20));
		msgPanel.setMinimumSize(new Dimension(380, 150));
		msgPanel.setVisible(true);
		msgPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		
		if(msg.getSender().equals(myUser)) {
			msgPanel.setBackground(Color.WHITE);
			senderLbl.setText("Me: ");
			
		}else {
			msgPanel.setBackground(Color.LIGHT_GRAY);
			senderLbl.setText(msg.getSender().getUsername()+": ");
		}
		if(msg.getImage()==null){
			JLabel txtLabel=new JLabel(msg.getSender().getUsername() + ": " + msg.getText());
			msgPanel.add(txtLabel);
			
		}else if (msg.getText()==null){
			ImageIcon imageIcon = msg.getImage(); 
			Image image = imageIcon.getImage(); 
			Image rescaledImage = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_DEFAULT); 
			imageIcon = new ImageIcon(rescaledImage);  
			JLabel iconLbl=new JLabel(imageIcon);
			msgPanel.add(iconLbl);
			
		}else {
			ImageIcon imageIcon = msg.getImage(); 
			Image image = imageIcon.getImage(); 
			Image rescaledImage = image.getScaledInstance(120, 120,  java.awt.Image.SCALE_DEFAULT); 
			imageIcon = new ImageIcon(rescaledImage);  
			JLabel iconLbl=new JLabel(imageIcon);
			JLabel txtLabel=new JLabel(msg.getText());
			
			msgPanel.add(senderLbl,BorderLayout.NORTH);
			msgPanel.add(txtLabel,BorderLayout.WEST);
			msgPanel.add(iconLbl,BorderLayout.EAST);
			
		}
		msgPanel.add(dateLabel,BorderLayout.SOUTH);
		chatHistory.add(msgPanel);
		revalidate();
		repaint();
		
	}
	public void update(Users onlineUsers)  {
		this.onlineUsers=onlineUsers;
		checkBoxList.clear(); // Change so stored users are kept
		userPanels.removeAll(); // Change so stored users are kept
		
		for (User user : onlineUsers) {
			JCheckBox checkbox=new JCheckBox(user.getUsername());
			checkBoxList.add(checkbox);
			checkbox.addActionListener(new BtnListener());
			ImageIcon userIcon;
			JLabel imageLbl;
			
			userIcon= user.getImage();
		
			imageLbl=new JLabel(userIcon);
			imageLbl.setPreferredSize(new Dimension(60,50));
			JPanel userPanel=new JPanel(new GridLayout(1,3));
			userPanel.setMinimumSize(new Dimension(170,70));
			userPanel.setMaximumSize(new Dimension(170,100));
			userPanel.setVisible(true);
			userPanel.add(checkbox);
			userPanel.add(imageLbl);
			
			userPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			userPanels.add(userPanel);
		}
		revalidate();
		repaint();
	}
	public void newUserImage() {
		JFileChooser fileChooser= new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("jpg Images", "jpg");
	    fileChooser.setFileFilter(filter);
	    int returnVal = fileChooser.showOpenDialog(null);
	    ImageIcon updatedImg;
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	    	updatedImg = new ImageIcon(fileChooser.getSelectedFile().getPath());
	    } else {
	    	updatedImg = myUser.getImage();
	    }
	    
	    setMyUser(new User(myUser.getUsername(), updatedImg));
	    controller.updateUser(myUser);
	    
	}
	public void setMyUser(User user){
		this.myUser=user;
		myUserName.removeAll();
		myUserImage.removeAll();
		myUserName.setText(user.getUsername());
		myUserImage.setIcon(user.getImage());
		myUserPanel.revalidate();
		myUserPanel.repaint();
	
	}
	
	public LinkedList<String> saveContacts(){
		LinkedList<String> contacts=new LinkedList<String>();
		
		for(int i=0; i<checkBoxList.size();i++) {
			if(checkBoxList.get(i).isSelected()) {
				String userName=checkBoxList.get(i).getText();
				contacts.add(userName);
				System.out.println(userName  + " test ");
			}
		}

		return contacts;
	}
	
	
	private class BtnListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if (connectBtn==e.getSource()) {
				disconnectBtn.setEnabled(true);
				sendBtn.setEnabled(true);
				getIconBtn.setEnabled(true);
				userImageBtn.setEnabled(true);
				connect();
			
			}
			else if (disconnectBtn==e.getSource()) {
				disconnectBtn.setEnabled(false);
				sendBtn.setEnabled(false);
				getIconBtn.setEnabled(false);
				userImageBtn.setEnabled(false);
				connectBtn.setEnabled(true);
				disconnect();
			}
			else if(sendBtn==e.getSource()) {
				packMsg();
				
				getIconBtn.setEnabled(true);
				
			}
			else if(getIconBtn==e.getSource()) {
				getIcon();
			}
			else if(userImageBtn==e.getSource()) {
				newUserImage();
				
			}
			
		}
		
	}

	
	public void setController(ClientController controller) {
		this.controller = controller;
		
	}
	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowClosing(WindowEvent arg0) {
		controller.disconnect();	
	}
	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
