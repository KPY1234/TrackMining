package gui;

import core.Instance;
import core.RadarTrack;
import core.RadarPoint;
import core.cluster.DirectlyGetCluster;
import core.cluster.DBSCAN;
import core.cluster.RecursiveInterSection;
import core.cluster.PixelsDistance;
import core.track.TrackUtilities;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Canvas;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JRadioButton;

import format.FormatTransform;

import java.util.Vector;
import java.util.HashMap;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.JComboBox;
import javax.swing.JList;

public class ClusterVisualizeFrame extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField oneClusterText;
	
	DirectlyGetCluster dgc;
	DBSCAN dbs;
	RecursiveInterSection ris;
	PixelsDistance pd;
	
	ClusterVisualCanvas cvc;
	private JTextField trackNumText;
	private JList list;
	
	int ClusterType=0;
	
	
	/**
	 * Create the frame.
	 */
	
	public ClusterVisualizeFrame(){
		
	}
	
	public ClusterVisualizeFrame(DirectlyGetCluster dgc) {
		
		initialize(dgc);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1680, 1050);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Cluster Number");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("All Cluster Visualize");
		panel.add(btnNewButton_1);
		
		JLabel lblGoTo = new JLabel("Go To Cluster:(>0)");
		panel.add(lblGoTo);
		
		oneClusterText = new JTextField();
		oneClusterText.setText("1");
		panel.add(oneClusterText);
		oneClusterText.setColumns(10);
		
		JButton btnNewButton = new JButton("Visualize");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OneClusterVisualize();
			}
		});
		panel.add(btnNewButton);
		
		JLabel lblTracks = new JLabel("Tracks");
		panel.add(lblTracks);
		
		trackNumText = new JTextField();
		trackNumText.setEditable(false);
		panel.add(trackNumText);
		trackNumText.setColumns(10);
		
		
		cvc = new ClusterVisualCanvas(dgc.getCenterClustersHashMap());
		contentPane.add(cvc, BorderLayout.CENTER);
		
		ClusterType=99;
		
	}
	
	
	public ClusterVisualizeFrame(DBSCAN dbs) {
		
		initialize(dbs);
		
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1680, 1050);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Cluster Number");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("All Cluster Visualize");
		panel.add(btnNewButton_1);
		
		JLabel lblGoTo = new JLabel("Go To Cluster:(>0)");
		panel.add(lblGoTo);
		
		oneClusterText = new JTextField();
		oneClusterText.setText("1");
		panel.add(oneClusterText);
		oneClusterText.setColumns(10);
		
		JButton btnNewButton = new JButton("Visualize");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OneClusterVisualize();
			}
		});
		panel.add(btnNewButton);
		
		JLabel lblTracks = new JLabel("Tracks");
		panel.add(lblTracks);
		
		trackNumText = new JTextField();
		trackNumText.setEditable(false);
		panel.add(trackNumText);
		trackNumText.setColumns(10);
		
		
		
	
		
		cvc = new ClusterVisualCanvas(dbs.getCenterClustersHashMap());
		contentPane.add(cvc, BorderLayout.CENTER);
		
		ClusterType=1;
		
	}
	
	public ClusterVisualizeFrame(RecursiveInterSection ris) {
		
		initialize(ris);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1680, 1050);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Cluster Number");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("All Cluster Visualize");
		panel.add(btnNewButton_1);
		
		JLabel lblGoTo = new JLabel("Go To Cluster:(>0)");
		panel.add(lblGoTo);
		
		oneClusterText = new JTextField();
		oneClusterText.setText("1");
		panel.add(oneClusterText);
		oneClusterText.setColumns(10);
		
		JButton btnNewButton = new JButton("Visualize");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OneClusterVisualize();
			}
		});
		panel.add(btnNewButton);
		
		JLabel lblTracks = new JLabel("Tracks");
		panel.add(lblTracks);
		
		trackNumText = new JTextField();
		trackNumText.setEditable(false);
		panel.add(trackNumText);
		trackNumText.setColumns(10);
		
		cvc = new ClusterVisualCanvas(ris.getCenterClustersHashMap());
		contentPane.add(cvc, BorderLayout.CENTER);
		
		ClusterType=2;
		
	}
	
	public ClusterVisualizeFrame(PixelsDistance pd) {
		
		initialize(pd);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(0, 0, 1680, 1050);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("Cluster Number");
		panel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setEditable(false);
		panel.add(textField);
		textField.setColumns(10);
		
		JButton btnNewButton_1 = new JButton("All Cluster Visualize");
		panel.add(btnNewButton_1);
		
		JLabel lblGoTo = new JLabel("Go To Cluster:(>0)");
		panel.add(lblGoTo);
		
		oneClusterText = new JTextField();
		oneClusterText.setText("1");
		panel.add(oneClusterText);
		oneClusterText.setColumns(10);
		
		JButton btnNewButton = new JButton("Visualize");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				OneClusterVisualize();
			}
		});
		panel.add(btnNewButton);
		
		JLabel lblTracks = new JLabel("Tracks");
		panel.add(lblTracks);
		
		trackNumText = new JTextField();
		trackNumText.setEditable(false);
		panel.add(trackNumText);
		trackNumText.setColumns(10);
		
		cvc = new ClusterVisualCanvas(pd.getCenterClustersHashMap());
		contentPane.add(cvc, BorderLayout.CENTER);
		
		ClusterType=3;
		
	}
	
	
	public void initialize(DirectlyGetCluster dgc){
		
		this.dgc=dgc;
		
		HashMap<Integer, RadarTrack> hm = dgc.getCenterClustersHashMap();
		
//		for(int i=0;i<hm.size();i++)
//			System.out.println(hm.get(i+1));
			
			
		
	
	}
	
	public void initialize(DBSCAN dbs){
		
		this.dbs=dbs;
		
		HashMap<Integer, RadarTrack> hm = dbs.getCenterClustersHashMap();
		
//		for(int i=0;i<hm.size();i++)
//			System.out.println(hm.get(i+1));
		
	}
	
	public void initialize(RecursiveInterSection ris){
		
		this.ris=ris;
	
		HashMap<Integer, RadarTrack> hm = ris.getCenterClustersHashMap();
		
//		for(int i=0;i<hm.size();i++)
//			System.out.println(hm.get(i+1));
			
		
		
	}
	
	public void initialize(PixelsDistance pd){
		
		this.pd=pd;
	
		HashMap<Integer, RadarTrack> hm = pd.getCenterClustersHashMap();
		
//		for(int i=0;i<hm.size();i++)
//			System.out.println(hm.get(i+1));
			
		
		
	}
	
	public void OneClusterVisualize(){
		
		if(ClusterType==99){
			
			int clusterNum = Integer.parseInt(oneClusterText.getText().trim());
			
			HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
			
			Vector<RadarTrack> oneCluster = dgc.getAllCluster().get(clusterNum-1);
			
			for(int i=0;i<oneCluster.size();i++){
				hm.put(i+1, oneCluster.elementAt(i));
			}
			
			cvc.initialize(hm);
			
			Graphics g = cvc.getGraphics();
			
			g.clearRect(0, 0, 1700, 1100);
			//g.drawLine(0,0,100,100);
			
			cvc.update(g);
			
			trackNumText.setText(Integer.toString(oneCluster.size()));
			
			
		}
		
		if(ClusterType==1){
			
			int clusterNum = Integer.parseInt(oneClusterText.getText().trim());
			
			Vector<Instance> oriInstance = dbs.getOriInstances();
			Vector<Instance> oneCluster = dbs.getOneCluster(clusterNum-1);
			
			HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
			
			String[] trackInstStr = new String[oneCluster.size()];
			
			
			for(int i=0;i<oneCluster.size();i++){
				
				Instance inst = oriInstance.elementAt(oneCluster.elementAt(i).getID()-1);
				trackInstStr[i]="trackNo"+inst.getTrackNum()+" "+inst.toString();
				hm.put(i+1,FormatTransform.PointsVectorToRadarTrack(inst));
				
			}
			
			System.out.println("Cluster:"+clusterNum);
			
			for(int i=0;i<trackInstStr.length;i++)
				System.out.println(trackInstStr[i]);
			
			
				
			cvc.initialize(hm);
			
			
			Graphics g = cvc.getGraphics();
			
			g.clearRect(0, 0, 1700, 1100);
			//g.drawLine(0,0,100,100);
			
			cvc.update(g);
			
			trackNumText.setText(Integer.toString(oneCluster.size()));
			
			
		}
		else if(ClusterType==2){
			
			int clusterNum = Integer.parseInt(oneClusterText.getText().trim());
			
			HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
			
			Vector<RadarTrack> oneCluster = ris.getAllCluster().get(clusterNum-1);
			
			for(int i=0;i<oneCluster.size();i++){
				hm.put(i+1, oneCluster.elementAt(i));
			}
			
			cvc.initialize(hm);
			
			Graphics g = cvc.getGraphics();
			
			g.clearRect(0, 0, 1700, 1100);
			//g.drawLine(0,0,100,100);
			
			cvc.update(g);
			
			trackNumText.setText(Integer.toString(oneCluster.size()));
			
		}
		
		else if(ClusterType==3){
			
			int clusterNum = Integer.parseInt(oneClusterText.getText().trim());
			
			HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
			
			Vector<RadarTrack> oneCluster = pd.getAllCluster().get(clusterNum-1);
			
			for(int i=0;i<oneCluster.size();i++){
				hm.put(i+1, oneCluster.elementAt(i));
			}
			
			cvc.initialize(hm);
			
			Graphics g = cvc.getGraphics();
			
			g.clearRect(0, 0, 1700, 1100);
			//g.drawLine(0,0,100,100);
			
			cvc.update(g);
			
			trackNumText.setText(Integer.toString(oneCluster.size()));
			
		}
		
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		double oriLon=114.78;
		double oriLat=17.69;
		double maxLon=128.35;
		double maxLat=31;
	
		int grids=1024;
		
		HashMap<Integer, RadarTrack> hm = new HashMap<Integer, RadarTrack>();
		
		RadarTrack rt1 = new RadarTrack();
		
		RadarPoint rp = new RadarPoint();
		rp.setHeight(200);
		rp.setLon(120.5);
		rp.setLat(20.3);
		
		rt1.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(200);
		rp.setLon(121.3);
		rp.setLat(21.9);
		
		rt1.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(122.7);
		rp.setLat(23.0);
		
		rt1.add(rp);
		
		hm.put(1, rt1);
		
		RadarTrack rt2 = new RadarTrack();
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(118.1);
		rp.setLat(18.3);
		
		rt2.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(118.5);
		rp.setLat(19.2);
		
		rt2.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(119.48);
		rp.setLat(22.0);
		
		rt2.add(rp);
		
		hm.put(2, rt2);
		
		RadarTrack rt3 = new RadarTrack();
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(118.2);
		rp.setLat(18.4);
		
		rt3.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(118.6);
		rp.setLat(19.3);
		
		rt3.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(119.49);
		rp.setLat(22.1);
		
		rt3.add(rp);
		
		hm.put(3, rt3);
		
		RadarTrack rt4 = new RadarTrack();
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(119.2);
		rp.setLat(19.4);
		
		rt4.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(119.6);
		rp.setLat(18.3);
		
		rt4.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(117.49);
		rp.setLat(21.1);
		
		rt4.add(rp);
		
		hm.put(4, rt4);
		
		
		
//		Instance[] inst = new Instance[10];
//		Vector<Instance> insts = new Vector<Instance>();
//		
//		for(int i=0;i<inst.length;i++){
//			
//			inst[i] = new Instance();
//			inst[i].setID(i+1);
//			inst[i].add(120.+i);
//			inst[i].add(20.+i*i-2*i);
//			inst[i].add(360.);
//			insts.add(inst[i]);
//		}
//		
//		//inst[0].set(0, 50);
//		//inst[0].add(20);
//		
//		inst[8].set(1, 40.0);
//		inst[9].set(1, 45.0);
//		
//		//System.out.println(insts.elementAt(0).elementAt(0));
//		System.out.println("Instances:"+insts);

		final DBSCAN dbs = new DBSCAN(TrackUtilities.getInstances(hm), 0.1, 1);
		
		final RecursiveInterSection ris = new RecursiveInterSection(oriLon,oriLat,maxLon,maxLat,grids,hm);
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//ClusterVisualizeFrame frame = new ClusterVisualizeFrame(dbs);
					ClusterVisualizeFrame frame = new ClusterVisualizeFrame(ris);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

}



