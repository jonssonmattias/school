package f8.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import f8.Request;

public class ClientD implements Client {
	private ClientController controller;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	
	public ClientD(String ip, int port) throws IOException {
		socket = new Socket(ip,port);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		new Listener().start();
	}
	
	public void setClientController(ClientController controller) {
		this.controller = controller;
	}

	public void put(String name, String ageStr) throws IOException {
		int age = -1;
		try {
			age = Integer.parseInt(ageStr);
		} catch(NumberFormatException e) {}
		oos.writeObject(new Request(Request.Type.PUT,name,age));
		oos.flush();
	}

	public void get(String name) throws IOException {
		oos.writeObject(new Request(Request.Type.GET,name,0));
		oos.flush();
	}

	public void list() throws IOException {
		oos.writeObject(new Request(Request.Type.LIST,null,0));
		oos.flush();
	}

	public void remove(String name) throws IOException {
		oos.writeObject(new Request(Request.Type.REMOVE,name,0));
		oos.flush();
	}

	public void exit() throws IOException {
		if(socket!=null)
		    socket.close();		
	}

	private class Listener extends Thread {
		public void run() {
			String response;
			try {
				while(true) {
					response = ois.readUTF();
					controller.newResponse(response);
				}
			} catch(IOException e) {}
			try {
				exit();
			} catch(IOException e) {}
			controller.newResponse("Klient kopplar ner");
		}
	}
}
