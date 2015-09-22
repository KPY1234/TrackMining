package core;

import java.util.StringTokenizer;

import format.CoordTransform;


public class RadarPoint {
	
	private int trackNo;
	private int trackId;
	private int flightSize;
	private int height;
	private int xPos;
	private int yPos;
	private double lon;
	private double lat;
	private int xVel;
	private int yVel;
	private int trackTime;
	
	public RadarPoint(){
		
	}
	
	public RadarPoint(double lon, double lat, int height){
		
		this.lon=lon;
		this.lat=lat;
		this.height=height;
	}
	
	public RadarPoint(int trackNo,int trackId, int flightSize,int height,int xPos,int yPos,int xVel,int yVel,int trackTime){
		
		this.trackNo=trackNo;
		this.trackId=trackId;
		this.flightSize=flightSize;
		this.height=height;
		this.xPos=xPos;
		this.yPos=yPos;
		this.xVel=xVel;
		this.yVel=yVel;
		this.trackTime=trackTime;
		
		double[] lonlat = CoordTransform.XYToLonLat(xPos, yPos);
		
		setLon(lonlat[0]);
		setLat(lonlat[1]);
	}
		
	public RadarPoint(String featureStr){
		
		StringTokenizer st = new StringTokenizer(featureStr);
		
		int count=0;
		
		while(st.hasMoreElements()){
			String tem=st.nextToken();
			
			switch(count){
				case 0:
					//System.out.println("trackNo="+tem);
					this.trackNo=Integer.parseInt(tem);
					break;
				case 1:
					//System.out.println("trackId="+tem);
					this.trackId=Integer.parseInt(tem);
					break;
				case 2:
					//System.out.println("flightSize="+tem);
					this.flightSize=Integer.parseInt(tem);
					break;
				case 3:
					//System.out.println("height="+tem);
					this.height=Integer.parseInt(tem);
					break;
				case 4:
					//System.out.println("xPos="+tem);
					this.xPos=Integer.parseInt(tem);
					break;
				case 5:
					//System.out.println("yPos="+tem);
					this.yPos=Integer.parseInt(tem);
					break;
				case 6:
					//System.out.println("xVel="+tem);
					this.xVel=Integer.parseInt(tem);
					break;
				case 7:
					//System.out.println("yVel="+tem);
					this.yVel=Integer.parseInt(tem);
					break;
				case 8:
					//System.out.println("trackTime="+tem);
					this.trackTime=Integer.parseInt(tem);
					break;
			}
			
			count++;
		}
		
		double[] lonlat = CoordTransform.XYToLonLat(xPos, yPos);
		
		setLon(lonlat[0]);
		setLat(lonlat[1]);
		
	}
	
	public void setLon(double lon){
		this.lon=lon;
	}
	public void setLat(double lat){
		this.lat=lat;
	}
	
	public void setHeight(int h){
		this.height=h;
	}
	
	public int getTrackNo(){
		return trackNo;
	}
	public int getTrackId(){
		return trackId;
	}
	public int getFlightSize(){
		return flightSize;
	}
	public int getHeight(){
		return height;
	}
	public int getXPos(){
		return xPos;
	}
	public int getYPos(){
		return yPos;
	}
	public double getLon(){
		return lon;
	}
	public double getLat(){
		return lat;
	}
	public int getXVel(){
		return xVel;
	}
	public int getYVel(){
		return yVel;
	}
	public int getTrackTime(){
		return trackTime;
	}
	
	public String toString(){
		//String tem="height: "+getHeight()+" lon: "+getLon()+" lat:"+getLat()+" trackTime: "+getTrackTime()+" ";
		String tem=getLon()+" "+getLat()+" "+getHeight()+" ";
		return tem;
	}
	
	public static void main(String args[]){
		
		
		RadarPoint rp = new RadarPoint("3004	3	0	282	-203753	2726411	0	0	1352114191");
		
		//System.out.println(rp.getTrackNo());
		//System.out.println(rp.getHeight());
		
		
	}
	

}
