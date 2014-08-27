package controler;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class Test extends JFrame{
	  public static void main(String[] args) {
	    Test frame = new Test();

	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    frame.setBounds(10, 10, 400, 300);
	    frame.setTitle("グラフサンプル");
	    frame.setVisible(true);
	  }

	  Test(){
	    DefaultPieDataset data = new DefaultPieDataset();
	    data.setValue("支持する", 40);
	    data.setValue("支持しない", 55);
	    data.setValue("未回答", 5);

	    JFreeChart chart = 
	      ChartFactory.createPieChart("支持率", data, true, false, false);

	    ChartPanel cpanel = new ChartPanel(chart);
	    getContentPane().add(cpanel, BorderLayout.CENTER);
	  }
	}