class ClusterVisualCanvas extends Canvas{
	
	int oriXPixel = 30;
	int oriYPixel = 920;
	int maxXPixel = 1660;
	int maxYPixel = 0;
	
	Vector<double[]> allTrackLon;
	Vector<double[]> allTrackLat;
	
	
	
	double maxLon=Double.MIN_VALUE;
	double minLon=Double.MAX_VALUE;
	double maxLat=Double.MIN_VALUE;
	double minLat=Double.MAX_VALUE;
	
	int splitSize=10;
	
	double[] lonSplit = new double[splitSize];
	double[] latSplit = new double[splitSize];
	
	int[] xPixelSplit = new int[splitSize];
	int[] yPixelSplit = new int[splitSize];
	
	public ClusterVisualCanvas(HashMap<Integer,RadarTrack> trackMap){
		initialize(trackMap);
	}
	
	
	
	public void initialize(HashMap<Integer,RadarTrack> trackMap){
		
		allTrackLon = new Vector<double[]>();
		allTrackLat = new Vector<double[]>();
		
		
		Object[] keys = trackMap.keySet().toArray();
		
		
		double[] lonOfTrack;
		double[] latOfTrack;
		
		
		
		for(int i=0;i<trackMap.size();i++){
			
			int trackSize = trackMap.get(keys[i]).size();
			
			lonOfTrack = new double[trackSize];
			latOfTrack = new double[trackSize];
			
			for(int j=0;j<trackSize;j++){
				
				lonOfTrack[j] = trackMap.get(keys[i]).elementAt(j).getLon();
				latOfTrack[j] = trackMap.get(keys[i]).elementAt(j).getLat();
			
				//System.out.println(trackMap.get(keys[i]).elementAt(j));
				
				//System.out.println(lonOfTrack[j]);
				//System.out.println(latOfTrack[j]);
				
			}
			allTrackLon.add(lonOfTrack);
			allTrackLat.add(latOfTrack);
			
		}
		
		maxLon=getMaxValue(allTrackLon);
		minLon=getMinValue(allTrackLon);
		maxLat=getMaxValue(allTrackLat);
		minLat=getMinValue(allTrackLat);
		
		maxLon=128.35;
		minLon=113.64;
		maxLat=31;
		minLat=16;
		
		
		double lonDiff = maxLon - minLon;
		double latDiff = maxLat - minLat;
		
		for(int i=0;i<splitSize;i++){
			lonSplit[i] = minLon + i*(lonDiff/splitSize);
			latSplit[i] = minLat + i*(latDiff/splitSize);
			
			lonSplit[i] = getCarry(lonSplit[i],2);
			latSplit[i] = getCarry(latSplit[i],2);
			
//			System.out.println("lonSplit"+i+":"+lonSplit[i]);
//			System.out.println("latSplit"+i+":"+latSplit[i]);
			
		}
		
		
		for(int i=0;i<splitSize;i++){
			
			xPixelSplit[i]=getXPixel(lonSplit[i]);
			yPixelSplit[i]=getYPixel(latSplit[i]);
//			System.out.println("xPixelSplit"+i+":"+xPixelSplit[i]);
//			System.out.println("xPixelSplit"+i+":"+xPixelSplit[i]);
			
		}
		
		
		
//		System.out.println("maxLon="+maxLon);
//		System.out.println("minLon="+minLon);
//		System.out.println("maxLat="+maxLat);
//		System.out.println("minLat="+minLat);
//		
//		System.out.println(getXPixel(maxLon));
//		System.out.println(getYPixel(maxLat));
		
		
		//System.out.println("max="+getMaxValue(allTrackLon));
		//System.out.println("min="+getMinValue(allTrackLon));
		
		
		
	}
	
