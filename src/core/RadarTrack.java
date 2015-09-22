package core;

import core.RadarPoint;

import java.util.Vector;
import java.util.StringTokenizer;


public class RadarTrack extends Vector<RadarPoint> {
	
	private int trackNo;
	private int clusterID=0;
	private boolean isVisited = false;
	
	
	public RadarTrack(){
		
	}
	
	public RadarTrack(String lineStr){
		
		int beginIndex = lineStr.indexOf("[")+1;
		int endIndex = lineStr.indexOf("]")-1;
		
		String trackStr = lineStr.substring(beginIndex, endIndex);
		StringTokenizer st = new StringTokenizer(trackStr, ",");
		
		while(st.hasMoreTokens()){
			
			
			
			String radarPointStr = st.nextElement().toString().trim();
			
			//System.out.println(radarPointStr);
			
			String[] radarPointFeatures = radarPointStr.split(" ");
			
			double[] floatRadarPointFeatures = new double[radarPointFeatures.length];
			
			for(int i=0;i<radarPointFeatures.length;i++){
				//System.out.println(radarPointFeatures[i]);
				floatRadarPointFeatures[i] = Double.parseDouble(radarPointFeatures[i]);
				
			}
			
			
			add(new RadarPoint(floatRadarPointFeatures[0],floatRadarPointFeatures[1],(int)floatRadarPointFeatures[2]));
			
			
		}
	}
	
	
	public String toString(){
		
		String tem="[";
		
		int size=this.size();
		
		for(int i=0;i<size;i++){
			tem+=this.elementAt(i).toString();
			if(i!=size-1)
				tem+=", "; 
		}
		tem+="]";
		
		return tem;
		
	}
	
	
	public void setTrackNo(int num){
		trackNo=num;
	}
	
	public void setClusterID(int id){
		clusterID=id;
	}
	
	public int getTrackNo(){
		return trackNo;
	}
	
	public int getClusterID(){
		return clusterID;
	}
	
	public static void main(String args[]){
		
		RadarTrack rt = new RadarTrack("2049001 [124.832 20.105 360 , 124.827 20.093 360 , 124.822 20.082 360 , 124.822 20.081 360 , 124.812 20.061 360 , 124.817 20.07 360 , 124.801 20.039 360 , 124.791 20.017 360 , 124.781 19.996 360 , 124.77 19.975 360 , 124.761 19.955 360 , 124.751 19.933 360 , 124.74 19.912 360 , 124.729 19.889 360 , 124.719 19.868 360 , 124.709 19.846 360 , 124.699 19.825 360 , 124.689 19.804 360 , 124.687 19.8 360 , 124.687 19.8 360 , 124.687 19.8 360 , 124.687 19.8 360 ]");
		
		//System.out.println(rt);
	}

}
