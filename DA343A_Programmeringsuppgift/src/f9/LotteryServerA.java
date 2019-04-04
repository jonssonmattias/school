package f9;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Random;

public class LotteryServerA implements Runnable{
	private String[] winningTickets = {"111111","222222","333333","444444","555555","666666","777777","888888","999999"};
	private Thread server = new Thread(this);
	private DatagramSocket socket;
	private Random rand = new Random();

	public LotteryServerA(int port) {
		try {
			socket = new DatagramSocket(port);
			server.start();
		} catch (SocketException e) {
			// Logging
		}
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

	private String checkTicket(String ticket) { 
		for(int i=0; i<winningTickets.length; i++) {
			if(ticket.equals(winningTickets[i])) {
				return "VINST";
			}
		}
		return "NIT";
	}

	public void run() {
		DatagramPacket packet;
		String ticket, response;
		boolean win;
		byte[] readBuffer = new byte[256];
		byte[] outData;
		System.out.println("ServerA running, port " + socket.getLocalPort());
		while(true) {
			try {
				packet = new DatagramPacket(readBuffer,readBuffer.length);

				socket.receive(packet);
				ticket = new String(packet.getData(),0,packet.getLength());
				System.out.println("Klient: " + packet.getAddress() + ", Lott: " + ticket);

				response = getResponse(ticket);
				outData = response.getBytes();
				packet = new DatagramPacket(outData,outData.length,packet.getAddress(),packet.getPort());
				socket.send(packet);
			} catch(Exception e) { 
				System.err.println(e);
			}
		}
	}

	public static void main(String[] args) {
		new LotteryServerA(3460);
	}
}
