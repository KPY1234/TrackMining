package format;

import core.RadarPoint;
import core.RadarTrack;
import core.Instance;

import java.util.Vector;
import java.util.HashMap;

import core.RadarTrack;

public class FormatTransform {
	
	
	public FormatTransform(){
		
	}
	
	public static Instance RadarTrackToPointsVector(RadarTrack rt){
		
		Instance psv = new Instance();
		
		for(int i=0;i<rt.size();i++){
			
			//psv.add(1.0*rt.elementAt(i).getXPos());
			//psv.add(1.0*rt.elementAt(i).getYPos());
			psv.add(rt.elementAt(i).getLon());
			psv.add(rt.elementAt(i).getLat());
			psv.add(1.0*rt.elementAt(i).getHeight());
		}
		return psv;
	}
	
	public static RadarTrack PointsVectorToRadarTrack(Instance inst){
		
		RadarTrack rt = new RadarTrack();
		
		int trackSize=inst.size()/3;
		
		for(int i=0;i<trackSize;i++){
			
			RadarPoint rp = new RadarPoint();
			rp.setLon(inst.elementAt(i*3));
			rp.setLat(inst.elementAt(i*3+1));
			rp.setHeight((int)Math.round(inst.elementAt(i*3+2)));
			rt.add(rp);
		}
		return rt;
	}
	
	public static void main(String args[]){
		
	}

}