	public double getMaxValue(Vector<double[]> allTrackValues){
		
		double max = Double.MIN_VALUE;
		
		for(int i=0;i<allTrackValues.size();i++){
			
			for(int j=0;j<allTrackValues.elementAt(i).length;j++){
				
				if(allTrackValues.elementAt(i)[j]>max)
					max=allTrackValues.elementAt(i)[j];
				
				//System.out.println(allTrackValues.elementAt(i)[j]);
			}
		}
		//System.out.println("max="+max);
		return max;
	}
	
	public double getMinValue(Vector<double[]> allTrackValues){
		
		double min = Double.MAX_VALUE;
		
		for(int i=0;i<allTrackValues.size();i++){
			
			for(int j=0;j<allTrackValues.elementAt(i).length;j++){
				
				if(allTrackValues.elementAt(i)[j]<min)
					min=allTrackValues.elementAt(i)[j];
				
				//System.out.println(allTrackValues.elementAt(i)[j]);
			}
		}
		//System.out.println("min="+min);
		return min;
	}
	
	public int getXPixel(double lon){
		
		int xPixel=0;
		
		int width=Math.abs(maxXPixel-oriXPixel)-20;
	
		xPixel=(int)Math.floor(oriXPixel+(lon-minLon)*width/(maxLon-minLon));
		
		//System.out.println("width="+width);
	    
		return xPixel;
		
	}
	
