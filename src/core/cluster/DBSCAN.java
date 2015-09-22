package core.cluster;

import core.RadarPoint;
import core.RadarTrack;
import core.Instance;
import core.track.FixTrackLength;

import format.FormatTransform;

import libsvm.svm_node;
import libsvm.svm_problem;

import java.sql.SQLException;
import java.util.Vector;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;



public class DBSCAN {
	
	final int CORE=1, COMMON=2, NOISE=3, UNSET=0;
	
	Vector<Instance> dataSet = null;
	
	ArrayList<Vector<Instance>> allCluster = null;
	
	Vector<Instance> OriInstances = null;
	
	
	public DBSCAN(Vector<Instance> Instances, double eps, int MinPts){
		
		//System.out.println(Instances);
		
		
		Date da = new Date();
		long startTime = da.getTime();
		
		OriInstances = new Vector<Instance>();
		
		for(int i=0;i<Instances.size();i++)
			OriInstances.add((Instance)Instances.elementAt(i).clone());
		
		Instances = getTrackNormalize(Instances);
		
		//System.out.println(Instances);
		
		//System.out.println(OriInstances.elementAt(0));
		
		dataSet = Instances;
		Vector<Instance> neighborPts = new Vector<Instance>();
		
		eps=Math.sqrt(dataSet.elementAt(0).size()*Math.pow(eps, 2));
		
		//System.out.println("feature number:"+dataSet.elementAt(0).size());
		System.out.println("eps:"+eps);
		//System.out.println(dataSet.elementAt(0));
		
		allCluster = new ArrayList<Vector<Instance>>();
		
		System.out.println("Data size:"+dataSet.size());
		
		for(int i=0;i<dataSet.size();i++){
			
			
			System.out.println("visited data:"+i);
			
			if(dataSet.elementAt(i).getVisited()==false){
				
				setVisited(dataSet.elementAt(i),true);
				neighborPts = regionQuery(dataSet.elementAt(i), eps);
				//System.out.println(i+":"+neighborPts);
				
				if(neighborPts.size()<MinPts)
					dataSet.elementAt(i).setType(NOISE);
				else{
					allCluster.add(new Vector<Instance>());
					expandCluster(dataSet.elementAt(i),neighborPts, allCluster, eps, MinPts);
				}
			}
		}
		
		for(int i=0;i<dataSet.size();i++){
			if(dataSet.elementAt(i).getClusterID()==0){
				allCluster.add(new Vector<Instance>());
				groupPoint(allCluster.size(),allCluster.get(allCluster.size()-1),dataSet.elementAt(i));
			}
				
			
		}
		
//		for(int i=0;i<allCluster.size();i++){
//			System.out.println("cluster"+i+":"+allCluster.get(i));
//			
//		}
		
//		for(int i=0;i<getCenterClusters().size();i++)
//			System.out.println("Center"+i+":"+getCenterClusters().elementAt(i));
		
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
		System.out.println("DBSCAN time :"+diffSeconds+"(s)");
			
	}
	
	public void expandCluster(Instance P, Vector<Instance> neighborPts, ArrayList<Vector<Instance>> allCluster, double eps, int MinPts){
		
		
		//System.out.println("In expanding...\t ID:"+P.getID());
		
		groupPoint(allCluster.size(),allCluster.get(allCluster.size()-1),P);
		
		Vector<Instance> _neighborPts = new Vector<Instance>();
		
		
		
		for(int i=0;i<neighborPts.size();i++){
			
			//System.out.println("In expanding...\tneighborPts size:"+neighborPts.size());
			
			if(neighborPts.elementAt(i).getVisited()==false){
				
				//System.out.println("neighborPts "+i+":"+neighborPts.elementAt(i));
				
				setVisited(neighborPts.elementAt(i),true);
			}
			_neighborPts = regionQuery(neighborPts.elementAt(i), eps);
			//System.out.println("_neighborPts"+_neighborPts);
			if(_neighborPts.size()>=MinPts)
				neighborPts=joined(neighborPts,_neighborPts);
			if(neighborPts.elementAt(i).getClusterID()==0)
				groupPoint(allCluster.size(), allCluster.get(allCluster.size()-1), neighborPts.elementAt(i));
				
		}
	
	}
	
