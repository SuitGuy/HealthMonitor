package graphTools;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

public class FileWriter {
	private static final long serialVersionUID = 1L;
	private static int port = 19995;
	final TimeSeriesCollection dataset;
	private int DATASIZE = 10;
	final TimeSeries series = new TimeSeries("Paul", Millisecond.class);;
	private final static String FILENAME = "C:\\Users\\Paul\\Desktop\\ecgTest.dat";
	Millisecond ms = new Millisecond();
	
	
	public FileWriter() {
		super();

		dataset = new TimeSeriesCollection(this.series);
		

	}
	
	public static void main(final String[] args) throws InterruptedException, IOException {
		int count = 0;
		if (args.length > 0 && args.length < 2) {
			port = Integer.parseInt(args[1]);
		} else {
			System.out.println("No/Invalid Arguments supplied resorting to default port 19995");
		}
		if (args.length > 2) {
			System.out.println("To many arguments supplied");
			System.exit(1);
		}

		final FileWriter Test3 = new FileWriter();
		@SuppressWarnings("resource")
		ServerSocket socket = new ServerSocket(port);
		System.out.println("Server Initialized With Port " + port);
		System.out.println(socket.getLocalPort());
		PrintWriter pw = new PrintWriter(new File("output.csv"));
		StringBuilder sb  = new StringBuilder();
		
		while(true){ 
			Socket connection = socket.accept();
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			
			String receiveBuffer = "Empty Nothing here";
			while ((receiveBuffer = in.readLine()) != null) {
				String[] parts = receiveBuffer.split("-");
				Double data =  Double.parseDouble(parts[0]);
				pw.write(data + "\n");
				pw.flush();
				
			}
			System.out.println(receiveBuffer);
			
		}
	}
}
