package core.cluster;

import core.Instance;
import core.RadarTrack;
import core.RadarPoint;
import core.TrackPassMatrix;
import core.CallTimesMatrix;
import core.track.TrackUtilities;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;

import format.FormatTransform;


public class RecursiveInterSection {
	
	
	ArrayList<Vector<RadarTrack>> allCluster = null;
	
	double scoreLimit=60;
	
	public RecursiveInterSection(double oriLon, double oriLat, 
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
						
						double[] score = getSimilarityScores(oriLon,oriLat,maxLon,maxLat,grids,allCluster.get(j).elementAt(k), hm.get(key[i]));
						
						if(score[0]>scoreLimit&&score[1]>scoreLimit){
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
		
		System.out.println("Recursive InterSection Time :"+diffSeconds+"(s)");
		
	}
	
	public double[] getSimilarityScores(double oriLon, double oriLat, 
			double maxLon, double maxLat, int grids, RadarTrack rt1, RadarTrack rt2){
		
		Date da = new Date();
		long startTime = da.getTime();
		
		TrackPassMatrix tpm1, tpm2;
		
		double[] scores = new double[2];
		
		tpm1 = TrackUtilities.getTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt1);
		tpm2 = TrackUtilities.getTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt2);
		
		CallTimesMatrix ctm1 = tpm1.getDefaultCallTimesMatrix();
		CallTimesMatrix ctm2 = tpm2.getDefaultCallTimesMatrix();
		
		Object[] key1 = tpm1.keySet().toArray();
		Object[] key2 = tpm2.keySet().toArray();
		
		int[] keyInt1 = new int[key1.length];
		int[] keyInt2 = new int[key2.length];
		
		for(int i=0;i<keyInt1.length;i++)
			keyInt1[i] = (int)key1[i];
		for(int i=0;i<keyInt2.length;i++)
			keyInt2[i] = (int)key2[i];
		
		
		Arrays.sort(keyInt1);
		Arrays.sort(keyInt2);
//		bubbleSort(keyInt1);
//		bubbleSort(keyInt2);
		
		//System.out.println("tpm1..."+tpm1);
		//System.out.println("tpm2..."+tpm2);
		
		//System.out.println("tpm1 size..."+tpm1.getValueSize());
		//System.out.println("tpm2 size..."+tpm2.getValueSize());
		
		
		//for(int i=0;i<keyInt1.length;i++)
			//System.out.println(keyInt1[i]);
		
		//System.out.println("before increase..."+ctm1);
		//System.out.println("before increase..."+ctm2);
		
		//increaseCallMatrix(ctm1, 0, 290, 0, 512);
		//System.out.println("after increase..."+ctm1);
		
		
		int count=getCallTimesMatrix(tpm1, tpm2, keyInt1, keyInt2, ctm1, ctm2, 0, grids, 0, grids, 0);
		
		
		scores=calculateScore(tpm1, tpm2,ctm1,ctm2,grids);
		
		//System.out.println("ctm1 after increase..."+ctm1);
		//System.out.println("ctm2 after increase..."+ctm2);
		
		//System.out.println("score1:"+scores[0]);
		//System.out.println("score2:"+scores[1]);
		
		
		//System.out.println(tpm1.getHashMap().size());
		//System.out.println(tpm2.getHashMap().size());
		
		//System.out.println(tpm1.size());
		//System.out.println(tpm2);
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
		//System.out.println("Calculate Similarity Score  Time :"+diffSeconds+"(s)");
		
