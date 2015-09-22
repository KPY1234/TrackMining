package core;

import java.util.Vector;


public class Instance extends Vector<Double> {
	
	int ID=0;
	boolean isVisited = false;
	int pointType=0;	//core=1,commom point=2, noise=3, unset=0
	int clusterID=0;
	int trackNo=0;
	
	public void setID(int id){
		this.ID=id;
	}
	
	public void setVisited(boolean isVisited){
		this.isVisited = isVisited;
	}
	public void setType(int type){
		this.pointType=type;
	}
	public void setClusterID(int clusterID){
		this.clusterID=clusterID;
	}
	
	public void setTrackNumber(int trackNo){
		this.trackNo=trackNo;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public boolean getVisited(){
		return isVisited;
	}
	
	public int getPointType(){
		return pointType;
	}
	
	public int getClusterID(){
		return this.clusterID;
	}
	
	public int getTrackNum(){
		return trackNo;
	}
	
	
	public String toString(){
		
		String str="";
		
		for(int i=0;i<this.size();i++){
			
			str+=this.elementAt(i);
			str+=" ";
		}
		
		return str;
	}
	
	public static void main(String args[]){
		
	}
	
	
}
