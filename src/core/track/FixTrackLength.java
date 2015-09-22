package core.track;

import core.RadarPoint;
import core.RadarTrack;
import core.Instance;
import core.track.GeneratingTrack;

import java.sql.SQLException;

import java.util.HashMap;
import java.util.Vector;

import format.FormatTransform;

public class FixTrackLength {
	
	Vector<Integer> groupVector = null;
	GeneratingTrack gt = null;
	
	HashMap<Integer,RadarTrack> hm = null;
	
	public FixTrackLength() throws SQLException{
		
		
		gt = new GeneratingTrack();
		gt.run();
		groupVector = gt.getGroupVector();
		hm = gt.getRadarTrackHashMap();
		trackFilter();
		fixLength();
		
		
	}
	
	public void fixLength(){
		
		int maxLength=getMaxLength();
		
		maxLength=20;
		
		int trackLength;
		
		//System.out.println(maxLength);
		
		for(int i=0;i<groupVector.size();i++){
			
			trackLength = hm.get(groupVector.elementAt(i)).size();
			RadarTrack rt_tem = hm.get(groupVector.elementAt(i));
			
			//System.out.println(trackLength);
			
			if(trackLength<maxLength){
				
				for(int j=trackLength;j<maxLength;j++){
					
					int index=(int)Math.round(1.0*j*trackLength/maxLength);
					RadarPoint newPoint = rt_tem.elementAt(index);
					
					rt_tem.add(newPoint);
					
					
					//System.out.print(index+" ");
					
				}
			
			}
			else{
				
				for(int j=trackLength-1;j>=maxLength;j--){
					rt_tem.remove(j);
				}
				
			}
			hm.put(groupVector.elementAt(i), rt_tem);
			
			//System.out.println(hm.get(groupVector.elementAt(i)));
			trackLength = hm.get(groupVector.elementAt(i)).size();
			//System.out.println("after:"+trackLength);
		}
		
	}
	
	public void trackFilter(){
		
//		System.out.println(hm.size());
//		System.out.println(groupVector.size());
		
		int minLengthLimit=10;
		
		for(int i=0;i<groupVector.size();i++){
			if(hm.get(groupVector.elementAt(i)).size()<minLengthLimit){
				hm.remove(groupVector.elementAt(i));
				groupVector.removeElementAt(i);
				
			}
				
		}
//		System.out.println(hm.size());
//		System.out.println(groupVector.size());
	}
	
	public int getMaxLength(){
		
		int max=0;
		
		for(int i=0;i<groupVector.size();i++){
			if(hm.get(groupVector.elementAt(i)).size()>max)
				max=hm.get(groupVector.elementAt(i)).size();
		}
		//System.out.println(max);
		return max;
	}
	
	public HashMap getTracks(){
		return hm;
	}
	
	public Vector<Instance> getInstances(){
		
		Vector<Instance> instV = new Vector<Instance>();
		
		Object[] keyset = hm.keySet().toArray();
		
		for(int i=0;i<keyset.length;i++){
			
			//System.out.println("The "+(i+1)+":"+keyset[i]);
			
			Instance inst = FormatTransform.RadarTrackToPointsVector((RadarTrack)hm.get(keyset[i]));
			
			//hm.put((Integer)keyset[i],null);
			//System.gc();
			
			
			//System.out.println(inst);
			//System.out.println(inst.size());
			inst.setID(i+1);
			inst.setTrackNumber((int)keyset[i]);
			instV.add(inst);
			
//			System.out.println(keyset[i]);
//			System.out.println(hm.get(keyset[i]));
//			System.out.println(inst);
		}
			
		return instV;
	}
	
	public static void main(String args[]){
		
		try{
			FixTrackLength ft = new FixTrackLength();
			Vector<Instance> instV=ft.getInstances();
		
		}catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
		
		
	}
	

}
