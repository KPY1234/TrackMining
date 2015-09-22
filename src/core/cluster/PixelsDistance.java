package core.cluster;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;
import java.util.Vector;

import core.Instance;
import core.RadarPoint;
import core.RadarTrack;
import core.TrackPassMatrix;
import core.track.TrackUtilities;
import format.FormatTransform;

public class PixelsDistance {
	
	ArrayList<Vector<RadarTrack>> allCluster = null;
	
	double scoreLimit=90;
	
	public PixelsDistance(double oriLon, double oriLat, 
			double maxLon, double maxLat, int grids, HashMap<Integer,RadarTrack> hm){
		
		Date da = new Date();
		long startTime = da.getTime();
		
		allCluster = new ArrayList<Vector<RadarTrack>>();
		
		int clusterCount=0;
		boolean isJoined=false;
		
		Object[] key = hm.keySet().toArray();
		
		
		for(int i=0;i<key.length;i++){
			
			//System.out.println("i="+i);
			
			isJoined=false;
			
			if(allCluster.size()==0){
				clusterCount++;
				Vector<RadarTrack> temVec = new Vector<RadarTrack>();
				hm.get(key[i]).setClusterID(clusterCount);
				temVec.add(hm.get(key[i]));
				allCluster.add(temVec);
				System.out.println("track "+ key[i]+" Independent...");
				
			}
			else{
				for(int j=0;j<allCluster.size();j++){
					
					//System.out.println("j="+j);
					
					for(int k=0;k<allCluster.get(j).size();k++){
						
						//System.out.println("k="+k);
						
						double score = getSimilarityScore(oriLon,oriLat,maxLon,maxLat,grids,allCluster.get(j).elementAt(k), hm.get(key[i]));
						
						if(score>scoreLimit){
							isJoined=true;
							System.out.println("track "+ key[i]+" Pull by cluster "+(j+1)+" track "+ (k+1));
							break;
						}
						
					}
					if(isJoined){
						hm.get(key[i]).setClusterID(j+1);
						allCluster.get(j).add(hm.get(key[i]));
						break;
					}
					
				}
				
				if(!isJoined){
					clusterCount++;
					Vector<RadarTrack> temVec = new Vector<RadarTrack>();
					hm.get(key[i]).setClusterID(clusterCount);
					temVec.add(hm.get(key[i]));
					allCluster.add(temVec);
					System.out.println("track "+ key[i]+" Independent...");
				}
				
			}
			System.out.println("Progress...:\t"+(i+1)+"/"+key.length+"("+100.0*(i+1)/key.length+"%)");
			
		}
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
		System.out.println("Pixels Distance Clustering Time :"+diffSeconds+"(s)");
		
	}
	
	public double getSimilarityScore(double oriLon, double oriLat, 
			double maxLon, double maxLat, int grids, RadarTrack rt1, RadarTrack rt2){
		
		Date da = new Date();
		long startTime = da.getTime();
		
		double score = 0;
		int count1=0, count2=0, count=0;;
		int sim_distance1=0, sim_distance2=0, sim_distance=0;
		
		TrackPassMatrix tpm1, tpm2;
		
		tpm1 = TrackUtilities.getDirTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt1);
		tpm2 = TrackUtilities.getDirTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt2);
		
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
		
		score = 1.0*sim_distance/count*100;
		
//		System.out.println("count1:"+count1);
//		System.out.println("count2:"+count2);
//		
//		System.out.println("similar distance1:"+sim_distance1);
//		System.out.println("similar distance2:"+sim_distance2);
//		
//		System.out.println("similar distance:"+sim_distance);
//		System.out.println("count:"+count);
//		
		System.out.println("score:"+score);
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
//		System.out.println("Pixels Distance Score Time :"+diffSeconds+"(s)");
		
		return score;
		
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
	
	public void printFile(){
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\KPY\\орн▒\\PDClusters.txt"));
			bw.write(toString());
			bw.close();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		
	}
	
	public ArrayList<Vector<RadarTrack>> getAllCluster(){
		return allCluster;
	}
	
	public String toString(){
		
		String str="";
		
		for(int i=0;i<allCluster.size();i++){
			
			for(int j=0;j<allCluster.get(i).size();j++){
				str+=(i+1)+"\t"+allCluster.get(i).elementAt(j)+"\n";
				
			}
		}
		
		return str;
	}
	
	public static void main(String args[]){
		
		double oriLon=114.78;
		double oriLat=17.69;
		double maxLon=128.35;
		double maxLat=31;
	
		int grids=256;
		
		HashMap<Integer, RadarTrack> hm = new HashMap<Integer, RadarTrack>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("C:\\Documents and Settings\\KPY\\орн▒\\clusterTem1.txt"));
			
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
		
		PixelsDistance pd = new PixelsDistance(oriLon,oriLat,maxLon,maxLat,grids,hm);
		pd.printFile();
		
		
	}

}
