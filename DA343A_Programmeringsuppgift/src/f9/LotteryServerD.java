package f9;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LotteryServerD implements Runnable{
	private String[] winningTickets = {"111111","222222","333333","444444","555555","666666","777777","888888","999999"};
	private Thread server = new Thread(this);
	private ServerSocket serverSocket;
	private Random rand = new Random();

	public LotteryServerD(int port) throws IOException {
		serverSocket = new ServerSocket(port);
		server.start();
	}

	private String getResponse(String ticket) {
		randomPause();
		return checkTicket(ticket);
	}

	private void randomPause() {
		try {
			Thread.sleep(rand.nextInt(4)*1000);
		} catch (InterruptedException e) {}    	
	}

	// synkronisering behövs ofta då flera trådar använder samma resurs.
	// Behövs ej om samtliga trådar endast läser från resursen
	private String checkTicket(String ticket) { 
		for(int i=0; i<winningTickets.length; i++) {
			if(ticket.equals(winningTickets[i])) {
				return "VINST";
			}
		}
		return "NIT";
	}

	public void run() {
		System.out.println("ServerD running, port " + serverSocket.getLocalPort());
		while(true) {
			try  {
				Socket socket = serverSocket.accept();
				new ClientHandler(socket).start();
			} catch(IOException e) { 
			}
		}
	}

	private class ClientHandler extends Thread {
		private Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			String ticket,response;
			try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					DataInputStream dis = new DataInputStream(socket.getInputStream())	) {
				ticket = dis.readUTF();
				response = getResponse(ticket);
				dos.writeUTF(response);
				dos.flush();
			} catch(IOException e) {
			}
			try {
				socket.close();
			} catch(Exception e) {}
		}
	}

	public static void main(String[] args) throws IOException {
		new LotteryServerD(3463);
	}
}

/*
public class LotteryServerD implements Runnable{
	private final static Logger requestLog = Logger.getLogger("requests"); 
	private final static Logger errorLog = Logger.getLogger("errors");
	private FileHandler requestFile = new FileHandler("files/requestLog.log");
	private FileHandler errorFile = new FileHandler("files/errorLog.log");

	private String[] winningTickets = {"111111","222222","333333","444444","555555","666666","777777","888888","999999"};
	private Thread server = new Thread(this);
	private ServerSocket serverSocket;
	private Random rand = new Random();

	public LotteryServerD(int port) throws IOException {
//		requestFile.setFormatter(new SimpleFormatter()); // xml default in file
//		requestLog.setUseParentHandlers(false); // not in console
		requestLog.addHandler(requestFile); // log to file
		errorLog.addHandler(errorFile); // log to file (and console)    	

		serverSocket = new ServerSocket(port);
		server.start();
	}

	private String getResponse(String ticket) {
		randomPause();
		return checkTicket(ticket);
	}

	private void randomPause() {
		try {
			Thread.sleep(rand.nextInt(4)*1000);
		} catch (InterruptedException e) {}    	
	}

  // synkronisering behövs ofta då flera trådar använder samma resurs.
  // Behövs ej om samtliga trådar endast läser från resursen
	private synchronized String checkTicket(String ticket) { 
		for(int i=0; i<winningTickets.length; i++) {
			if(ticket.equals(winningTickets[i])) {
				return "VINST";
			}
		}
		return "NIT";
	}

	public void run() {
		System.out.println("ServerD running, port " + serverSocket.getLocalPort());
		while(true) {
			try  {
				Socket socket = serverSocket.accept();
				new ClientHandler(socket).start();
			} catch(IOException e) { 
				errorLog.severe(e.toString());
			}
		}
	}

	private class ClientHandler extends Thread {
		private Socket socket;

		public ClientHandler(Socket socket) {
			this.socket = socket;
		}

		public void run() {
			String ticket,response;
			try (DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
					DataInputStream dis = new DataInputStream(socket.getInputStream())	) {
				ticket = dis.readUTF();
				response = getResponse(ticket);
				if(response.equals("NIT"))
					requestLog.warning(socket.getInetAddress() + ": Lott=" +ticket+", Resultat="+response);
				else
					requestLog.info(socket.getInetAddress() + ": Lott=" +ticket+", Resultat="+response);
				dos.writeUTF(response);
				dos.flush();
			} catch(IOException e) {
				requestLog.warning(e.toString());
			}
			try {
				socket.close();
			} catch(Exception e) {}
		}
	}

	public static void main(String[] args) throws IOException {
		new LotteryServerD(3463);
	}
}
*/
