package db.trans;

import db.DatabaseConnection;
import db.DatabaseViewer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.util.Date;
import java.util.Vector;
import java.util.HashMap;


public class TransToTextFromDatabase {
	
	
	
	
	
	public TransToTextFromDatabase(){
		
	}
	
	
	public static void run() throws SQLException, IOException{
		
		Date da = new Date();
		long startTime=da.getTime();
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\trackinfot_filter.prop"));
		
		int limitSize = 10000000;
		
		String limit=" LIMIT "+limitSize;
		DatabaseConnection dc = new DatabaseConnection("jdbc:mysql://localhost:3306/airdefence","TrackMining","icrdc4isr19+");
		dc.startConnection();
		Connection conn = dc.getConnection();
		
		String sql = "SELECT trackNo,trackId,flightSize,height,xPos,yPos,xVel,yVel,trackTime FROM trackinfot " +
				"WHERE trackTime > 1352044800 and  trackTime < 1352131200 ORDER by trackNo,trackTime";
		
		Statement stat = conn.createStatement();
		
		ResultSet rs = stat.executeQuery(sql);
		
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int columnNum=rsmd.getColumnCount();
		
		
		
		
		for(int i=0;i<columnNum;i++){
			//System.out.print(rsmd.getColumnTypeName(i+1));
			bw.write(rsmd.getColumnTypeName(i+1));
			if(i!=columnNum-1){
				//System.out.print(",");
				bw.write(",");
			}
		}
		//System.out.println();
		bw.write("\n");
		
		bw.close();
		
		bw = new BufferedWriter(new FileWriter("D:\\trackinfot_20121105-20121106.txt"));
		
		int count=0;
		while (rs.next()){		
			String temStr="";
			for(int i=0;i<columnNum;i++)
				temStr+=rs.getObject(i+1)+"\t";
			//System.out.println(temStr);
			bw.write(temStr+"\n");
			count++;
			
			if(count%100000==0)
				System.out.println("count: "+count);
		}
		
		
		
		dc.closeConnection();
		bw.close();
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("Trans To Text From Database elapse time:"+diffSeconds+"(s)");
	
		
	}
	
	public static void generatePerTrack() throws IOException{
		
		HashMap<Integer,Vector<String>> hm = new HashMap<Integer,Vector<String>>();
		
		Date da = new Date();
		long startTime=da.getTime();
		
		BufferedReader br = new BufferedReader(new FileReader("D:\\trackinfot_filter.txt"));
		
		String readLineStr = br.readLine();
		
		while(readLineStr!=null){
			
			String[] splitStr = readLineStr.split("\t");
			int trackNoTem = Integer.parseInt(splitStr[0]);
		
			
			if(!hm.containsKey(trackNoTem)){
				
				Vector<String> temV = new Vector<String>();
				temV.add(readLineStr);
				hm.put(trackNoTem, temV);
			}
			
			else{
				
				Vector<String> temV = hm.get(trackNoTem);
				temV.add(readLineStr);
				hm.put(trackNoTem, temV);
			}
			
			
			
			//System.out.println(trackNoTem);
			//System.out.println(readLineStr);
			
			
			readLineStr = br.readLine();
		}
		
		br.close();
		
		Object[] keySet = hm.keySet().toArray();
		
		for(int i=0;i<keySet.length;i++){
			
			int trackNo = (int)keySet[i];
			
			String fileName="D:\\track\\trackinfot_filter_"+trackNo+".txt";
			BufferedWriter bw = new BufferedWriter(new FileWriter(fileName));
			
			Vector<String> temV = hm.get(trackNo);
			
			for(int j=0;j<temV.size();j++){
				
				bw.write(temV.elementAt(j));
				bw.write("\n");
				//System.out.println(temV.elementAt(j));
			}
			
			
			
			bw.close();
			
		}
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("Generate per track elapse time:"+diffSeconds+"(s)");
		
	}
	
	public static void combineValidPerTrackIntoFile() throws IOException{
		
		Date da = new Date();
		long startTime=da.getTime();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\trackinfot_filter_combined.txt"));
		
		String pathStr="D:\\track_filter";
		
		File ff = new File(pathStr);
		String[] fileNames = ff.list();
		
		bubbleSort(fileNames);
		
		for(int i=0;i<fileNames.length;i++){
			
			String fileNameStr = pathStr+"\\"+fileNames[i];
			
			BufferedReader br = new BufferedReader(new FileReader(fileNameStr));
			
			String readLineStr = br.readLine();
			
			int trackCount=1;
			int trackNo=0;
			
			while(readLineStr!=null){
				
				String[] splitStr = readLineStr.split("\t");
				
				trackNo = Integer.parseInt(splitStr[0])*1000+trackCount;
				
				int oldTrackTime = Integer.parseInt(splitStr[8]);
				
				
				splitStr[0] = Integer.toString(trackNo);
				
				String temStr="";
				for(int j=0;j<splitStr.length;j++){
					temStr+=splitStr[j];
					if(j!=splitStr.length-1)
						temStr+="\t";
					
				}
				//System.out.println(temStr);
				bw.write(temStr);
				bw.write("\n");
				
				//System.out.println(readLineStr);
				//System.out.println("old track time:"+oldTrackTime);
				
				readLineStr = br.readLine();
				
				if(readLineStr!=null){
					splitStr = readLineStr.split("\t");
				
					int newTrackTime = Integer.parseInt(splitStr[8]);
					
					//System.out.println("new track time:"+newTrackTime);
					
					int diff = newTrackTime-oldTrackTime;
					
					if(diff>100){
						
						trackCount++;
						
						//System.out.println("track time difference:"+(diff));
						//System.out.println("track Number:"+trackCount);
					}
						
				}
					
			}
			
			br.close();
			System.out.println(fileNames[i]);
		}
		
		
		
		bw.close();
		
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("Combine per track elapse time:"+diffSeconds+"(s)");
	}
	
