package se.mau.mattiasjonsson.p2.Connection;

import android.graphics.Bitmap;
import android.util.Log;

import org.json.simple.JSONObject;

import java.io.*;
import java.net.*;

public class TCPConnection {
    private RunOnThread thread;
    private Receive receive;
    private ReceiveListener listener;
    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    private InetAddress address;
    private int port;
    private String ip;
    private Exception exception;

    public TCPConnection(String ip, int port, ReceiveListener listener) {
        this.ip = ip;
        this.port = port;
        this.listener = listener;
        thread = new RunOnThread();

    }

    public void connect() {
        thread.start();
        thread.execute(new Connect());
    }

    public void disconnect() {
        thread.execute(new Disconnect());
    }

    public void send(JSONObject jsonObject) {
        thread.execute(new Send(jsonObject));
    }

    public void uploadImage(Bitmap bitmap, String imageID, int port){
        thread.execute(new UploadImage(bitmap, imageID, port));
    }

    public void downloadImage(String imageID, int port){
        thread.execute(new DownloadImage(imageID, port));
    }


    private class Receive extends Thread {
        public void run() {
            String result;
            try {
                while (receive != null) {
                    result = input.readUTF();
                    listener.newMessage(result);
                }
            } catch (Exception e) { // IOException, ClassNotFoundException
                receive = null;
            }
        }
    }

    private class Connect implements Runnable {
        public void run() {
            try {
                Log.d("TCPConnection","Connect-run");
                address = InetAddress.getByName(ip);
                Log.d("TCPConnection-Connect","Skapar socket");
                socket = new Socket(address, port);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                output.flush();
                Log.d("TCPConnection-Connect","Strömmar klara");
                listener.newMessage("CONNECTED");
                receive = new Receive();
                receive.start();
            } catch (Exception e) { // SocketException, UnknownHostException
                Log.d("TCPConnection-Connect",e.toString());
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class Disconnect implements Runnable {
        public void run() {
            try {
                if (socket != null)
                    socket.close();
                if (input != null)
                    input.close();
                if (output != null)
                    output.close();
                thread.stop();
                listener.newMessage("CLOSED");
            } catch(IOException e) {
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class Send implements Runnable {
        private JSONObject jsonObject;

        public Send(JSONObject jsonObject) {
            this.jsonObject = jsonObject;
        }

        public void run() {
            try {
                output.writeUTF(jsonObject.toString());
                output.flush();
            } catch (IOException e) {
                exception = e;
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class UploadImage implements Runnable {
        private Bitmap bitmap;
        private String imageID;
        private int port;

        public UploadImage(Bitmap bitmap, String imageID, int port) {
            this.bitmap = bitmap;
            this.imageID=imageID;
            this.port=port;
        }
        public void run() {
            try {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                byte[] byteArray = stream.toByteArray();
                Socket imageSocket = new Socket( address, port );
                ObjectOutputStream output= new ObjectOutputStream(imageSocket.getOutputStream());
                output.flush();
                output.writeUTF(imageID);
                output.flush();
                output.writeObject(byteArray);
                output.flush();
                imageSocket.close();
            } catch (IOException e) {
                exception = e;
                e.printStackTrace();
                listener.newMessage("EXCEPTION");
            }
        }
    }

    public class DownloadImage implements Runnable {
        private String imageID;
        private int port;

        public DownloadImage(String imageID, int port) {
            this.imageID=imageID;
            this.port=port;
        }
        public void run() {
            try {
                byte[] downloadArray; // tilldelas bilddata från servern, sista instruktionen nedan
                Socket socket = new Socket(address, port);
                ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());
                output.flush();

                output.writeUTF(imageID);
                output.flush();
                downloadArray = (byte[]) input.readObject();
                listener.newMessage(downloadArray);
                socket.close();
            }catch (Exception e) {
                exception = e;
                e.printStackTrace();
                listener.newMessage("EXCEPTION");
            }
        }
    }

}
