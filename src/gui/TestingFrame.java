package gui;

import core.RadarPoint;
import core.RadarTrack;
import core.TrackPassMatrix;
import core.cluster.DirectlyGetCluster;
import core.track.TrackUtilities;

import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

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
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class TestingFrame extends JFrame {
	
	int oriXPixel = 30;
	int oriYPixel = 920;
	int maxXPixel = 1650;
	int maxYPixel = 0;
	
	double maxLon=128.35;
	double minLon=113.64;
	double maxLat=31;
	double minLat=16;
	

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JButton btnClusterChooser;
	private JButton btnTestingDataChooser;
	private JButton btnStart;
	
	
	HashMap<Integer,RadarTrack> centerClustersHM = null;
	TrackPassMatrix[] centerClustersTPM = null;
	
	HashMap<Integer,RadarTrack> testingTracksHM;
	
	TestingVisualCanvas tvc;

	/**
	 * Create the frame.
	 */
	public TestingFrame() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1680, 1050);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		
		btnClusterChooser = new JButton("Cluster Chooser");
		btnClusterChooser.setEnabled(false);
		btnClusterChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseClusterFile();
			}
		});
		panel.add(btnClusterChooser);
		
		JLabel lblClusterNum = new JLabel("Cluster Num:");
		panel.add(lblClusterNum);
		
		textField = new JTextField();
		textField.setEditable(false);
		panel.add(textField);
		textField.setColumns(10);
		
		btnTestingDataChooser = new JButton("Testing Data Chooser");
		btnTestingDataChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				chooseTestingData();
			}
		});
		panel.add(btnTestingDataChooser);
		
		JLabel lblSimulateData = new JLabel("Simulate Data:");
		panel.add(lblSimulateData);
		
		textField_1 = new JTextField();
		textField_1.setEditable(false);
		panel.add(textField_1);
		textField_1.setColumns(10);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				start();
			}
		});
		panel.add(btnStart);
		
		tvc = new TestingVisualCanvas();
		contentPane.add(tvc, BorderLayout.CENTER);
		
	}
	
	
	public void chooseClusterFile(){
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File","txt","csv");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    
	    String fileName = null;
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	     
	    	fileName = chooser.getSelectedFile().getPath();
	    }
	    
	   // System.out.println(fileName);
	    
	    if(fileName!=null){
	    	
	    	double oriLon=114.78;
			double oriLat=17.69;
			double maxLon=128.35;
			double maxLat=31;
		
			int grids=256;
	    	
	    	DirectlyGetCluster dgc = new DirectlyGetCluster(fileName);
	    	centerClustersHM = dgc.getCenterClustersHashMap();
	    	centerClustersTPM = new TrackPassMatrix[centerClustersHM.size()];
	    	
	    	textField.setText(Integer.toString(centerClustersHM.size()));
	    	
	    	//System.out.println(centerClustersHM);
	    	
	    	Object[] key = centerClustersHM.keySet().toArray();
	    	
	    	int[] intKey = new int[key.length];
	    	
	    	
	    	for(int i=0;i<intKey.length;i++)
	    		intKey[i] = (int)key[i];
	    	
	    	bubbleSort(intKey);
	    	
	    	System.out.println("Transform clusters into track passed matrix...");
	    	
	    	Date da = new Date();
			long startTime=da.getTime();
	    	
	    	for(int i=0;i<centerClustersTPM.length;i++){
	    		
	    		centerClustersTPM[i] = 
	    				TrackUtilities.getDirTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, centerClustersHM.get(intKey[i]));
	    		//System.out.println("i="+i+"intKey["+i+"]="+intKey[i]);
	    		//System.out.println(centerClustersTPM[i]);
	    		
	    	}
	    	
	    	Object[] testTracksKey =  testingTracksHM.keySet().toArray();
	    	
	    	da = new Date();
			long endTime=da.getTime();
			long diffTime=endTime-startTime;
			double diffSeconds=diffTime/1000.0;
			
			System.out.println("Generating track passed matrix from clusters elapse time:"+diffSeconds+"(s)");
	    	
	    }
		
		
	}
	
	public void chooseTestingData(){
		
		JFileChooser chooser = new JFileChooser();
	    FileNameExtensionFilter filter = new FileNameExtensionFilter("Text File","txt","csv");
	    chooser.setFileFilter(filter);
	    int returnVal = chooser.showOpenDialog(this);
	    
	    String fileName = null;
	    
	    if(returnVal == JFileChooser.APPROVE_OPTION) {
	     
	    	fileName = chooser.getSelectedFile().getPath();
	    }
	    
	   // System.out.println(fileName);
	    
	    if(fileName!=null){
	    	
	    	btnClusterChooser.setEnabled(true);
	    	
	    	Date da = new Date();
			long startTime=da.getTime();
			
			HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
			
			Vector<Integer> groupVector = new Vector<Integer>();
			
			int count=0;
			
			try{
				BufferedReader br = new BufferedReader(new FileReader(fileName));
				
				String str = br.readLine();
				StringTokenizer st = new StringTokenizer(str);
				
				while(str!=null){
					
					RadarPoint rp = new RadarPoint(str);
					
					if(!groupVector.contains((int)rp.getTrackNo())){
						
						//System.out.println("trackNo:"+rp.getTrackNo());
						groupVector.add(rp.getTrackNo());
					}
						
					
					if(!hm.containsKey(rp.getTrackNo())){
						RadarTrack rt = new RadarTrack();
						rt.setTrackNo(rp.getTrackNo());
						//System.out.println("trackNo:"+rt.getTrackNo());
						rt.add(rp);
						hm.put(rp.getTrackNo(), rt);
					}
					else{
						
						RadarTrack rt = hm.get(rp.getTrackNo());
						rt.add(rp);
						hm.put(rp.getTrackNo(), rt);
						
					}
						
					
					str = br.readLine();
					
//					if(count%100000==0)
//						System.out.println("count:"+count);
					
					count++;
					
//					if(count==2000){
//						System.out.println("break");
//						break;
//					}
					
				}
				br.close();
				
			}catch(IOException ioex){
				
			}
			
			//System.out.println("track number:"+groupVector.size());
			
			textField_1.setText(Integer.toString(hm.size()));
			
			//System.out.println("count:"+count);
			
			//System.out.println(hm);
			
			testingTracksHM=hm;
			
			
			da = new Date();
			long endTime=da.getTime();
			long diffTime=endTime-startTime;
			double diffSeconds=diffTime/1000.0;
			
			System.out.println("Generating track from points text elapse time:"+diffSeconds+"(s)");
	    	
	    }
		
		
	}
	
	public void start(){
		
		HashMap<Integer,RadarTrack> currentTracksHM = new HashMap<Integer,RadarTrack>();
		
		Vector<HashMap<Integer, HashMap<String, Integer>>> oldProperties = null; //<clusterID, properties, value>
		oldProperties = new Vector<HashMap<Integer, HashMap<String, Integer>>>();
		
		Object[] keySet = testingTracksHM.keySet().toArray();
		
		TrackPassMatrix[] oldTPM1 = new TrackPassMatrix[keySet.length];
		
		//System.out.println("key set length:"+oldTPM1.length);
		
		for(int i=0;i<keySet.length;i++){
			RadarTrack rt = new RadarTrack();
			rt.setTrackNo((int)keySet[i]);
			currentTracksHM.put((int)keySet[i], rt);
			oldProperties.add(new HashMap<Integer, HashMap<String, Integer>>());
			oldTPM1[i] = new TrackPassMatrix();
		}
			
		int splitSize=10;
		
		double[] lonSplit = new double[splitSize];
		double[] latSplit = new double[splitSize];
		
		int[] xPixelSplit = new int[splitSize];
		int[] yPixelSplit = new int[splitSize];
		
		double lonDiff = maxLon - minLon;
		double latDiff = maxLat - minLat;
		
		for(int i=0;i<splitSize;i++){
			lonSplit[i] = minLon + i*(lonDiff/splitSize);
			latSplit[i] = minLat + i*(latDiff/splitSize);
			
			lonSplit[i] = getCarry(lonSplit[i],2);
			latSplit[i] = getCarry(latSplit[i],2);
		
		
		}
		
		for(int i=0;i<splitSize;i++){
			
			xPixelSplit[i]=getXPixel(lonSplit[i]);
			yPixelSplit[i]=getYPixel(latSplit[i]);
//			System.out.println("xPixelSplit"+i+":"+xPixelSplit[i]);
//			System.out.println("xPixelSplit"+i+":"+xPixelSplit[i]);
			
		}
		
		
		Object[] key = testingTracksHM.keySet().toArray();
		
		int trackLength = testingTracksHM.get(key[0]).size();
		
		System.out.println("Tracks Length:"+trackLength);
		
		for(int i=0;i<trackLength;i++){
			
			CalculateSimilarThread[] cst = new CalculateSimilarThread[key.length];
			
			for(int j=0;j<key.length;j++){
				
				RadarTrack temRadarTrack = currentTracksHM.get(key[j]);
				temRadarTrack.add(testingTracksHM.get(key[j]).get(i));
				
				//System.out.println("key"+key[j]+" "+temRadarTrack);
				
				currentTracksHM.put((int)key[j], temRadarTrack);
				
				cst[j] = new CalculateSimilarThread(i+1, tvc, oldProperties.elementAt(j), oldTPM1[j], temRadarTrack, centerClustersTPM, centerClustersHM);
				cst[j].start();
				
//				try{
//					cst[j].join();
//				}catch(InterruptedException ie){
//					ie.printStackTrace();
//				}
				
			}
			
			//System.out.println(currentTracksHM);
			
			//System.out.println("Main Thread Execute");
			
			
			
			Graphics g = tvc.getGraphics();
			
			g.clearRect(0, 0, 1700, 1100);
			
			int cor_r=0;
			int cor_g=0;
			int cor_b=0;
			
			g.setColor(new Color(cor_r,cor_g,cor_b));
			
			g.drawLine(oriXPixel, oriYPixel, oriXPixel, maxYPixel);
			g.drawLine(oriXPixel, oriYPixel, maxXPixel, oriYPixel);
			
			for(int j=0;j<xPixelSplit.length;j++){
				
				g.drawLine(xPixelSplit[j], oriYPixel+3, xPixelSplit[j], oriYPixel-3);
				g.drawString(Double.toString(lonSplit[j]), xPixelSplit[j]-20, oriYPixel+20);
			}
			
			for(int j=0;j<yPixelSplit.length;j++){
				
				g.drawLine(oriXPixel-3, yPixelSplit[j], oriXPixel+3, yPixelSplit[j]);
				g.drawString(Double.toString(latSplit[j]), oriXPixel-30, yPixelSplit[j]+5);
			}
			
			
			drawTracks(g, currentTracksHM);
			
			//g.drawLine(20+i,100+i,50+i,600+i);
			
			tvc.update(g);
			
			try{
				Thread.sleep(3000);
			}catch(InterruptedException ie){
				ie.printStackTrace();
			}
			
		}
		
	}
	
	public void drawTracks(Graphics g, HashMap<Integer, RadarTrack> trackMap){
		
		Vector<double[]> allTrackLon = new Vector<double[]>();
		Vector<double[]> allTrackLat = new Vector<double[]>();
		
		
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
	
	public void bubbleSort(int[] array){
		
		for(int i=0;i<array.length-1;i++){
			for(int j=0;j<array.length-1;j++){
				if(array[j]>array[j+1]){
					int tem=array[j+1];
					array[j+1]=array[j];
					array[j]=tem;
				}
			}
		}
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TestingFrame frame = new TestingFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	

}

class TestingVisualCanvas extends Canvas{
	
	int oriXPixel = 30;
	int oriYPixel = 920;
	int maxXPixel = 1650;
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
	
	public TestingVisualCanvas(){
		initialize();
	}
	
	
	
	public void initialize(){
		
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
		
		
		}
		
		for(int i=0;i<splitSize;i++){
			
			xPixelSplit[i]=getXPixel(lonSplit[i]);
			yPixelSplit[i]=getYPixel(latSplit[i]);
//			System.out.println("xPixelSplit"+i+":"+xPixelSplit[i]);
//			System.out.println("xPixelSplit"+i+":"+xPixelSplit[i]);
			
		}
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
		
		//GUITasks.drawDashedLine(g, 50,100,150,300,1,1);
		
		
		//int[] XPixel = {100,200,300};
		//int[] YPixel = {300,400,500};
		
		//GUITasks.drawDashedPolyLine(g, XPixel,YPixel,3,2,2);
		
		
//		for(int i=0;i<allTrackLon.size();i++){
//			
//			int[] xPixelTrack = getXPixelTrack(allTrackLon.elementAt(i));
//			int[] yPixelTrack = getYPixelTrack(allTrackLat.elementAt(i));
//			
//			int direction=1;
//			
//			if(xPixelTrack.length>1){
//				if(xPixelTrack[1]-xPixelTrack[0]>=0)
//					direction=1;
//				else
//					direction=0;
//			}
//				
//			g.setColor(new Color(i*120%256,i*20%256,i*40%256));
//			
//			fillTriangle(xPixelTrack[0], yPixelTrack[0], direction, g);
//			
//			g.drawPolyline(xPixelTrack, yPixelTrack, xPixelTrack.length);
//			
//			g.fillOval(xPixelTrack[xPixelTrack.length-1]-5, yPixelTrack[yPixelTrack.length-1]-5, 10, 10);
//			
//		}
		
		//g.clearRect(0, 0, 1780, 1100);
		
		//System.out.println(g.getColor()); 
		
	}
	
	public void update(Graphics g){
	
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

class CalculateSimilarThread extends Thread{
	
	private HashMap<Integer, HashMap<String, Integer>> oldProperties;
	
	int oriXPixel = 30;
	int oriYPixel = 920;
	int maxXPixel = 1650;
	int maxYPixel = 0;
	
	double maxLon=128.35;
	double minLon=113.64;	//canvas start longitude
	double maxLat=31;
	double minLat=16;		//canvas start latitude
	
	double oriLon=114.78;	//transform into passed matrix start longitude
	double oriLat=17.69;	//transform into passed matrix start latitude
	

	int grids = 256;
	
	int pointNum=0;
	
	RadarTrack track, delta_track;
	
	TrackPassMatrix[] centerTracksTPM;
	TrackPassMatrix oldTPM1, delta_track_TPM;
	HashMap<Integer,RadarTrack> centerClustersHM;
	double maxScore = Double.MIN_VALUE;
	
	int MaxSimClusterNum=0;
	
	TestingVisualCanvas tvc;
	
	public CalculateSimilarThread(int pointNum, TestingVisualCanvas tvc, HashMap<Integer, HashMap<String, Integer>> oldProperties,
			TrackPassMatrix oldTPM1, RadarTrack rt, TrackPassMatrix[] tpm, HashMap<Integer,RadarTrack> centerClustersHM){
		
		this.pointNum = pointNum;
		
		this.oldProperties = oldProperties;
		
		this.centerClustersHM = centerClustersHM;
		this.tvc = tvc;
		this.track = rt;
		this.centerTracksTPM = tpm;
		this.oldTPM1 = oldTPM1;
		
		delta_track = new RadarTrack();
		
		if(track.size()==1){
			delta_track.add(track.elementAt(track.size()-1));
			delta_track.add(track.elementAt(track.size()-1));
			delta_track.setTrackNo(track.getTrackNo());
		}
			
		else{
			delta_track.add(track.elementAt(track.size()-2));
			delta_track.add(track.elementAt(track.size()-1));
			delta_track.setTrackNo(track.getTrackNo());
		}
		
		delta_track_TPM = TrackUtilities.getTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, delta_track);
		
	}
	
	public void run(){
		
		Date da = new Date();
		long startTime = da.getTime();
		
		double oriLon=114.78;
		double oriLat=17.69;
		double maxLon=128.35;
		double maxLat=31;
	
		int grids=256;
		
		for(int i=0;i<centerTracksTPM.length;i++){
			
			//double currentScore = getSimilarityScore(oriLon, oriLat, maxLon, maxLat, grids, track, centerTracksTPM[i]);
			double currentScore = getMemorizeSimilarityScore(oriLon, oriLat, maxLon, maxLat, grids, oldProperties, delta_track, centerTracksTPM[i], i+1);
			
			double trackStartLon = track.elementAt(0).getLon();
			double trackStartLat = track.elementAt(0).getLat();
			
			double clusterTrackStartLon = centerClustersHM.get(i+1).elementAt(0).getLon();
			double clusterTrackStartLat = centerClustersHM.get(i+1).elementAt(0).getLat();
			
			double startPointDistance = Math.sqrt(Math.pow(trackStartLon-clusterTrackStartLon, 2)+
					Math.pow(trackStartLat-clusterTrackStartLat, 2));	//calculate start point distance
			
			//System.out.println("Start Point Distance"+startPointDistance);
			
			if(startPointDistance < 0.2){
				if(currentScore >= maxScore){
					maxScore = currentScore;
					MaxSimClusterNum = i+1;
				}
				//System.out.println(track.getTrackNo()+"\tcluster:"+(i+1)+"\t"+currentScore);
			}
			
		}
		
		//System.out.println(oldProperties);
		
//		if(delta_track.getTrackNo()==2049001){
//			
//			
//			printLogFile("C:\\Documents and Settings\\KPY\\桌面\\log.txt", "point num:"+pointNum+" trackNo:"+delta_track.getTrackNo()+" Max score count:"+
//					oldProperties.get(MaxSimClusterNum).get("count")+"\n");
//			
//			if(oldProperties.get(MaxSimClusterNum) == null)
//				printLogFile("C:\\Documents and Settings\\KPY\\桌面\\log.txt","NULL"+"\n");
//			else
//				printLogFile("C:\\Documents and Settings\\KPY\\桌面\\log.txt",oldProperties.toString()+"\n");
//		}
		
	
//		System.out.println("point num:"+pointNum+" trackNo:"+delta_track.getTrackNo()+" Max score count1:"+
//				oldProperties.get(MaxSimClusterNum).get("count1"));
//		System.out.println("point num:"+pointNum+" trackNo:"+delta_track.getTrackNo()+" Max score sim_dis:"+
//				oldProperties.get(MaxSimClusterNum).get("sim_distance"));
		
		
		System.out.println("Point Number:"+pointNum+"\t"+track.getTrackNo()+"\tMax Score:"+maxScore+"\tCluster:"+" "+MaxSimClusterNum);
		//System.out.println(track.getTrackNo()+"  "+track+"  cluster  "+centerClustersHM.get(MaxSimClusterNum));
		
		if(MaxSimClusterNum!=0){
			
			Graphics g = tvc.getGraphics();
			
			drawPredictedTracks(g, centerClustersHM.get(MaxSimClusterNum));
			tvc.update(g);
			
		}
		
		Object[] keyX = delta_track_TPM.keySet().toArray();
		
		for(int i=0;i<keyX.length;i++){
			
			if(oldTPM1.containsKey(keyX[i]) == false){
				
				HashMap<Integer, Integer> temHM = delta_track_TPM.get(keyX[i]);
				oldTPM1.put((int)keyX[i], temHM);
				
			}
			else{
				
				Object[] keyY = delta_track_TPM.get(keyX[i]).keySet().toArray();
				
				HashMap<Integer, Integer> temHM = oldTPM1.get(keyX[i]);
				
				for(int j=0;j<keyY.length;j++){
					
					if(temHM.containsKey(keyY[j]) == false)
						temHM.put((int)keyY[j], delta_track_TPM.get(keyX[i]).get(keyY[j]));
				}
				oldTPM1.put((int)keyX[i], temHM);
			}
		}
		
		//System.out.println(delta_track.getTrackNo()+"\t"+"old tpm1:"+oldTPM1);
		
		
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
		System.out.println(delta_track.getTrackNo()+"\t"+"Thread Time :"+diffSeconds+"(s)");
		
		
	}
	
	public double getSimilarityScore(double oriLon, double oriLat, 
			double maxLon, double maxLat, int grids,  RadarTrack rt1, TrackPassMatrix clusterTPM){
		
		Date da = new Date();
		long startTime = da.getTime();
		
		double score = 0;
		int count1=0, count2=0, count=0;;
		int sim_distance1=0, sim_distance2=0, sim_distance=0;
		
		TrackPassMatrix tpm1, tpm2;
		
		tpm1 = TrackUtilities.getDirTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt1);
		tpm2 = clusterTPM;
		
		//System.out.println("tpm1..."+tpm1);
		//System.out.println("tpm2..."+tpm2);
		
		
		Object[] key_X1 = tpm1.keySet().toArray();
		
		for(int i=0;i<key_X1.length;i++){
			
			Object[] key_Y1 = tpm1.get(key_X1[i]).keySet().toArray();
			
			for(int j=0;j<key_Y1.length;j++){
				
				if(tpm2.containsKey(key_X1[i]))
					if(tpm2.get(key_X1[i]).containsKey(key_Y1[j]))
						if(tpm1.get(key_X1[i]).get(key_Y1[j])==tpm2.get(key_X1[i]).get(key_Y1[j]))
							sim_distance1++;
				
				count1++;
			}
			
		}
		
		Object[] key_X2 = tpm2.keySet().toArray();
		
		for(int i=0;i<key_X2.length;i++){
			
			Object[] key_Y2 = tpm2.get(key_X2[i]).keySet().toArray();
			
			for(int j=0;j<key_Y2.length;j++){
				
				
				if(tpm1.containsKey(key_X2[i]))
					if(tpm1.get(key_X2[i]).containsKey(key_Y2[j]))
						sim_distance2++;
				
				count2++;
			}
			
		}
		
		sim_distance = sim_distance1 + sim_distance2;
		count = count1 + count2;
		
		score = 1.0*sim_distance/count*100;		// score = sim_distance/count*100;
		
//		System.out.println("count1:"+count1);
//		System.out.println("count2:"+count2);
//		
//		System.out.println("similar distance1:"+sim_distance1);
//		System.out.println("similar distance2:"+sim_distance2);
//		
//		System.out.println("similar distance:"+sim_distance);
//		System.out.println("count:"+count);
//		
//		System.out.println("score:"+score);
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
		//System.out.println("Get Sim Score :"+diffSeconds+"(s)");
		
		return score;
		
	}
	
//	public double getSimilarityScore(double oriLon, double oriLat, 
//			double maxLon, double maxLat, int grids, RadarTrack rt1, RadarTrack rt2){
//		
//		Date da = new Date();
//		long startTime = da.getTime();
//		
//		double score = 0;
//		int count1=0, count2=0, count=0;;
//		int sim_distance1=0, sim_distance2=0, sim_distance=0;
//		
//		TrackPassMatrix tpm1, tpm2;
//		
//		tpm1 = TrackUtilities.getDirTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt1);
//		tpm2 = TrackUtilities.getDirTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt2);
//		
//		//System.out.println("tpm1..."+tpm1);
//		//System.out.println("tpm2..."+tpm2);
//		
//		
//		Object[] key_X1 = tpm1.keySet().toArray();
//		
//		for(int i=0;i<key_X1.length;i++){
//			
//			Object[] key_Y1 = tpm1.get(key_X1[i]).keySet().toArray();
//			
//			for(int j=0;j<key_Y1.length;j++){
//				
//				if(tpm2.containsKey(key_X1[i]))
//					if(tpm2.get(key_X1[i]).containsKey(key_Y1[j]))
//						if(tpm1.get(key_X1[i]).get(key_Y1[j])==tpm2.get(key_X1[i]).get(key_Y1[j]))
//							sim_distance1++;
//				
//				count1++;
//			}
//			
//		}
//		
//		Object[] key_X2 = tpm2.keySet().toArray();
//		
//		for(int i=0;i<key_X2.length;i++){
//			
//			Object[] key_Y2 = tpm2.get(key_X2[i]).keySet().toArray();
//			
//			for(int j=0;j<key_Y2.length;j++){
//				
//				
//				if(tpm1.containsKey(key_X2[i]))
//					if(tpm1.get(key_X2[i]).containsKey(key_Y2[j]))
//						sim_distance2++;
//				
//				count2++;
//			}
//			
//		}
//		
//		sim_distance = sim_distance1 + sim_distance2;
//		count = count1 + count2;
//		
//		score = 1.0*sim_distance/count*100;
//		
////		System.out.println("count1:"+count1);
////		System.out.println("count2:"+count2);
////		
////		System.out.println("similar distance1:"+sim_distance1);
////		System.out.println("similar distance2:"+sim_distance2);
////		
////		System.out.println("similar distance:"+sim_distance);
////		System.out.println("count:"+count);
////		
////		System.out.println("score:"+score);
//		
//		da = new Date();
//		long endTime = da.getTime();
//		
//		long diff=endTime-startTime;
//		double diffSeconds=diff/1000.0;
//		
////		System.out.println("Pixels Distance Score Time :"+diffSeconds+"(s)");
//		
//		return score;
//		
//	}
	
	public synchronized  double getMemorizeSimilarityScore(double oriLon, double oriLat, 
			double maxLon, double maxLat, int grids, HashMap<Integer, HashMap<String, Integer>> oldProperties,
				RadarTrack delta_rt, TrackPassMatrix clusterTPM, int clusterID){
		
		Date da = new Date();
		long startTime = da.getTime();
		
		double score = 0;
		int oldCount1=0, delta_count1=0, count2=0, count_new=0;
		int delta_sim_distance1=0, delta_sim_distance2=0, sim_distance_new=0, old_sim_distance=0;
		
		if(oldProperties.get(clusterID) == null){
			oldProperties.put(clusterID, new HashMap<String, Integer>());
		}
			
		HashMap<String, Integer> secondHM = oldProperties.get(clusterID);
		if(secondHM == null)
			secondHM = new HashMap<String, Integer>();
		
		
		if(oldProperties.get(clusterID).get("count1") == null)
			oldCount1 = 0;
		else
			oldCount1 = oldProperties.get(clusterID).get("count1");
		
		if(oldProperties.get(clusterID).get("sim_distance") == null)
			old_sim_distance = 0;
		else
			old_sim_distance = oldProperties.get(clusterID).get("sim_distance");
		
		
		TrackPassMatrix tpm1, tpm2;
		
		tpm1 = TrackUtilities.getDirTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, delta_rt);
		tpm2 = clusterTPM;
		
		//System.out.println(delta_rt.getTrackNo()+" cluster "+clusterID+"\ttrack..."+delta_rt);
		//System.out.println(delta_rt.getTrackNo()+"\ttpm1..."+tpm1);
		//System.out.println(delta_rt.getTrackNo()+"\ttpm1 size..."+tpm1.getValueSize());
		//System.out.println("tpm2..."+tpm2);
		
		
		
		
		
		Object[] key_X1 = tpm1.keySet().toArray();
		
		for(int i=0;i<key_X1.length;i++){
			
			Object[] key_Y1 = tpm1.get(key_X1[i]).keySet().toArray();
			
			for(int j=0;j<key_Y1.length;j++){
				
				if(oldTPM1.containsKey(key_X1[i]) == false || oldTPM1.get(key_X1[i]).containsKey(key_Y1[j]) == false){
					if(tpm2.containsKey(key_X1[i]))
						if(tpm2.get(key_X1[i]).containsKey(key_Y1[j]))
							if(tpm1.get(key_X1[i]).get(key_Y1[j])==tpm2.get(key_X1[i]).get(key_Y1[j]))
								delta_sim_distance1++;
					
					delta_count1++;
					
				}
				
			}
			
		}
		
		Object[] key_X2 = tpm2.keySet().toArray();
		
		for(int i=0;i<key_X2.length;i++){
			
			Object[] key_Y2 = tpm2.get(key_X2[i]).keySet().toArray();
			
			for(int j=0;j<key_Y2.length;j++)
					count2++;
		}
		
		delta_sim_distance2 = delta_sim_distance1;
		
		//score_t = sim_t-1 + sim_delta(t) / count1_t-1 + count1_delta(t) + count2
		
		sim_distance_new = delta_sim_distance1 + delta_sim_distance2 + old_sim_distance;
		count_new = delta_count1 + count2 + oldCount1;
		
		score = 1.0*sim_distance_new/count_new*100;		// score = sim_distance/count*100;
				


//		System.out.println("score:"+score);
		
		secondHM.put("count1", delta_count1+oldCount1);
		secondHM.put("sim_distance", sim_distance_new);
		
		oldProperties.put(clusterID, secondHM);
		
//		if(delta_track.getTrackNo()==2049012){
//			
//			
//			printLogFile("C:\\Documents and Settings\\KPY\\桌面\\log.txt", 
//					pointNum+"\t"+delta_rt.getTrackNo()+" cluster "+clusterID+"\ttrack..."+delta_rt+
//						"\ttpm1..."+tpm1+"\ttpm1 size..."+tpm1.getValueSize()+"\t"+oldProperties+"\n");
//			
//			
//		}
		
		
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
//		//System.out.println("Get Sim Score :"+diffSeconds+"(s)");
		
//		System.out.println(delta_track);
//		System.out.println(clusterID);
//		System.out.println("trackNo:"+delta_track.getTrackNo());
		
		return score;
		
	}
	
	public void drawPredictedTracks(Graphics g, RadarTrack rt){
		
		
		double[] lonOfTrack;
		double[] latOfTrack;
		
		
		int trackSize = rt.size();
			
		lonOfTrack = new double[trackSize];
		latOfTrack = new double[trackSize];
			
		for(int i=0;i<trackSize;i++){
				
			lonOfTrack[i] = rt.get(i).getLon();
			latOfTrack[i] = rt.get(i).getLat();
			
			//System.out.println(lonOfTrack[i]);
			//System.out.println(latOfTrack[i]);
				
		}
		
		
		int[] xPixelTrack = getXPixelTrack(lonOfTrack);
		int[] yPixelTrack = getYPixelTrack(latOfTrack);
			
		int direction=1;
			
		if(xPixelTrack.length>1){
			if(xPixelTrack[1]-xPixelTrack[0]>=0)
				direction=1;
			else
				direction=0;
		}
				
		g.setColor(new Color(255,100,150));
			
		g.drawPolyline(xPixelTrack, yPixelTrack, xPixelTrack.length);
		
		//GUITasks.drawDashedPolyLine(g,xPixelTrack, yPixelTrack, xPixelTrack.length,2,2);
		
		
	}
	
	public HashMap<Integer, HashMap<String, Integer>> getOldProperties(){
		return oldProperties;
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
	
	public void printLogFile(String filePath, String logStr){
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter(filePath, true));
			
			bw.write(logStr);
			bw.close();
			
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		
	}
	
}

