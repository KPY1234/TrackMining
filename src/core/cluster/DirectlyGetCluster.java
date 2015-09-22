package core.cluster;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import core.Instance;
import core.RadarTrack;
import format.FormatTransform;

public class DirectlyGetCluster {
	
	
	ArrayList<Vector<RadarTrack>> allCluster = null;
	
	
	public DirectlyGetCluster(String fileName){
		
		Date da = new Date();
		long startTime=da.getTime();
		
		allCluster = new ArrayList<Vector<RadarTrack>>();
		
		
		int count=0;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
			String str = br.readLine();
			
			while(str!=null){
				
				String[] splitStr = str.split("\t");
				
				int clusterNum = Integer.parseInt(splitStr[0]);
				
				if(allCluster.size()<clusterNum)
					allCluster.add(new Vector<RadarTrack>());
				
				RadarTrack rt = new RadarTrack(str);
				allCluster.get(clusterNum-1).add(rt);
				//System.out.println("clusterNum: "+clusterNum+" "+rt);
				count++;
				str = br.readLine();
			}
			br.close();
			
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		
		
	
		
		
		System.out.println("count:"+count);
		
		//System.out.println(hm);
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		System.out.println("Generating cluster from clusters text elapse time:"+diffSeconds+"(s)");
		
		
		
	}
	
	public HashMap<Integer,RadarTrack> getCenterClustersHashMap(){
		
		HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
		
		int minSize=Integer.MAX_VALUE;
		
		for(int i=0;i<allCluster.size();i++){
			
			minSize=Integer.MAX_VALUE;
			
			for(int j=0;j<allCluster.get(i).size();j++)
				if(allCluster.get(i).elementAt(j).size()<minSize)
					minSize=allCluster.get(i).elementAt(j).size();
			
			double[] averageFeature = new double[minSize*3];
			
			for(int j=0;j<allCluster.get(i).size();j++){
				
				for(int k=0;k<minSize;k++){
					
					averageFeature[k*3]+=allCluster.get(i).elementAt(j).elementAt(k).getLon();
					averageFeature[k*3+1]+=allCluster.get(i).elementAt(j).elementAt(k).getLat();
					averageFeature[k*3+2]+=allCluster.get(i).elementAt(j).elementAt(k).getHeight();
					
				}
				
			}
			
			Instance inst = new Instance();
			
			//System.out.println("Cluster"+i+":");
			for(int j=0;j<averageFeature.length;j++){
				averageFeature[j]/=allCluster.get(i).size();
				inst.add(averageFeature[j]);
				//System.out.print(averageFeature[j]+" ");
				
			}
			hm.put(i+1,FormatTransform.PointsVectorToRadarTrack(inst));
			
			//System.out.print("\n");
			//System.out.println("minimal size:"+minSize);
			
			
		}
		//System.out.println(hm);
		
		return hm;
		
	}
	
	public ArrayList<Vector<RadarTrack>> getAllCluster(){
		return allCluster;
	}
	
	public String toString(){
		
		String str="";
		
		for(int i=0;i<allCluster.size();i++){
			
			str+="Cluster"+(i+1)+":\n";
			
			for(int j=0;j<allCluster.get(i).size();j++){
				str+=(i+1)+" "+allCluster.get(i).elementAt(j)+"\n";
				
			}
		}
		
		return str;
	}
	
	
	
	public static void main(String[] args){
		DirectlyGetCluster dgc = new DirectlyGetCluster("C:\\Documents and Settings\\KPY\\орн▒\\PPDCluster_result_1M.txt");
		//System.out.println(dgc.getCenterClustersHashMap());
	}

}
