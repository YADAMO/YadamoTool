package controler;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.TickUnit;
import org.jfree.chart.axis.TickUnits;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RectangleInsets;

public class DrawChart {
	private XYDataset dataset;
	private JFreeChart chart;
	private int maxtime;
	
	private int creatDataset(DefaultTableModel tableModel, ArrayList<Integer> list){
		ArrayList<XYSeries> xyList = new ArrayList<XYSeries>();
		for(Integer num:list){
			xyList.add(new XYSeries("data" + num));
		}
		int i = 0;
		int time = 0;
		for(Integer colm:list){
			for(int row = 0; row < tableModel.getRowCount()-1; row++){
				time = Integer.parseInt((String) tableModel.getValueAt(row, 0));
				xyList.get(i).add((double)time,(double)Integer.parseInt((String)tableModel.getValueAt(row, colm)));
			}
			i++;
		}

		XYSeriesCollection xySeriesCollection = new XYSeriesCollection();
		for(XYSeries data:xyList){
			xySeriesCollection.addSeries(data);
		}
		dataset = xySeriesCollection;
		return Integer.parseInt((String)tableModel.getValueAt(tableModel.getRowCount()-1, 0));
	}
	public DrawChart(DefaultTableModel tableModel, ArrayList<Integer> list){
		maxtime = creatDataset(tableModel, list);
		System.out.println(maxtime);
		chart = ChartFactory.createXYLineChart(" ", "time", " ", dataset, PlotOrientation.VERTICAL, true, false, false);
		customizeChart();
	}
	public void createChart(){
		GraphicsEnvironment ev = GraphicsEnvironment.getLocalGraphicsEnvironment();
		DisplayMode displayMode = ev.getDefaultScreenDevice().getDisplayMode();
		
		try {
			ChartUtilities.saveChartAsPNG(new File("tmp.png"), chart, displayMode.getWidth(), 500);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void customizeChart(){
		// グラフ全体の背景色の設定
        Paint bgPaint = chart.getBackgroundPaint();
        chart.setBackgroundPaint(Color.WHITE);
        // グラフ全体の境界線の設定
        chart.setBorderVisible( true);
        chart.setBorderPaint( Color.BLACK);
        chart.setBorderStroke( new BasicStroke(5));

        // 描画領域の設定
        XYPlot plot = (XYPlot)chart.getPlot();
        plot.setBackgroundPaint(Color.lightGray);
        plot.setRangeGridlinePaint(Color.BLUE);
        plot.setDomainGridlinesVisible(true);
        
        // 横軸の設定
        NumberAxis axis = (NumberAxis)plot.getDomainAxis();
        axis.setLowerMargin(0.03);
        axis.setUpperMargin(0.03);
        TickUnits tickUnits = new TickUnits();
        TickUnit unit = new NumberTickUnit((maxtime/10));
        tickUnits.add(unit);
        axis.setStandardTickUnits(tickUnits);
        
        // 縦軸の設定
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();        
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        
	}
	
	public JFreeChart getChart(){
		return chart;
	}
}