	public Vector<Instance> regionQuery(Instance P, double eps){
		
		Vector<Instance> neighborhoodOfP = new Vector<Instance>();
		
		for(int i=0;i<dataSet.size();i++){
			
			if(EuclideanDistance(P, dataSet.elementAt(i))<eps)
				neighborhoodOfP.add(dataSet.elementAt(i));
		}
		
		return neighborhoodOfP;
	}
	
	public Vector<Instance> getTrackNormalize(Vector<Instance> Instances){
		
		int featureSize=3;
		
		
		double[] minValue = new double[featureSize];
		
		for(int i=0;i<minValue.length;i++){
			minValue[i]=Double.MAX_VALUE;
		}
		
		for(int i=0;i<Instances.size();i++){
			for(int j=0;j<Instances.elementAt(i).size();j++){
				
				if(Instances.elementAt(i).elementAt(j)<minValue[j%featureSize])
					minValue[j%featureSize]=Instances.elementAt(i).elementAt(j);
					
			}
		}
		
//		for(int i=0;i<minValue.length;i++)
//			System.out.println("min"+i+":"+minValue[i]);
		
		
		for(int i=0;i<Instances.size();i++){
			for(int j=0;j<Instances.elementAt(i).size();j++){
				
				if(minValue[j%featureSize]<0){
//					System.out.println(Instances.elementAt(i).elementAt(j));
//					System.out.println(Math.abs(minValue[j%featureSize]));
//					System.out.println(Instances.elementAt(i).elementAt(j)+Math.abs(minValue[j%featureSize]));
					Instances.elementAt(i).set(j, Instances.elementAt(i).elementAt(j)+Math.abs(minValue[j%featureSize]));
					
				}
					
				else{
//					System.out.println(Instances.elementAt(i).elementAt(j));
//					System.out.println(Math.abs(minValue[j%featureSize]));
//					System.out.println(Instances.elementAt(i).elementAt(j)-Math.abs(minValue[j%featureSize]));
					Instances.elementAt(i).set(j, Instances.elementAt(i).elementAt(j)-Math.abs(minValue[j%featureSize]));
				
				}
					
					
			}
		}
		
		
//		System.out.println("adjust min"+Instances);
		
		double[] maxValue = new double[featureSize];
		
		for(int i=0;i<maxValue.length;i++){
			maxValue[i]=Double.MIN_VALUE;
		}
		
		for(int i=0;i<Instances.size();i++){
			for(int j=0;j<Instances.elementAt(i).size();j++){
				
				if(Instances.elementAt(i).elementAt(j)>maxValue[j%featureSize])
					maxValue[j%featureSize]=Instances.elementAt(i).elementAt(j);
					
			}
	
		}
		
//		for(int i=0;i<maxValue.length;i++)
//			System.out.println("max"+i+":"+maxValue[i]);
		
		for(int i=0;i<Instances.size();i++){
			for(int j=0;j<Instances.elementAt(i).size();j++){
				
				Instances.elementAt(i).set(j, Instances.elementAt(i).elementAt(j)/maxValue[j%featureSize]);
				
			}
			
		}
		
		
		//System.out.println("After normalize instances:"+Instances);
		
		return Instances;
	}
	
	
	
	public void setVisited(Instance P, boolean isVisited){
		P.setVisited(isVisited);
		
		//System.out.println("Got visiting ID:"+P.getID());
	}
	
	public void groupPoint(int clusterID, Vector<Instance> cluster, Instance P){
		P.setClusterID(clusterID);
		cluster.add(P);
	}
	
	public Vector<Instance> joined(Vector<Instance> firstVec, Vector<Instance> lastVec){
		
		//System.out.println("join...");
		
		//System.out.println("before joined..."+firstVec);
		//System.out.println("the joined vector:"+lastVec);
		
		
		
		for(int i=0;i<lastVec.size();i++){
			boolean isReplicated=false;
			for(int j=0;j<firstVec.size();j++){
				if(lastVec.elementAt(i).getID()==firstVec.elementAt(j).getID()){
					isReplicated=true;
					break;
				}
			}
			if(isReplicated==false)
				firstVec.add(lastVec.elementAt(i));
		}
		
		//System.out.println("after joined..."+firstVec);
		
		return firstVec;
	}
	
	
	
	
	public double EuclideanDistance(Instance P, Instance dataSetInst){
		
		double diff=0;
		
		//System.out.println("P Size:"+P.size());
		//System.out.println("Data Set Size:"+dataSetInst.size());
		
		for(int i=0;i<P.size();i++){
			diff+=Math.pow(P.elementAt(i)-dataSetInst.elementAt(i),2);
			//System.out.println("P's feature:"+P.elementAt(i)+" data set's feature:"+dataSetInst.elementAt(i));
		}
			
			
		diff=Math.sqrt(diff);
		
		//System.out.println("diff:"+diff);
		
		return diff;
	}
	
//	public Vector<Instance> getInstanceFormHashMap(HashMap trackMap){
//		
//		Vector<Instance> instV = new Vector<Instance>();
//		
//		Object[] keyset = trackMap.keySet().toArray();
//		
//		for(int i=0;i<keyset.length;i++){
//			
//			
//			Instance inst = FormatTransform.RadarTrackToPointsVector((RadarTrack)trackMap.get(keyset[i]));
//			inst.setID(i+1);
//			instV.add(inst);
//			System.out.println(keyset[i]);
//			System.out.println(trackMap.get(keyset[i]));
//			System.out.println(inst);
//		}
//			
//		return instV;
//	}
	
