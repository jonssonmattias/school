package f8.client;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class ClientController {
	private ClientUI ui = new ClientUI(this);
	private Client client;
	
	private void showClientUI() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
			    JFrame frame = new JFrame(client.getClass().getName());
			    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			    frame.add(ui);
			    frame.pack();
			    frame.setVisible(true);
			}
		});
	}
	
	public ClientController(Client client) {
		this.client = client;
		client.setClientController(this);
		showClientUI();
	}

	public void put(String name, String age) {
		try {
		    client.put(name, age);
		} catch(IOException e) {
			newResponse(e.toString());
		}
	}

	public void get(String name) {
		try {
			client.get(name);
		} catch (IOException e) {
			newResponse(e.toString());
		}
	}

	public void list() {
		try {
			client.list();
		} catch (IOException e) {
			newResponse(e.toString());
		}
	}

	public void remove(String name) {
		try {
			client.remove(name);
		} catch (IOException e) {
			newResponse(e.toString());
		}
	}

	public void exit() {
		try {
			client.exit();
		} catch (IOException e) {
			newResponse(e.toString());
		}		
	}

	public void newResponse(final String response) {
		SwingUtilities.invokeLater(new Runnable() { // behövs ej för ClientA
			public void run() {
				ui.setResponse(response);
			}
		});
	}

	public static void main(String[] args) {
		try {
			Client clientA = new ClientA("127.0.0.1",725);
			new ClientController(clientA);
//			Client clientB = new ClientB("127.0.0.1",726);
//			new ClientController(clientB);
//			Client clientC = new ClientC("127.0.0.1",727);
//			new ClientController(clientC);
//			Client clientD = new ClientD("127.0.0.1",728);
//			new ClientController(clientD);
//			Client clientA = new ClientA("195.178.227.53",725);
//			new ClientController(clientA);
//			Client clientB = new ClientB("195.178.227.53",726);
//			new ClientController(clientB);
//			Client clientC = new ClientC("195.178.227.53",727);
//			new ClientController(clientC);
//			Client clientD = new ClientD("195.178.227.53",728);
//			new ClientController(clientD);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