	public int getYPixel(double lat){
		
		int yPixel=0;
		
		int height=Math.abs(maxYPixel-oriYPixel)-40;
	
		yPixel=(int)Math.floor(oriYPixel-(lat-minLat)*height/(maxLat-minLat));
		
		//System.out.println("height="+height);
	    
		
		return yPixel;
		
	}
	
	public int[] getXPixelTrack(double[] lon){
		
		int[] xPixelTrack = new int[lon.length];
		
		for(int i=0;i<lon.length;i++){
			xPixelTrack[i]=getXPixel(lon[i]);
			//System.out.println("xTrack="+xPixelTrack[i]);
			
		}
			
		
		return xPixelTrack;
	}
	
	public int[] getYPixelTrack(double[] lat){
		
		int[] yPixelTrack = new int[lat.length];
		
		for(int i=0;i<lat.length;i++)
			yPixelTrack[i]=getYPixel(lat[i]);
		
		return yPixelTrack;
		
	}
	
	public double getCarry(double number, int digital){
		
		boolean isCarray=false;
		int determineBit;
		
		determineBit=(int)(number*Math.pow(10, digital+1))%10;
		
		if(determineBit>=5)
			isCarray=true;
			
		if(isCarray){
			
			number=number*Math.pow(10, digital);
			number=Math.ceil(number);
			number=number/Math.pow(10, digital);
			
		}
		else{
			
			number=number*Math.pow(10, digital);
			number=Math.floor(number);
			number=number/Math.pow(10, digital);
			
		}
		
		
		//System.out.println(determineBit);
		
		
		return number;
	}
	
