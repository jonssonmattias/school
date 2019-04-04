package f8.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientB implements Client {
	private ClientController controller;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public ClientB(String ip, int port) throws IOException {
		socket = new Socket(ip,port);
		System.out.println("Använder port nr " + socket.getLocalPort());
		dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
		new Listener().start();
	}
	
	public void setClientController(ClientController controller) {
		this.controller = controller;
	}

	public void put(String name, String age) throws IOException {
		dos.writeUTF("PUT " + name + " " + age);
		dos.flush();
	}

	public void get(String name) throws IOException {
		dos.writeUTF("GET " + name);
		dos.flush();
	}

	public void list() throws IOException {
		dos.writeUTF("LIST");
		dos.flush();
	}

	public void remove(String name) throws IOException {
		dos.writeUTF("REMOVE " + name);
		dos.flush();
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
					response = dis.readUTF();
					controller.newResponse(response);
				}
			} catch(Exception e) {
				System.out.println(e);
			}
			try {
				exit();
			} catch(IOException e) {}
//			} finally {
			    controller.newResponse("Klient kopplar ner");
//			}
		}
	}
}