	public ArrayList<Vector<Instance>> getAllCluster(){
		return this.allCluster;
	}
	
	public Vector<Instance> getCenterClusters(){
		
		 Vector<Instance> centerClusters = new Vector<Instance>();
		
		for(int i=0;i<allCluster.size();i++){
			
			double[] featureValue = new double[OriInstances.elementAt(0).size()];
			
			for(int j=0;j<allCluster.get(i).size();j++){
				
				for(int k=0;k<allCluster.get(i).elementAt(j).size();k++)
					featureValue[k] += OriInstances.elementAt(allCluster.get(i).elementAt(j).getID()-1).elementAt(k);
				
			}
			
			Instance inst = new Instance();
			
			for(int j=0;j<featureValue.length;j++){
				
				featureValue[j]/=allCluster.get(i).size();
				inst.add(featureValue[j]);
				//System.out.print("F"+j+":"+featureValue[j]+" ");
			}
			//System.out.print("\n");
			
			centerClusters.add(inst);
		}
		
		return centerClusters;
		
	}
	
	public HashMap<Integer,RadarTrack> getCenterClustersHashMap(){
		
		HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
		
			for(int i=0;i<allCluster.size();i++){
				
				double[] featureValue = new double[OriInstances.elementAt(0).size()];
				
				for(int j=0;j<allCluster.get(i).size();j++){
					
					for(int k=0;k<allCluster.get(i).elementAt(j).size();k++)
						featureValue[k] += OriInstances.elementAt(allCluster.get(i).elementAt(j).getID()-1).elementAt(k);
					
				}
				
				Instance inst = new Instance();
				
				for(int j=0;j<featureValue.length;j++){
					
					featureValue[j]/=allCluster.get(i).size();
					inst.add(featureValue[j]);
					//System.out.print("F"+j+":"+featureValue[j]+" ");
				}
				//System.out.print("\n");
				
				hm.put(i+1,FormatTransform.PointsVectorToRadarTrack(inst));
			}
		
		return hm;
		
	}
	
	public svm_problem getSVM_Prob(){
		
		svm_problem sp = new svm_problem();
		
		Vector<Instance> centerClusters = this.getCenterClusters();
		
		sp.l = centerClusters.size();
		sp.y = new double[sp.l];
		
		svm_node[][] sn = new svm_node[centerClusters.size()][];
		
		for(int i=0;i<centerClusters.size();i++){
			
			sp.y[i] = i+1;
			sn[i] = new svm_node[centerClusters.elementAt(i).size()];
			
			for(int j=0;j<centerClusters.elementAt(i).size();j++){
				
				sn[i][j] = new svm_node();
				sn[i][j].index = j+1;
				sn[i][j].value = centerClusters.elementAt(i).elementAt(j);
				
			}
			
		}
		sp.x = sn;
		
		
		return sp;
		
//		svm_problem sp = new svm_problem();
//		
//		
//		ArrayList<Vector<Instance>> allCluster = this.getAllCluster();
//		
//		
//		sp.l = OriInstances.size();
//		sp.y = new double[sp.l];
//		
//		svm_node[][] sn = new svm_node[OriInstances.size()][];
//		
//		int row=0;
//		
//		for(int i=0;i<allCluster.size();i++){
//			
//			
//			for(int j=0;j<allCluster.get(i).size();j++){
//				
//				
//				sp.y[row] = i+1;
//				sn[row] = new svm_node[allCluster.get(i).elementAt(j).size()];
//				
//				for(int k=0;k<allCluster.get(i).elementAt(j).size();k++){
//					
//					sn[row][k] = new svm_node();
//					sn[row][k].index = k+1;
//					sn[row][k].value = OriInstances.elementAt(allCluster.get(i).elementAt(j).getID()-1).elementAt(k);
//					
//				}
//				
//				row++;
//			}
//			
//		}
//		sp.x = sn;
//		
//		
//		return sp;
		
	}
	