	public void paint(Graphics g){
		
		int cor_r=0;
		int cor_g=0;
		int cor_b=0;
		
		g.setColor(new Color(cor_r,cor_g,cor_b));
		
		g.drawLine(oriXPixel, oriYPixel, oriXPixel, maxYPixel);
		g.drawLine(oriXPixel, oriYPixel, maxXPixel, oriYPixel);
		
		for(int i=0;i<xPixelSplit.length;i++){
			
			g.drawLine(xPixelSplit[i], oriYPixel+3, xPixelSplit[i], oriYPixel-3);
			g.drawString(Double.toString(lonSplit[i]), xPixelSplit[i]-20, oriYPixel+20);
		}
		
		for(int i=0;i<yPixelSplit.length;i++){
			
			g.drawLine(oriXPixel-3, yPixelSplit[i], oriXPixel+3, yPixelSplit[i]);
			g.drawString(Double.toString(latSplit[i]), oriXPixel-30, yPixelSplit[i]+5);
		}
		
		
		for(int i=0;i<allTrackLon.size();i++){
			
			int[] xPixelTrack = getXPixelTrack(allTrackLon.elementAt(i));
			int[] yPixelTrack = getYPixelTrack(allTrackLat.elementAt(i));
			
			int direction=1;
			
			if(xPixelTrack.length>1){
				if(xPixelTrack[1]-xPixelTrack[0]>=0)
					direction=1;
				else
					direction=0;
			}
				
			g.setColor(new Color(i*120%256,i*20%256,i*40%256));
			
			fillTriangle(xPixelTrack[0], yPixelTrack[0], direction, g);
			
			g.drawPolyline(xPixelTrack, yPixelTrack, xPixelTrack.length);
			
			g.fillOval(xPixelTrack[xPixelTrack.length-1]-5, yPixelTrack[yPixelTrack.length-1]-5, 10, 10);
			
		}
		
		//g.clearRect(0, 0, 1780, 1100);
		
		//System.out.println(g.getColor()); 
		
	}
	
