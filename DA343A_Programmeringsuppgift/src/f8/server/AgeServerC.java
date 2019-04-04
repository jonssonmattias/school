package f8.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AgeServerC {
	private ServerController controller;
	
	public AgeServerC(ServerController controller, int port) {
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
			System.out.println("AgeServerC startad");
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Lyssnar på port nr " + serverSocket.getLocalPort());
				while(true) {
					try {
						socket = serverSocket.accept();
						new ClientHandler(socket);
					} catch(Exception e) {
						System.err.println(e);
						if(socket!=null)
							socket.close();
					}
				}
			} catch(IOException e) {
				System.err.println(e);
			}
			System.out.println("AgeServerC nere");
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
			String request, response;
			try {
				while(true) {
					request = dis.readUTF();
					if(request.equals("PUT")) {
						response = put();
					} else if(request.equals("GET")) {
						response = get();
					} else if(request.equals("LIST")) {
						response = controller.list();
					} else if(request.equals("REMOVE")) {
						response = remove();
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
		
		private String put() throws IOException {
			String name = dis.readUTF();
			int age = dis.readInt();
			return controller.put(name, ""+age);
		}
		
		private String get() throws IOException {
			String name = dis.readUTF();
			return controller.get(name);
		}
		
		private String remove() throws IOException {
			String name = dis.readUTF();
			return controller.remove(name);
		}
				
		private String badRequest(String request) throws IOException {
			String response = "Felaktig request: " + request;
//			dis.skip(dis.available());
			return response;
		}
	}
}