		return scores;
		
	}
	
	
	
	public int getCallTimesMatrix(TrackPassMatrix tpm1, TrackPassMatrix tpm2, int[] key1, int[] key2, 
			CallTimesMatrix ctm1, CallTimesMatrix ctm2, int X_minBound, int X_maxBound, int Y_minBound, int Y_maxBound, int count){
		
		
	
		int X_range=X_maxBound-X_minBound;
		int X_midpoint=(int)Math.ceil(X_range/2.0)+X_minBound;
		
		int Y_range=Y_maxBound-Y_minBound;
		int Y_midpoint=(int)Math.ceil(Y_range/2.0)+Y_minBound;
		
		boolean isTpm1ContainKey=false;
		boolean isTpm2ContainKey=false;
		boolean bothContainKey=false;
		
		// Left Up
		for(int i=X_minBound;i<X_midpoint;i++){
			for(int j=Y_midpoint;j<Y_maxBound;j++){
				
				isTpm1ContainKey=false;
				isTpm2ContainKey=false;
				bothContainKey=false;
				
				if(tpm1.containsKey(i))
					if(tpm1.get(i).containsKey(j))
						isTpm1ContainKey=true;
				
				if(tpm2.containsKey(i))
					if(tpm2.get(i).containsKey(j))
						isTpm2ContainKey=true;
						
					
				if(isTpm1ContainKey==true && isTpm2ContainKey==true){
					//System.out.println("Left Up");
					//System.out.println("i="+i+" j="+j);
					bothContainKey=true;
					break;
				}
					
				
				//System.out.println(isTpm1ContainKey);
				//System.out.println(isTpm2ContainKey);
				
			}
			if(bothContainKey==true)
				break;
		}
		
		
		if(bothContainKey==true && X_maxBound-X_minBound>1 && Y_maxBound-Y_minBound>1){
			increaseCallMatrix(ctm1,X_minBound,X_midpoint,Y_midpoint,Y_maxBound);
			increaseCallMatrix(ctm2,X_minBound,X_midpoint,Y_midpoint,Y_maxBound);
			count=getCallTimesMatrix(tpm1, tpm2, key1, key2, ctm1, ctm2, X_minBound, X_midpoint, Y_midpoint, Y_maxBound, ++count);
			
		}
		
		//Right Up
	
		for(int i=X_midpoint;i<X_maxBound;i++){
			for(int j=Y_midpoint;j<Y_maxBound;j++){
				
				isTpm1ContainKey=false;
				isTpm2ContainKey=false;
				bothContainKey=false;
				
				if(tpm1.containsKey(i))
					if(tpm1.get(i).containsKey(j))
						isTpm1ContainKey=true;
				
				if(tpm2.containsKey(i))
					if(tpm2.get(i).containsKey(j))
						isTpm2ContainKey=true;
						
					
				if(isTpm1ContainKey==true && isTpm2ContainKey==true){
					//System.out.println("Right Up");
					//System.out.println("i="+i+" j="+j);
					bothContainKey=true;
					break;
				}
					
				
				//System.out.println(isTpm1ContainKey);
				//System.out.println(isTpm2ContainKey);
				
			}
			if(bothContainKey==true)
				break;
		}
		
		
		if(bothContainKey==true && X_maxBound-X_minBound>1 && Y_maxBound-Y_minBound>1){
			increaseCallMatrix(ctm1,X_midpoint,X_maxBound,Y_midpoint,Y_maxBound);
			increaseCallMatrix(ctm2,X_midpoint,X_maxBound,Y_midpoint,Y_maxBound);
			count=getCallTimesMatrix(tpm1, tpm2, key1, key2, ctm1, ctm2, X_midpoint, X_maxBound, Y_midpoint, Y_maxBound, ++count);
			
		}
		
		//Left Down
		
		for(int i=X_minBound;i<X_midpoint;i++){
			for(int j=Y_minBound;j<Y_midpoint;j++){
				
				isTpm1ContainKey=false;
				isTpm2ContainKey=false;
				bothContainKey=false;
				
				if(tpm1.containsKey(i))
					if(tpm1.get(i).containsKey(j))
						isTpm1ContainKey=true;
				
				if(tpm2.containsKey(i))
					if(tpm2.get(i).containsKey(j)){
						
						//System.out.println("i="+i+"j="+j);
						isTpm2ContainKey=true;
						
					}
						
						
					
				if(isTpm1ContainKey==true && isTpm2ContainKey==true){
					//System.out.println("Left Down");
					//System.out.println("i="+i+" j="+j);
					bothContainKey=true;
					break;
				}
					
				
				//System.out.println(isTpm1ContainKey);
				//System.out.println(isTpm2ContainKey);
				
			}
			if(bothContainKey==true)
				break;
		}
		
	
		if(bothContainKey==true && X_maxBound-X_minBound>1 && Y_maxBound-Y_minBound>1){
			increaseCallMatrix(ctm1,X_minBound,X_midpoint,Y_minBound,Y_midpoint);
			increaseCallMatrix(ctm2,X_minBound,X_midpoint,Y_minBound,Y_midpoint);
			count=getCallTimesMatrix(tpm1, tpm2, key1, key2, ctm1, ctm2, X_minBound, X_midpoint, Y_minBound, Y_midpoint, ++count);
			
		}
		
		
		//Right Down
		
		for(int i=X_midpoint;i<X_maxBound;i++){
			for(int j=Y_minBound;j<Y_midpoint;j++){
				
				isTpm1ContainKey=false;
				isTpm2ContainKey=false;
				bothContainKey=false;
				
				if(tpm1.containsKey(i))
					if(tpm1.get(i).containsKey(j)){
						//System.out.println("i="+i+" j="+j);
						isTpm1ContainKey=true;
					}
						
				
				if(tpm2.containsKey(i))
					if(tpm2.get(i).containsKey(j)){
						
						//System.out.println("i="+i+"j="+j);
						isTpm2ContainKey=true;
						
					}
						
						
					
				if(isTpm1ContainKey==true && isTpm2ContainKey==true){
					//System.out.println("Right Down");
					//System.out.println("i="+i+" j="+j);
					bothContainKey=true;
					break;
				}
					
				
				//System.out.println(isTpm1ContainKey);
				//System.out.println(isTpm2ContainKey);
				
			}
			if(bothContainKey==true)
				break;
		}
		
		
		if(bothContainKey==true && X_maxBound-X_minBound>1 && Y_maxBound-Y_minBound>1){
			increaseCallMatrix(ctm1,X_midpoint,X_maxBound,Y_minBound,Y_midpoint);
			increaseCallMatrix(ctm2,X_midpoint,X_maxBound,Y_minBound,Y_midpoint);
			count=getCallTimesMatrix(tpm1, tpm2, key1, key2, ctm1, ctm2, X_midpoint, X_maxBound, Y_minBound, Y_midpoint, ++count);
			
		}
		
		
		//System.out.println("Recursion times:"+count);
		
		//System.out.println(midpoint);
		
		return count;
		
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
	
	public double[] calculateScore(TrackPassMatrix tpm1, TrackPassMatrix tpm2, CallTimesMatrix ctm1, CallTimesMatrix ctm2, int maxPixels){
		
		double[] scores = new double[2];
		
		double score1=0,score2=0;
		
		score1=100*getSum(ctm1)/(log2(maxPixels)*getSum(tpm1));
		score2=100*getSum(ctm2)/(log2(maxPixels)*getSum(tpm2));
		
		scores[0]=score1;
		scores[1]=score2;
		
		return scores;
	}
	
	public void increaseCallMatrix(CallTimesMatrix ctm, int X_minBound, int X_maxBound, int Y_minBound, int Y_maxBound){
		
		for(int i=X_minBound;i<X_maxBound;i++){
			
			for(int j=Y_minBound;j<Y_maxBound;j++){
				
				if(ctm.containsKey(i))
					if(ctm.get(i).containsKey(j)){
						
						HashMap<Integer,Integer> temH = ctm.get(i);
						temH.put(j,temH.get(j)+1);
						ctm.put(i, temH);
					}
					
			}
			
		}
	}
	
	public double log2(double num){
		return Math.log10(num)/Math.log10(2);
	}
	
	public int getSum(HashMap<Integer,HashMap<Integer,Integer>> hm){
		
		int sum=0;
		Object[] keyX = hm.keySet().toArray();
		
		
		
		for(int i=0;i<keyX.length;i++){
				
			Object[] keyY = hm.get(keyX[i]).keySet().toArray();
				
			for(int j=0;j<keyY.length;j++){
					
				sum+=hm.get(keyX[i]).get(keyY[j]);
					
			}
				
		}
		
		//System.out.println("sum:"+sum);
		
		return sum;
		
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
	
	public void printFile(){
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:/Users/c11KPY/Desktop/RISClusters.txt"));
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
			
			str+="Cluster"+(i+1)+":\n";
			
			for(int j=0;j<allCluster.get(i).size();j++){
				str+=allCluster.get(i).elementAt(j).getTrackNo()+" "+allCluster.get(i).elementAt(j)+"\n";
				
			}
		}
		
		
		
		return str;
	}
	
	
	
	public static void main(String args[]){
		
		double oriLon=114.78;
		double oriLat=17.69;
		double maxLon=128.35;
		double maxLat=31;
	
		int grids=1024;
	
		HashMap<Integer, RadarTrack> hm = new HashMap<Integer, RadarTrack>();
		
		try{
			BufferedReader br = new BufferedReader(new FileReader("C:/Users/c11KPY/Desktop/clusterTem2.txt"));
			
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
//		
//		RadarTrack rt3 = new RadarTrack();
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(118.2);
//		rp.setLat(18.4);
//		
//		rt3.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(118.6);
//		rp.setLat(19.3);
//		
//		rt3.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(119.49);
//		rp.setLat(22.1);
//		
//		rt3.add(rp);
//		
//		hm.put(3, rt3);
//		
//		RadarTrack rt4 = new RadarTrack();
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(119.2);
//		rp.setLat(19.4);
//		
//		rt4.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(119.6);
//		rp.setLat(18.3);
//		
//		rt4.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(117.49);
//		rp.setLat(21.1);
//		
//		rt4.add(rp);
//		
//		hm.put(4, rt4);
//		
//		RadarTrack rt5 = new RadarTrack();
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(119.21);
//		rp.setLat(19.41);
//		
//		rt5.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(119.61);
//		rp.setLat(18.31);
//		
//		rt5.add(rp);
//		
//		rp = new RadarPoint();
//		rp.setHeight(360);
//		rp.setLon(117.491);
//		rp.setLat(21.11);
//		
//		rt5.add(rp);
//		
//		hm.put(5, rt5);
		
		RecursiveInterSection rs = new RecursiveInterSection(oriLon,oriLat,maxLon,maxLat,grids,hm);
		rs.printFile();
		rs.getCenterClustersHashMap();
		
		//double[] scores=rs.getSimilarityScores(oriLon, oriLat, maxLon, maxLat, grids, hm.get(2), hm.get(3));
		
	}

}
