package f8;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class Time {
	public void printTime() {
		new ReadTime().start();
	}

	private class ReadTime extends Thread {
		public void run() {
			try( Socket socket = new Socket("time.nist.gov", 13);
//			try( Socket socket = new Socket("localhost", 13); // Laboration 9
					InputStreamReader isr = new InputStreamReader(socket.getInputStream(),"UTF-8");
					BufferedReader br = new BufferedReader(isr) ) {
				System.out.println(socket);
				socket.setSoTimeout(10000); 

				String time = br.readLine();
				while(time!=null) {
					System.out.println( time );
					time = br.readLine();
				}
//				int chr;
//				while((chr=br.read())!=-1) {
//					System.out.print((char)chr);
//				}
			} catch(IOException e) {
				System.err.println(e);
			}
			System.out.println("That's all!");
		}
		//		}
	}

	public static void main(String[] args) {
		Time ex = new Time();
		ex.printTime();
	}
}