	public void update(Graphics g){
		
		int cor_r=0;
		int cor_g=0;
		int cor_b=0;
		
		g.setColor(new Color(cor_r,cor_g,cor_b));
		
		g.drawLine(oriXPixel, oriYPixel, oriXPixel, maxYPixel);
		g.drawLine(oriXPixel, oriYPixel, maxXPixel, oriYPixel);
		
		for(int i=0;i<xPixelSplit.length;i++){
			
			g.drawLine(xPixelSplit[i], oriYPixel+3, xPixelSplit[i], oriYPixel-3);
			g.drawString(Double.toString(lonSplit[i]), xPixelSplit[i]-20, oriYPixel+20);
		}
		
		for(int i=0;i<yPixelSplit.length;i++){
			
			g.drawLine(oriXPixel-3, yPixelSplit[i], oriXPixel+3, yPixelSplit[i]);
			g.drawString(Double.toString(latSplit[i]), oriXPixel-30, yPixelSplit[i]+5);
		}
		
		
		for(int i=0;i<allTrackLon.size();i++){
			
			int[] xPixelTrack = getXPixelTrack(allTrackLon.elementAt(i));
			int[] yPixelTrack = getYPixelTrack(allTrackLat.elementAt(i));
			
			int direction=1;
			
			if(xPixelTrack.length>1){
				if(xPixelTrack[1]-xPixelTrack[0]>=0)
					direction=1;
				else
					direction=0;
			}
				
			g.setColor(new Color(i*120%256,i*20%256,i*40%256));
			
			fillTriangle(xPixelTrack[0], yPixelTrack[0], direction, g);
			
			g.drawPolyline(xPixelTrack, yPixelTrack, xPixelTrack.length);
			
			g.fillOval(xPixelTrack[xPixelTrack.length-1]-5, yPixelTrack[yPixelTrack.length-1]-5, 10, 10);
			
		}
		
		
		
	
	}
	
	public void fillTriangle(int x, int y, int direction, Graphics g){
		
		int size=5;
		
		int[] xPixels = new int[3];
		int[] yPixels = new int[3];
		
		if(direction==1){
			
			xPixels[0]=x+size;
			yPixels[0]=y;
			
			xPixels[1]=x-size;
			yPixels[1]=y-size;
			
			xPixels[2]=x-size;
			yPixels[2]=y+size;
		}
		else{
			
			xPixels[0]=x-size;
			yPixels[0]=y;
			
			xPixels[1]=x+size;
			yPixels[1]=y-size;
			
			xPixels[2]=x+size;
			yPixels[2]=y+size;
			
		}
		
		g.fillPolygon(xPixels, yPixels, 3);
		
	}
}

