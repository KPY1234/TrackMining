package gui;

import core.RadarTrack;
import core.RadarPoint;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Dimension;
import java.awt.Canvas;
import java.awt.Color;

import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;


public class TrackVisualizeFrame extends JFrame {

	private JPanel contentPane;
	
	
	/**
	 * Create the frame.
	 */
	public TrackVisualizeFrame(HashMap<Integer,RadarTrack> trackMap) {
		
		setTitle("軌跡視覺化");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(0, 0, 1680, 1050);
	
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout(0, 0));
		
		VisualCanvas vc = new VisualCanvas(trackMap);
	
		contentPane.add(vc, BorderLayout.CENTER);
		
		
	}
	
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		
		final HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("C:\\Documents and Settings\\KPY\\桌面\\clusterTem1.txt"));
			
			String str = br.readLine();
			
			int count=0;
			
			while(str!=null){
				
				
				int beginIndex = str.indexOf('[');
				int endIndex = str.indexOf(']');
				
				str = str.substring(beginIndex+1, endIndex-1) ;
				
				//System.out.println(startIndex);
				//System.out.println(endIndex);
				//System.out.println(str);
				
				StringTokenizer st = new StringTokenizer(str,",");
				
				int trackLength = st.countTokens();
				
				RadarTrack rt = new RadarTrack();
				
				for(int i=0;i<trackLength;i++){
					
					String ptstr = st.nextToken();
					
					//System.out.println(ptstr);
					
					StringTokenizer ptst = new StringTokenizer(ptstr," ");
					
					int ptLength = ptst.countTokens();
					
					RadarPoint rp = new RadarPoint();
					
					for(int j=0;j<ptLength;j++){
						
						String featureStr =  ptst.nextToken();
						
						if(j==0)
							rp.setLon(Double.parseDouble(featureStr));
						else if(j==1)
							rp.setLat(Double.parseDouble(featureStr));
						else if(j==2)
							rp.setHeight(Integer.parseInt(featureStr));
						
						
						//System.out.println(featureStr);
						
					}
					
					rt.add(rp);
					
				}

				hm.put(++count, rt);
				
				str = br.readLine();
				
			}
			
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		
		
		
//		RadarTrack rt1 = new RadarTrack();
//		
//		RadarPoint rp = new RadarPoint();
//		rp.setHeight(200);
//		rp.setLon(120.5);
//		rp.setLat(20.3);
//		
//		rt1.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(200);
//		rp.setLon(121.3);
//		rp.setLat(21.9);
//		
//		rt1.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(122.7);
//		rp.setLat(23.0);
//		
//		rt1.add(rp);
//		
//		hm.put(1, rt1);
//		
//		RadarTrack rt2 = new RadarTrack();
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(120.1);
//		rp.setLat(20.3);
//		
//		rt2.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(121.5);
//		rp.setLat(20.6);
//		
//		rt2.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(119.48);
//		rp.setLat(22.0);
//		
//		rt2.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(122.48);
//		rp.setLat(22.5);
//		
//		rt2.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(118.48);
//		rp.setLat(19.0);
//		
//		rt2.add(rp);
//		
//		hm.put(2, rt2);
		
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					TrackVisualizeFrame frame = new TrackVisualizeFrame(hm);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

}


class VisualCanvas extends Canvas{
	
	int oriXPixel = 30;
	int oriYPixel = 950;
	int maxXPixel = 1660;
	int maxYPixel = 10;
	
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
	
	public VisualCanvas(HashMap<Integer,RadarTrack> trackMap){
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
		
		
		//System.out.println(g.getColor()); 
		
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
