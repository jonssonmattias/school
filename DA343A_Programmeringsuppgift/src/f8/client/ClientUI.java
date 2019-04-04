package f8.client;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class ClientUI extends JPanel {
	private ClientController client;
	private JTextField tfName = new JTextField();
	private JTextField tfAge = new JTextField();
	private JButton btnPut = new JButton("PUT");
	private JButton btnGet = new JButton("GET");
	private JButton btnList = new JButton("LIST");
	private JButton btnRemove = new JButton("REMOVE");
	private JButton btnExit = new JButton("QUIT");
	private JTextArea taResponse = new JTextArea();
	
	public ClientUI(ClientController client) {
		this.client = client;
		setLayout(new BorderLayout());
		taResponse.setPreferredSize(new Dimension(400,400));
		add(northPanel(),BorderLayout.NORTH);
		add(new JScrollPane(taResponse),BorderLayout.CENTER);
		initListeners();
	}

	private JPanel northPanel() {
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(inputPanel(),BorderLayout.CENTER);
		panel.add(buttonPanel(),BorderLayout.EAST);
		return panel;
	}
	
	private JPanel inputPanel() {
		JPanel panel = new JPanel(new GridLayout(4,1));
		panel.add(new JLabel("Namn"));
		panel.add(tfName);
		panel.add(new JLabel("Ålder"));
		panel.add(tfAge);
		return panel;
	}
	
	private JPanel buttonPanel() {
		JPanel panel = new JPanel(new GridLayout(5,1));
		panel.add(btnPut);
		panel.add(btnGet);
		panel.add(btnList);
		panel.add(btnRemove);
		panel.add(btnExit);
		return panel;
	}
	
	private void initListeners() {
		ActionListener listener = new ButtonListener();
		btnPut.addActionListener(listener);
		btnGet.addActionListener(listener);
		btnList.addActionListener(listener);
		btnRemove.addActionListener(listener);
		btnExit.addActionListener(listener);
	}
	
	public void setResponse(String response) {
		taResponse.setText(response);
	}
	
	private class ButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if(e.getSource()==btnPut) {
				client.put(tfName.getText(), tfAge.getText());
			} else if(e.getSource()==btnGet) {
				client.get(tfName.getText());
			} else if(e.getSource()==btnList) {
				client.list();
			} else if(e.getSource()==btnRemove) {
				client.remove(tfName.getText());
			} else if(e.getSource()==btnExit) {
				client.exit();
			}
		}
	}
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				JFrame frame = new JFrame("Client UI");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.add(new ClientUI(null));
				frame.pack();
				frame.setVisible(true);
			}
		});
	}
}