	public static void combineValidPerTrackFromOneFile() throws IOException{
		
		Date da = new Date();
		long startTime=da.getTime();
		
		
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\trackinfot_20121105-20121106_combined.txt"));
		
		String fileNameStr = "D:\\trackinfot_20121105-20121106.txt";
		
		BufferedReader br = new BufferedReader(new FileReader(fileNameStr));
		
		String readLineStr = br.readLine();
		String[] lineSplit = readLineStr.split("\t");
		
		int trackNo = Integer.parseInt(lineSplit[0]);
		int trackTime = Integer.parseInt(lineSplit[8]);
		
		int trackCount=1;
		
		int oldTrackNum = trackNo;
		int oldTrackTime = trackTime;
		
		int writeTrackNo;
		
		while(readLineStr!=null){
			
			lineSplit = readLineStr.split("\t");
			trackNo = Integer.parseInt(lineSplit[0]);
			trackTime = Integer.parseInt(lineSplit[8]);
			
			//System.out.println("trackTime: "+trackTime+"\toldTrackTime: "+oldTrackTime);
			
			if(trackNo == oldTrackNum){
				
				if(trackTime - oldTrackTime > 100){
					trackCount++;
//					try{
//						Thread.sleep(5000);
//					}catch(InterruptedException ie){
//						ie.printStackTrace();
//					}
					
				}
				
			}
			else{
				oldTrackNum=trackNo;
				trackCount=1;
				
			}
			writeTrackNo = trackNo*1000 + trackCount;
			
			String writeStr="";
			
			writeStr+=Integer.toString(writeTrackNo)+"\t";
			
			for(int i=1;i<lineSplit.length;i++){
				writeStr+=lineSplit[i];
				
				if(i!=lineSplit.length-1)
					writeStr+="\t";
				
			}
			bw.write(writeStr+"\n");
			
			//System.out.println(writeTrackNo);
			
			//System.out.println(readLineStr);
			oldTrackTime = trackTime;
			readLineStr = br.readLine();
		}
		
		br.close(); 
		bw.close();
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("Combine per track from one file elapse time:"+diffSeconds+"(s)");
		
	}
	
	public static void generateRangeCombinedTrackFile() throws IOException{
		
		Date da = new Date();
		long startTime=da.getTime();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter("D:\\trackinfot_20121105-20121106_combined.txt"));
		
		BufferedReader br = new BufferedReader(new FileReader("D:\\trackinfot_20121105-20121106.txt"));
		
		String readLineStr = br.readLine();
		
		while(readLineStr!=null){
			
			String[] lineSplit = readLineStr.split("\t");
			
			int trackTime = Integer.parseInt(lineSplit[8]);
			
			if(trackTime > 1352044800 && trackTime < 1352131200)
				bw.write(readLineStr+"\n");
			
			
			readLineStr = br.readLine();
		}
		
		br.close();
		bw.close();
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("Generate Range Combined Track File elapse time:"+diffSeconds+"(s)");
	}
	
	
	public static void bubbleSort(String[] array){
		
		for(int i=0;i<array.length-1;i++){
			for(int j=0;j<array.length-1;j++){
				if(array[j].compareTo(array[j+1])>0){
					String tem=array[j+1];
					array[j+1]=array[j];
					array[j]=tem;
				}
			}
		}
		
		for(int i=0;i<array.length-1;i++){
			for(int j=0;j<array.length-1;j++){
				if(array[j].length()>array[j+1].length()){
					String tem=array[j+1];
					array[j+1]=array[j];
					array[j]=tem;
				}
			}
		}
		
		
	}
	
	public static void bubbleSort(int[] array){
		
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
	
	public static void main(String args[]){
		
		try{
			//TransToTextFromDatabase.run();
			TransToTextFromDatabase.combineValidPerTrackFromOneFile();
			//TransToTextFromDatabase.generateRangeCombinedTrackFile();
			//TransToTextFromDatabase.generatePerTrack();
			//TransToTextFromDatabase.combineValidPerTrackIntoFile();
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
		
		
	}

}
