package core.machineLearning;

import core.Instance;

import core.cluster.DBSCAN;
import core.track.FixTrackLength;

import libsvm.svm;
import libsvm.svm_parameter;
import libsvm.svm_problem;
import libsvm.svm_model;
import libsvm.svm_node;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Vector;


public class Training {
	
	svm_model sm = null;
	
	public Training(svm_problem prob, svm_parameter param){
		sm = svm.svm_train(prob, param);
		
		
	}
	
	
	
	
	
	
	public svm_model getModel(){
		return sm;
	}
	
	
	public static svm_node[] getSVM_NodeFromInstance(Instance inst){
		
		svm_node[] sn = new svm_node[inst.size()];
		
		for(int i=0;i<sn.length;i++){
			sn[i] = new svm_node();
			sn[i].index=i+1;
			sn[i].value=inst.elementAt(i);
		}
		
		return sn;
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
//		//System.out.println(dbs.toStringDetailed());
		
		
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
		
		
		
		svm_parameter sp = new svm_parameter();
		//sp.svm_type = svm_parameter.C_SVC;
		//sp.kernel_type = svm_parameter.POLY;
		sp.gamma = 0.1;
		sp.C = 100;
		sp.eps=0.001;
		
		System.out.println("Training Prob="+dbs.getSVM_Prob());
		
		Training tr = new Training(dbs.getSVM_Prob(),sp);
		
		svm_node[] testingData = getSVM_NodeFromInstance(dbs.getOriInstances().elementAt(6));
		
		System.out.println("Testing data1:"+dbs.getOriInstances().elementAt(6));
		
		svm_node[] testingData2 = getSVM_NodeFromInstance(dbs.getOriInstances().elementAt(0));
		
		System.out.println("Testing data2:"+dbs.getOriInstances().elementAt(0));
		
		double predictedValue = svm.svm_predict(tr.getModel(), testingData);
		System.out.println("predict1:"+predictedValue);
		predictedValue = svm.svm_predict(tr.getModel(), testingData2);
		System.out.println("predict2:"+predictedValue);
		
		System.out.println("Number of support vector is:"+tr.getModel().l);
		
		//System.out.println("Model's support vector:"+tr.getModel());
		
		
		
		
		
	}
	
	
	
}
