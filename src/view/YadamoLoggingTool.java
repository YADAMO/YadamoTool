package view;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import org.jfree.chart.ChartPanel;

import controler.DrawChart;
import controler.SirialCommunicator;


public class YadamoLoggingTool extends JFrame implements ActionListener{
	SirialCommunicator sirial = new SirialCommunicator();
	private boolean tflag = false;
	private int height = 800;
	private int width = 800;

	JPanel pUpper = new JPanel();
	JPanel pUnder = new JPanel();

	private String[] columnNames = {"time", "data1", "data2", "data3","data4","data5","data6","data7","data8","data9"};
	DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
	JTable table = new JTable(tableModel);
	JScrollPane sp = null;

	JMenuBar bar = new JMenuBar();

	JMenu mFile = new JMenu("File");
	JMenu mConnect = new JMenu("Connect");

	JMenuItem iExit = new JMenuItem("Exit");
	ButtonGroup iPort = new ButtonGroup();

	JButton bStart = new JButton("Start");
	JButton bStop = new JButton("Stop");
	JButton bChart = new JButton("Chart");

	JCheckBox[] ckbox = new JCheckBox[9];


	List<String> portList = sirial.getComPort();
	String comPort = null;

	YadamoLoggingTool() {
		setLayout(new FlowLayout());
		add(pUpper);
		pUpper.setPreferredSize(new Dimension(width, 250));
		add(pUnder);
		
		pUpper.add(bStart);
		pUpper.add(bStop);
		pUpper.add(bChart);
		bStart.addActionListener(this);
		bStop.addActionListener(this);
		bChart.addActionListener(this);

		for(int i = 0; i < 9; i++){
			ckbox[i] = new JCheckBox("data" + (i+1));
			pUpper.add(ckbox[i]);
		}
		
		sp = new JScrollPane(table);
		sp.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		pUpper.add(sp);
		sp.setPreferredSize(new Dimension(500, 200));

		for(String port: portList){
			JRadioButtonMenuItem rItem = new JRadioButtonMenuItem(port);
			rItem.addActionListener(this);
			iPort.add(rItem);
			mConnect.add(rItem);
		}

		iExit.addActionListener(this);

		getRootPane().setJMenuBar(bar);
		bar.add(mFile);
		bar.add(mConnect);
		mFile.add(iExit);

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("YadamoTool");
		setSize(width, height);
		setVisible(true);

		String lafClassName = "javax.swing.plaf.metal.MetalLookAndFeel";
		try{
			UIManager.setLookAndFeel(lafClassName);
			SwingUtilities.updateComponentTreeUI(this);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public static void main(String [] args) {
		new YadamoLoggingTool();
	}

	public void setTable(){
		try{
			FileReader rf = new FileReader("tmp.csv");
			BufferedReader br = new BufferedReader(rf);
			String line;
			StringTokenizer token;
			String tabledata[] = new String[15];
			int i = 0;
			while ((line = br.readLine()) != null) {
				token = new StringTokenizer(line, ",");

				while (token.hasMoreTokens()) {
					tabledata[i] = token.nextToken();
					i++;
				}
				i = 0;
				tableModel.addRow(tabledata);
			}
		}catch(IOException e){

		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		String cmdName = e.getActionCommand();
		System.out.println(cmdName);	

		if("Exit".equals(cmdName)){
			System.exit(0);
		}else if("Start".equals(cmdName)){
			if(comPort != null){
				if(sirial.openComport(comPort)){
					sirial.read();
				}
			}
		}else if("Stop".equals(cmdName)){
			sirial.close();
			setTable();
			tflag = true;
		}else if("Chart".equals(cmdName)){
			if(tflag){
				ArrayList<Integer> sctCkbox = new ArrayList<Integer>();
				for(int i = 0; i < 9; i++){
					if(ckbox[i].isSelected()){
						sctCkbox.add(i+1);
					}
				}
				DrawChart chart = new DrawChart(tableModel, sctCkbox);
				chart.createChart();
				ChartPanel cpnl = new ChartPanel(chart.getChart());
				pUnder.add(cpnl);
				
				this.setVisible(true);
		
			}
		}else{
			comPort = e.getActionCommand();
		}
	}
}
