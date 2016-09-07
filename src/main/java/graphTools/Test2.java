package graphTools;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

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

/**
 * Test2 class used to Test2 possible solutions for the live rendering of ECG
 * data.
 * 
 * @author Paul
 *
 */
public class Test2 extends ApplicationFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final TimeSeriesCollection dataset;
	private int DATASIZE = 10;
	final TimeSeries series = new TimeSeries("Paul", Millisecond.class);;
	private final static String FILENAME = "C:\\Users\\Paul\\Desktop\\ecgTest.dat";

	public Test2(final String title) {

		super(title);

		dataset = new TimeSeriesCollection(this.series);
		final JFreeChart chart = createChart(dataset);
		final ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
		setContentPane(chartPanel);

	}

	/**
	 * creates an initial data set from file used in Test2ing may not be needed
	 * for network version unless use found for reading from file.
	 * 
	 * @return a sample dataset.
	 */
/*	private XYDataset createDataset(String fileName) {
		BufferedReader br;
		double i = 1.0;
		try {
			br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName)));
			try {
				String line;
				while ((line = br.readLine()) != null && i < DATASIZE) {
					series.add(i, Double.parseDouble(line));
					i++;
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

		final XYSeriesCollection dataset = new XYSeriesCollection();
		dataset.addSeries(series);
		return dataset;

	}*/

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
		final JFreeChart chart = ChartFactory.createTimeSeriesChart(
				"ECG", // chart
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
		range.setRange(1.6, 2.5);
		range.setTickUnit(new NumberTickUnit(0.1));
		ValueAxis timeaxis =  plot.getDomainAxis();
		timeaxis.setAutoRange(true);
		timeaxis.setFixedAutoRange(1000.0);
		return chart;

	}

	/**
	 * launches Test2 graph
	 *
	 * @param args
	 *            ignored.
	 * @throws InterruptedException
	 */
	public static void main(final String[] args) throws InterruptedException {

		final Test2 Test2 = new Test2("Electrocardiogram");
		Test2.pack();
		RefineryUtilities.centerFrameOnScreen(Test2);
		Test2.setVisible(true);
		ArrayList<Double> ecgFile = Test2.readECGFile(FILENAME);
		for (int i = 0; i < 300; i++) {
			Thread.sleep(50);
			if (ecgFile.size() > 0) {
				Test2.getNewData(ecgFile);
			}
		}
	}

	private void getNewData(ArrayList<Double> ecgFile) {
		//series.remove(0);
		
		Double reading = ecgFile.remove(0);
		final Millisecond now = new Millisecond();
		this.series.add(now, reading);
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