	public Vector<Instance> getOriInstances(){
		return this.OriInstances;
	}
	
	public Vector<Instance> getOneCluster(int clusterNum){
		
		Vector<Instance> oneCluster = new Vector<Instance>();
		
		
		for(int i=0;i<allCluster.get(clusterNum).size();i++){
			
			oneCluster.add(allCluster.get(clusterNum).elementAt(i));
			//System.out.println("Inst"+i+":"+allCluster.get(clusterNum).elementAt(i));
		}
		
		
	
		
		
		return oneCluster;
		
	}
	
	public void printFile(){
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\KPY\\орн▒\\DBSCANClusters.txt"));
			bw.write(toStringDetailed());
			bw.close();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		
		
	}
	
	public String toString(){
		
		String tem="";
		
		for(int i=0;i<allCluster.size();i++){
			
			tem+="Cluster:"+(i+1);
			
			tem+="\t[";
			
			for(int j=0;j<allCluster.get(i).size();j++){
				
				tem+=allCluster.get(i).elementAt(j).getID();
				if(j!=allCluster.get(i).size()-1)
					tem+=" , ";
			}
			tem+="]";
			tem+="\n";
		}
		
		return tem;
	}
	
	public String toStringDetailed(){
		
		String tem="";
		
		for(int i=0;i<allCluster.size();i++){
			
			tem+="Cluster:"+(i+1);
			
			tem+="\t[";
			
			for(int j=0;j<allCluster.get(i).size();j++){
				
				tem+=allCluster.get(i).elementAt(j).getID();
				if(j!=allCluster.get(i).size()-1)
					tem+=" , ";
			}
			tem+="]";
			tem+="\n";
			tem+="\n";
			
			for(int j=0;j<allCluster.get(i).size();j++){
				
				tem+=OriInstances.elementAt(allCluster.get(i).elementAt(j).getID()-1).toString();
				if(j!=allCluster.get(i).size()-1)
					tem+="\n";
			}
			tem+="\n";
			tem+="\n";
		}
		
		Vector<Instance> centerClusters = this.getCenterClusters();
		
		tem+="Cluster Center...\n";
		
		for(int i=0;i<centerClusters.size();i++){
			tem+="Cluster Center:"+(i+1)+"\n\n";
			tem+=centerClusters.elementAt(i).toString();
			tem+="\n\n";
		}
		
		tem+=getSVM_Prob().toString();
	
		
		return tem;
		
	}
	
	
	public static void main(String args[]){
		

//		Instance[] inst = new Instance[10];
//		Vector<Instance> insts = new Vector<Instance>();
//		
//		for(int i=0;i<inst.length;i++){
//			
//			inst[i] = new Instance();
//			inst[i].setID(i+1);
//			inst[i].add(1.0+i);
//			inst[i].add(1.0+i*i-2*i);
//			insts.add(inst[i]);
//		}
//		
//		//inst[0].set(0, 50);
//		//inst[0].add(20);
//		
//		inst[8].set(1, 40.0);
//		inst[9].set(1, 45.0);
//		
//		//System.out.println(insts.elementAt(0).elementAt(0));
//		System.out.println("Instances:"+insts);
//
//		DBSCAN dbs = new DBSCAN(insts, 0.1, 3);
//		System.out.println(dbs.toStringDetailed());
		
		
		Vector<Instance> insts = null;
		
		try{
			FixTrackLength ft = new FixTrackLength();
			insts = ft.getInstances();
		
		}catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
		
		//System.out.println("Instances:"+insts);
		
		DBSCAN dbs = new DBSCAN(insts, 0.03, 3);
		//System.out.println(dbs.toStringDetailed());
		
		try{
			BufferedWriter bw = new BufferedWriter(new FileWriter("C:\\Documents and Settings\\KPY\\орн▒\\Clusters.txt"));
			bw.write(dbs.toStringDetailed());
			bw.close();
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		
	}

}
