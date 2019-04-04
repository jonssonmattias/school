package f8.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class AgeServerA {
	private ServerController controller;
	
	public AgeServerA(ServerController controller, int port) {
		this.controller = controller;
		new ClientHandler(port).start();
	}
	
	private class ClientHandler extends Thread {
		private int port;
		
		public ClientHandler(int port) {
			this.port = port;
		}
		
		public void run() {
			String[] parts;
			String request, response;
			
			System.out.println("AgeServerA startad");
			try(ServerSocket serverSocket = new ServerSocket(port)) {
				System.out.println("Lyssnar på port nr " + serverSocket.getLocalPort());
				while(true) {
					try( Socket socket = serverSocket.accept();
						 DataInputStream dis = new DataInputStream(socket.getInputStream());
						 DataOutputStream dos = new DataOutputStream(socket.getOutputStream()) ) {
						System.out.println("klient " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());
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
					} catch(Exception e) {
						System.err.println("AgeServerA: " + e);
					}
				}
			} catch(IOException e) {
				System.err.println(e);
				System.out.println("AgeServerA nere");
			}
		}
				
		private String badRequest(String request) throws IOException {
			return "Felaktig request: " + request;
		}
	}
}
