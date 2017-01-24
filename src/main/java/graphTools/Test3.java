package graphTools;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.SwingWorker;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

import utils.Tuple;

/**
 * Test3 class used to Test3 possible solutions for the live rendering of ECG
 * data.
 * 
 * @author Paul
 *
 */
public class Test3 extends ApplicationFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static int port = 19995;
	final TimeSeriesCollection dataset;
	private int DATASIZE = 10;
	final TimeSeries series = new TimeSeries("Paul", Millisecond.class);;
	private final static String FILENAME = "C:\\Users\\Paul\\Desktop\\ecgTest.dat";
	Millisecond ms = new Millisecond();

	public Test3(final String title) {

		super(title);

		dataset = new TimeSeriesCollection(this.series);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);

	}

	/**
	 * creates an initial data set from file used in Test3ing may not be needed
	 * for network version unless use found for reading from file.
	 * 
	 * @return a sample dataset.
	 */
	/*
	 * private XYDataset createDataset(String fileName) { BufferedReader br;
	 * double i = 1.0; try { br = new BufferedReader(new InputStreamReader(new
	 * FileInputStream(fileName))); try { String line; while ((line =
	 * br.readLine()) != null && i < DATASIZE) { series.add(i,
	 * Double.parseDouble(line)); i++; } } catch (IOException e) {
	 * System.err.println("Error whilst reading from file.");
	 * e.printStackTrace(); } finally { try { br.close(); } catch (IOException
	 * e) { System.err.println("error closing file."); e.printStackTrace(); } }
	 * } catch (FileNotFoundException e) {
	 * System.err.println("Error Could not locate file: " + fileName);
	 * e.printStackTrace(); }
	 * 
	 * final XYSeriesCollection dataset = new XYSeriesCollection();
	 * dataset.addSeries(series); return dataset;
	 * 
	 * }
	 */

	/**
	 * Creates a chart.
	 * 
	 * @param dataset
	 *            the data for the chart.
	 * 
	 * @return a chart.
	 */
	private JFreeChart createChart(final XYDataset dataset) {

		// create the chart...
		final JFreeChart chart = ChartFactory.createTimeSeriesChart("ECG", // chart
																			// title
				"Time", // x axis label
				"mV", // y axis label
				dataset, // data
				true, // include legend
				true, // tooltips
				false // urls
		);

		chart.setBackgroundPaint(Color.white);
		final XYPlot plot = chart.getXYPlot();
		plot.setBackgroundPaint(Color.lightGray);
		plot.setDomainGridlinePaint(Color.white);
		plot.setRangeGridlinePaint(Color.white);

		final XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();

		plot.setRenderer(renderer);

		final NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
		rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());

		NumberAxis range = (NumberAxis) plot.getRangeAxis();
		range.setRange(1.0, 3.0);
		range.setTickUnit(new NumberTickUnit(0.1));
		ValueAxis timeaxis = plot.getDomainAxis();
		timeaxis.setAutoRange(true);
		timeaxis.setFixedAutoRange(3000.0);
		return chart;

	}

	/**
	 * launches Test3 graph
	 *
	 * @param args
	 *            ignored.
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws InterruptedException {
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

		final Test3 Test3 = new Test3("Electrocardiogram");
		Test3.pack();
		RefineryUtilities.centerFrameOnScreen(Test3);
		Test3.setVisible(true);

		SwingWorker swingWorker = Test3.createUpdateThread();
		swingWorker.execute();
	}

	private SwingWorker<List<Tuple<Double, Millisecond>>, Tuple<Double, Millisecond>> createUpdateThread() {
		SwingWorker<List<Tuple<Double, Millisecond>>, Tuple<Double, Millisecond>> swingWorker = new SwingWorker<List<Tuple<Double, Millisecond>>, Tuple<Double, Millisecond>>() {

			@Override
			protected List<Tuple<Double, Millisecond>> doInBackground() throws Exception {
				try {

					@SuppressWarnings("resource")
					ServerSocket socket = new ServerSocket(port);
					System.out.println("Server Initialized With Port " + port);
					System.out.println(socket.getLocalPort());
					// check socket for incoming connections and start new
					// processing
					// thread.
					while(true){ 
						Socket connection = socket.accept();
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						
						String receiveBuffer = "Empty Nothing here";
						while ((receiveBuffer = in.readLine()) != null) {
							
							String[] parts = receiveBuffer.split("-");
							Double data =  Double.parseDouble(parts[0]);
							long dateAsLong = Long.parseLong(parts[1]);
							Date date = new Date();
							ms = new Millisecond((int) (ms.getMillisecond() + 40), ms.getSecond());
							
							//System.out.println(ms);
							Tuple<Double, Millisecond> tp = new Tuple<Double, Millisecond>(data, ms);
							publish(tp);
						}
						System.out.println(receiveBuffer);
						
					}

				} catch (NumberFormatException e3) {
					System.out.println("received data that could not be parsed into expected data type.");
				} catch (BindException e1) {
					System.err.println("ERROR BINDING SOCKET. SOCKET PROBABLY IN USE:");
					e1.printStackTrace();
				}catch (IOException e4) { 
					System.out.println("There was an error reading from the socket input stream");
				}
				catch (Exception e2) {

					System.err.println("UNHANDLED EXCEPTION OCCURED:");
					e2.printStackTrace();
				}
				return null;
			}

			@Override
			protected void process(List<Tuple<Double, Millisecond>> chunks) {
				for (Tuple<Double, Millisecond> T1 : chunks) {
					series.add(T1.getRight(), T1.getLeft());
				}
			}
		};
		return swingWorker;
	}

	private Double getNewData(ArrayList<Double> ecgFile) {
		Double reading = ecgFile.remove(0);
		final Millisecond now = new Millisecond();
		return reading;
		// this.series.add(now, reading);
	}

	private ArrayList<Double> readECGFile(String fileName) {
		ArrayList<Double> list = new ArrayList<Double>();
		BufferedReader br;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			try {
				String line;
				while ((line = br.readLine()) != null) {

					list.add(Double.parseDouble(line));
				}
			} catch (IOException e) {
				System.err.println("Error whilst reading from file.");
				e.printStackTrace();
			} finally {
				try {
					br.close();
				} catch (IOException e) {
					System.err.println("error closing file.");
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e) {
			System.err.println("Error Could not locate file: " + fileName);
			e.printStackTrace();
		}
		return list;
	}

}
