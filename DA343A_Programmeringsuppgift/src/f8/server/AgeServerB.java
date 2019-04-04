package f8.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AgeServerB {
	private ServerController controller;
	
	public AgeServerB(ServerController controller, int port) {
		this.controller = controller;
		new Connection(port).start();
	}
	
	private class Connection extends Thread {
		private int port;
		
		public Connection(int port) {
			this.port = port;
		}
		public void run() {
			Socket socket = null;
			System.out.println("AgeServerB startad");
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Lyssnar p� port nr " + serverSocket.getLocalPort());
				while(true) {
					try {
						socket = serverSocket.accept();
						new ClientHandler(socket);
					} catch(IOException e) { // Exception bättre? Kan något annat Exception kastas vid instansiering av ClientHandler?
						System.err.println(e);
						if(socket!=null)
							socket.close();
					}
				}
			} catch(IOException e) {
				System.err.println(e);
			}
			System.out.println("Server stoppad");
		}
	}
	
	private class ClientHandler extends Thread {
		private Socket socket;
		private DataInputStream dis;
		private DataOutputStream dos;
		
		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
			dos = new DataOutputStream(new BufferedOutputStream(socket.getOutputStream()));
			start();
		}
		
		public void run() {
			String[] parts;
			String request, response;
			try {
				while(true) {
					request = dis.readUTF();
					parts = request.split(" ");
					if(parts.length>=3 && parts[0].equals("PUT")) {
						response = controller.put(parts[1],parts[2]);
					} else if(parts.length>=2 && parts[0].equals("GET")) {
						response = controller.get(parts[1]);
					} else if(parts.length>=1 && parts[0].equals("LIST")) {
						response = controller.list();
					} else if(parts.length>=2 && parts[0].equals("REMOVE")) {
						response = controller.remove(parts[1]);
					} else {
						response = badRequest(request);
					}
					dos.writeUTF(response);
					dos.flush();
				}
			} catch(Exception e) {
				if(socket!=null) {
					try {
						socket.close();
					} catch(IOException e2) {}
				}
			}
			System.out.println("Klient nerkopplad");
		}
				
		private String badRequest(String request) throws IOException {
			return "Felaktig request: " + request;
		}
	}
}
