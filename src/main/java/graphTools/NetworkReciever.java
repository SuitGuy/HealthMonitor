package graphTools;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.MessageDigest;

public class NetworkReciever implements Runnable{
	protected static int port = 19995;
	private Socket con;
	private MessageDigest md;
	
	public NetworkReciever(Socket con, int instance) {
		this.con = con;
		System.out.println("NEW CONNECTION INSTANCE: " + instance);
	}
	
	public static void main(String[] args) {
		int count = 0;
		if(args.length > 0 && args.length < 2){ 
			port = Integer.parseInt(args[1]);
		}
		if(args.length > 2){ 
			System.out.println("To many arguments supplied");
			System.exit(1);
		}
		try {

			@SuppressWarnings("resource")
			ServerSocket socket = new ServerSocket(port);
			System.out.println("Server Initialized With Port " + port);
			// check socket for incoming connections and start new processing
			// thread.
			while (true) {
				Socket connection = socket.accept();
				Runnable runnable = new NetworkReciever(connection, ++count);
				Thread thread = new Thread(runnable);
				thread.start();
			}
		} catch (BindException e1) {
			System.err.println("ERROR BINDING SOCKET. SOCKET PROBABLY IN USE:");
			e1.printStackTrace();
		} catch (Exception e2) {

			System.err.println("UNHANDLED EXCEPTION OCCURED:");
			e2.printStackTrace();
		}
	}

	public void run() {
		try {
			 BufferedReader in =
				        new BufferedReader(
				            new InputStreamReader(con.getInputStream()));
			
			String receiveBuffer = "Empty Nothing here";
			while ((receiveBuffer = in.readLine()) != null) {
			    System.out.println(receiveBuffer);
			}
			System.out.println(receiveBuffer);
		}catch(Exception e){ 
			System.err.println("something went a tad wrong.");
			e.printStackTrace();
		}
		
	}

}
