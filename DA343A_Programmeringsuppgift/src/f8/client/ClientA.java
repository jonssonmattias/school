package f8.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientA implements Client {
	private ClientController controller;
	private String ip;
	private int port;
	private Socket socket;
	private DataInputStream dis;
	private DataOutputStream dos;
	
	public ClientA(String ip, int port) throws IOException {
		this.ip = ip;
		this.port = port;
	}
	
	public void setClientController(ClientController controller) {
		this.controller = controller;
	}
	
	private void connect() throws IOException {
		socket = new Socket(ip,port);
		socket.setSoTimeout(5000);
		dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
		dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
	}
	
	private void disconnect() throws IOException {
		socket.close();
	}

	public void put(String name, String age) throws IOException {
		connect();
		dos.writeUTF("PUT " + name + " " + age);
		dos.flush();
		String response = dis.readUTF(); // blockerar händelsetråden!
		controller.newResponse(response); 
		disconnect();
	}

	public void get(String name) throws IOException {
		connect();
		dos.writeUTF("GET " + name);
		dos.flush();
		String response = dis.readUTF();
		controller.newResponse(response);
		disconnect();
	}

	public void list() throws IOException {
		connect();
		dos.writeUTF("LIST");
		dos.flush();
		String response = dis.readUTF();
		controller.newResponse(response);
		disconnect();
	}

	public void remove(String name) throws IOException {
		connect();
		dos.writeUTF("REMOVE " + name);
		dos.flush();
		String response = dis.readUTF();
		controller.newResponse(response);
		disconnect();
	}

	public void exit() throws IOException {
	}
}
