package f8.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import f8.Request;

public class AgeServerD {
	private ServerController controller;
	
	public AgeServerD(ServerController controller, int port) {
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
			System.out.println("AgeServerD startad");
			try (ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Lyssnar på port nr " + serverSocket.getLocalPort());
				while(true) {
					try {
						socket = serverSocket.accept();
						new ClientHandler(socket);
					} catch(IOException e) {
						System.err.println(e);
						if(socket!=null)
							socket.close();
					}
				}
			} catch(IOException e) {
				System.err.println(e);
			} finally {
			    System.out.println("AgeServerD nere");
			}
		}
	}
	
	private class ClientHandler extends Thread {
		private Socket socket;
		private ObjectInputStream ois;
		private ObjectOutputStream oos;
		
		public ClientHandler(Socket socket) throws IOException {
			this.socket = socket;
			ois = new ObjectInputStream(socket.getInputStream());
			oos = new ObjectOutputStream(socket.getOutputStream());
			oos.flush();
			start();
		}
		
		public void run() {
			Request request;
			String response;
			System.out.println("Klienthanterare startad");
			try {
				while(true) {
					try {
						request = (Request)ois.readObject();
						if(request.getType().equals(Request.Type.PUT)) {
							response = controller.put(request.getName(),request.getAge()+"");
						} else if(request.getType().equals(Request.Type.GET)) {
							response = controller.get(request.getName());
						} else if(request.getType().equals(Request.Type.LIST)) {
							response = controller.list();
						} else if(request.getType().equals(Request.Type.REMOVE)) {
							response = controller.remove(request.getName());
						} else {
							response = badRequest(request);
						}
						oos.writeUTF(response);
						oos.flush();
					} catch(ClassNotFoundException e) {}
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
				
		private String badRequest(Request request) throws IOException {
			String response = "Felaktig request: " + request.toString();
//			dis.skip(dis.available());
			return response;
		}
	}
}
