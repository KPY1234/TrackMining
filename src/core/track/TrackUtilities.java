package core.track;

import core.Instance;
import core.RadarPoint;
import core.RadarTrack;
import core.TrackPassMatrix;
import db.DatabaseUtilities;
import format.FormatTransform;

import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.util.StringTokenizer;

import java.sql.SQLException;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;




public class TrackUtilities {
	
	
	HashMap<Integer,RadarTrack> hm = null;
	
	public TrackUtilities(){
		
	}
	
	public void exportTrack(String outFileName) throws IOException{
		
		int filterSize=10;
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(outFileName));
		
		Object[] keySet = hm.keySet().toArray();
		
		int[] keySetInt = new int[keySet.length];
		
		for(int i=0;i<keySetInt.length;i++)
			keySetInt[i] = (int)keySet[i];
		
		bubbleSort(keySetInt);
		
		for(int i=0;i<keySetInt.length;i++){
			int trackNo = keySetInt[i];
			
			if(hm.get(trackNo).size()>=filterSize){
				bw.write(trackNo+" ");
				bw.write(hm.get(trackNo).toString());
				bw.write("\n");
			}
			
		}
		bw.close();
		
	}
	
	public void setHashMap(HashMap<Integer,RadarTrack> hm){
		this.hm = hm;
	}
	
	public HashMap<Integer,RadarTrack> getTrackFromDatabase(DatabaseUtilities du) throws SQLException{
		
		HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
		
		Vector<Integer> groupVector = new Vector<Integer>();
		
		
		RadarTrack[] rt = null;
		
		Date da = new Date();
		long startTime=da.getTime();
		
		String sql = "SELECT trackNo FROM trackinfot_1000000_filter GROUP BY trackNo ORDER BY trackNo";
		
		Vector<String> queryResult = du.getQuery(sql);
		
		for(int i=0;i<queryResult.size();i++){
			
			groupVector.add(Integer.parseInt(queryResult.elementAt(i)));
			//System.out.println(groupVector.elementAt(i));
		}
			
		rt = new RadarTrack[groupVector.size()];
		
		for(int i=0;i<groupVector.size();i++){
			
			sql = "SELECT trackNo,trackId,flightSize,height,xPos,yPos,xVel,yVel,trackTime FROM trackinfot_1000000_filter";
			
			String whereStr=" WHERE trackNo = "+groupVector.elementAt(i).toString()+" ORDER BY trackTime";
			sql+=whereStr;
			
			queryResult = du.getQuery(sql);
			
			rt[i] = new RadarTrack();
			
			rt[i].setTrackNo(groupVector.elementAt(i));
			
			for(int j=0;j<queryResult.size();j++){
				
				rt[i].add(new RadarPoint(queryResult.elementAt(j)));
//				System.out.println(queryResult.elementAt(j));
				
			}
			hm.put(groupVector.elementAt(i), rt[i]);
			//System.out.println(groupVector.elementAt(i)+"\t"+hm.get(groupVector.elementAt(i)));
			//System.out.println(rt[i].size());
		}
		
		System.out.println(hm.size());
		
		rt = null;
		System.gc();
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		System.out.println("Generating track elapse time:"+diffSeconds+"(s)");
		
//		System.out.println(queryResult);
//		System.out.println(queryResult.size());
		
		setHashMap(hm);
		
		return hm;
		
	}
	
	public HashMap<Integer,RadarTrack> getTrackFromPointsText(String fileName) {
		
		Date da = new Date();
		long startTime=da.getTime();
		
		HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
		
		Vector<Integer> groupVector = new Vector<Integer>();
		
		long dataStartTime = Long.MAX_VALUE, dataEndTime = Long.MIN_VALUE;
		
		
		int count=0;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
			String str = br.readLine();
			StringTokenizer st = new StringTokenizer(str);
			
			
			
			while(str!=null){
				
				
				String[] splitStr = str.split("\t");
				
				if(Long.parseLong(splitStr[8]) < dataStartTime)
					dataStartTime = Long.parseLong(splitStr[8]);
				if(Long.parseLong(splitStr[8]) > dataEndTime)
					dataEndTime = Long.parseLong(splitStr[8]);
				
				RadarPoint rp = new RadarPoint(str);
				
				if(!groupVector.contains((int)rp.getTrackNo())){
					
					//System.out.println("trackNo:"+rp.getTrackNo());
					groupVector.add(rp.getTrackNo());
				}
					
				
				if(!hm.containsKey(rp.getTrackNo())){
					RadarTrack rt = new RadarTrack();
					rt.setTrackNo(rp.getTrackNo());
					rt.add(rp);
					hm.put(rp.getTrackNo(), rt);
				}
				else{
					
					RadarTrack rt = hm.get(rp.getTrackNo());
					rt.add(rp);
					hm.put(rp.getTrackNo(), rt);
					
				}
					
				
				str = br.readLine();
				
				if(count%100000==0)
					System.out.println("count:"+count);
				
				count++;
				
//				if(count==2000){
//					System.out.println("break");
//					break;
//				}
				
			}
			br.close();
			
		}catch(IOException ioex){
			
		}
		
		System.out.println("Data start time:"+dataStartTime+"\tData end time:"+dataEndTime);
		
		//System.out.println("track number:"+groupVector.size());
		
		System.out.println("count:"+count);
		
		//System.out.println(hm);
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		System.out.println("Generating track from points text elapse time:"+diffSeconds+"(s)");
		
		setHashMap(hm);
		
		return hm;
	}
	
	public HashMap<Integer,RadarTrack> getTrackFromTracksText(String fileName) {
		
		Date da = new Date();
		long startTime=da.getTime();
		
		HashMap<Integer,RadarTrack> hm = new HashMap<Integer,RadarTrack>();
		
		int count=0;
		
		try{
			BufferedReader br = new BufferedReader(new FileReader(fileName));
			
			String str = br.readLine();
			
			while(str!=null){
				
				String[] splitStr = str.split("\t");
				
				int trackNo = Integer.parseInt(splitStr[0]);
				RadarTrack rt = new RadarTrack(str);
				hm.put(trackNo, rt);
				//System.out.println("trackNo: "+trackNo+" "+rt);
				count++;
				str = br.readLine();
			}
			br.close();
			
		}catch(IOException ioex){
			
		}
		
		
		System.out.println("count:"+count);
		
		//System.out.println(hm);
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		System.out.println("Generating track from tracks text elapse time:"+diffSeconds+"(s)");
		
		setHashMap(hm);
		
		return hm;
	}
	
	
	
	public HashMap<Integer,RadarTrack> getFilterTrack(HashMap<Integer,RadarTrack> oriHm){
		
		
		Object[] keyset = oriHm.keySet().toArray();
		
		Vector<Integer> groupVector = new Vector<Integer>();
		
		for(int i=0;i<keyset.length;i++)
			groupVector.add((int)keyset[i]);
		
		
		int minLengthLimit=10;
		
		for(int i=0;i<groupVector.size();i++){
			if(oriHm.get(groupVector.elementAt(i)).size()<minLengthLimit){
				oriHm.remove(groupVector.elementAt(i));
				
			}
				
		}
		
		return oriHm;
		
	}
	
	public HashMap<Integer,RadarTrack> getFixLengthTrack(int length, HashMap<Integer,RadarTrack> oriHm){
		
		int maxLength = length;
		Object[] keyset = oriHm.keySet().toArray();
		
		Vector<Integer> groupVector = new Vector<Integer>();
		
		for(int i=0;i<keyset.length;i++)
			groupVector.add((int)keyset[i]);
		
		int trackLength;
		
		for(int i=0;i<groupVector.size();i++){
			
			trackLength = oriHm.get(groupVector.elementAt(i)).size();
			RadarTrack rt_tem = oriHm.get(groupVector.elementAt(i));
			
			//System.out.println("before:"+trackLength);
			
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
			oriHm.put(groupVector.elementAt(i), rt_tem);
			
			//System.out.println(oriHm.get(groupVector.elementAt(i)));
			trackLength = oriHm.get(groupVector.elementAt(i)).size();
			//System.out.println("after:"+trackLength);
		}
		
		
		
		
		
		return oriHm;
	}
	
	public static Vector<Instance> getInstances(HashMap<Integer,RadarTrack> hm){
		
		
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
	
	/*
	 * width x height matrix
	 */
	public static TrackPassMatrix getDirTrackPassMatrix(double oriLon, double oriLat, 
									double maxLon, double maxLat, int grids, RadarTrack rt){
		
		Date da = new Date();
		long startTime = da.getTime();
		
		
		TrackPassMatrix tpm = new TrackPassMatrix();
		
		double lonInterval = (maxLon - oriLon)/grids;
		double latInterval = (maxLat - oriLat)/grids;
		
		
		for(int i=0;i<rt.size()-1;i++){
			
			double x0 = rt.elementAt(i).getLon();
			double y0 = rt.elementAt(i).getLat();
			double x1 = rt.elementAt(i+1).getLon();
			double y1 = rt.elementAt(i+1).getLat();
			
			
			
			Vector<Double> passPoints = getPassPoint(x0,y0,x1,y1,lonInterval,latInterval);
			//System.out.println(passPoints);
			
			for(int j=0;j<passPoints.size()-1;j+=2){
				
				int[] lonLatCor = getMatrixCor(oriLon,oriLat,passPoints.elementAt(j),passPoints.elementAt(j+1),lonInterval,latInterval);
				
				//System.out.println("Key_Y"+lonLatCor[1]);
				
				HashMap<Integer, Integer> hm;
				
				if(tpm.containsKey(lonLatCor[0]))
					hm = tpm.get(lonLatCor[0]);
				else
					hm = new HashMap<Integer, Integer>();
				
				
				if(passPoints.elementAt(passPoints.size()-1)==1.0)
					hm.put(lonLatCor[1], 1);
				else
					hm.put(lonLatCor[1], -1);
				
				tpm.put(lonLatCor[0], hm);
				
			}
			
			//System.out.println(tpm);
			
		}
		
//		Object[] key_X = tpm.keySet().toArray();
//		
//		for(int i=0;i<key_X.length;i++){
//			System.out.println("keyX"+key_X[i]);
//		}
		
		
		//Vector<Double> passPoints = getPassPoint(114.80,17.7,119,20,lonInterval,latInterval);
		
		//System.out.println(passPoints);
		//System.out.println(passPoints.size());
		
		
//		int[] lonLatCor = getMatrixCor(oriLon,oriLat,114.7933,17.791,lonInterval,latInterval);
//		
		//System.out.println("Lon Interval="+lonInterval);
		//System.out.println("Lat Interval="+latInterval);
		
		da = new Date();
		long endTime = da.getTime();
		
		long diff=endTime-startTime;
		double diffSeconds=diff/1000.0;
		
//		System.out.println("Get DirTrack Pass Matrix :"+diffSeconds+"(s)");
		
		return tpm;
	}
	
	public static TrackPassMatrix getTrackPassMatrix(double oriLon, double oriLat, 
			double maxLon, double maxLat, int grids, RadarTrack rt){

		TrackPassMatrix tpm = new TrackPassMatrix();

		double lonInterval = (maxLon - oriLon)/grids;
		double latInterval = (maxLat - oriLat)/grids;


		for(int i=0;i<rt.size()-1;i++){

			double x0 = rt.elementAt(i).getLon();
			double y0 = rt.elementAt(i).getLat();
			double x1 = rt.elementAt(i+1).getLon();
			double y1 = rt.elementAt(i+1).getLat();



			Vector<Double> passPoints = getPassPoint(x0,y0,x1,y1,lonInterval,latInterval);
			//System.out.println(passPoints);

			for(int j=0;j<passPoints.size()-1;j+=2){

				int[] lonLatCor = getMatrixCor(oriLon,oriLat,passPoints.elementAt(j),passPoints.elementAt(j+1),lonInterval,latInterval);

				//System.out.println("Key_Y"+lonLatCor[1]);

				HashMap<Integer, Integer> hm;

				if(tpm.containsKey(lonLatCor[0]))
					hm = tpm.get(lonLatCor[0]);
				else
					hm = new HashMap<Integer, Integer>();


				
				hm.put(lonLatCor[1], 1);
				

				tpm.put(lonLatCor[0], hm);

			}

			//System.out.println(tpm);

		}

		//Object[] key_X = tpm.keySet().toArray();
		//
		//for(int i=0;i<key_X.length;i++){
		//System.out.println("keyX"+key_X[i]);
		//}


		//Vector<Double> passPoints = getPassPoint(114.80,17.7,119,20,lonInterval,latInterval);

		//System.out.println(passPoints);
		//System.out.println(passPoints.size());


		//int[] lonLatCor = getMatrixCor(oriLon,oriLat,114.7933,17.791,lonInterval,latInterval);
		//
		//System.out.println("Lon Interval="+lonInterval);
		//System.out.println("Lat Interval="+latInterval);


		return tpm;
	}
	
	public static int[] getMatrixCor(double oriLon, double oriLat, double lon, double lat, 
															double lonInterval, double latInterval){
		
		int[] lonLatCor = new int[2];
		
		int X_Cor = (int)Math.floor((lon - oriLon)/lonInterval);
		int Y_Cor = (int)Math.floor((lat - oriLat)/latInterval);
		
		
//		System.out.println("X Cor:"+X_Cor);
//		System.out.println("Y Cor:"+Y_Cor);
		
		lonLatCor[0]=X_Cor;
		lonLatCor[1]=Y_Cor;
		
		return lonLatCor;
	}
	
	public static Vector<Double> getPassPoint(double x0,double y0,double x1,double y1,
																double lonInterval, double latInterval){
		
		Vector<Double> passPoints = new Vector<Double>();
		double slope = (y1-y0)/(x1-x0);
		
		//System.out.println("numerator="+(y1-y0));
		//System.out.println("denominator="+(x1-x0));
		
		//System.out.println(slope);
		
		passPoints.add(x0);
		passPoints.add(y0);
		
		//System.out.println("x0="+x0);
		//System.out.println("y0="+y0);
		
		if(x1>x0){
			
			double x=x0;
			double y=y0;
			
			while(x<x1){
				
				x=x+(lonInterval/1000);
				
				if(x>x1)
					break;
				
				passPoints.add(x);
				y=slope*(x-x0)+y0;
				passPoints.add(y);
				
				//System.out.println("x="+x);
				//System.out.println("y="+y);
				
			}
		}
		
		if(x1<x0){
			
			double x=x0;
			double y=y0;
			
			while(x>x1){
				
				x=x-(lonInterval/1000);
				
				if(x<x1)
					break;
				
				passPoints.add(x);
				y=slope*(x-x0)+y0;
				passPoints.add(y);
				
				//System.out.println("x="+x);
				//System.out.println("y="+y);
				
			}
		}
		
		if(x1>=x0)				//add X-axis direction 
			passPoints.add(1.0);
		else
			passPoints.add(-1.0);
		
		return passPoints;
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
	
	public static void main(String args[]){
		
//		String driver="org.gjt.mm.mysql.Driver";
//		String url="jdbc:mysql://localhost:3306/airdefence";
//		
//		HashMap<Integer,RadarTrack> track, filterTrack, fixLengthTrack;
//		
//		
//		try{
//			DatabaseUtilities du = new DatabaseUtilities(driver,url,"TrackMining","icrdc4isr19+");
//			TrackUtilities tu = new TrackUtilities();
//			track = tu.getTrackFromDatabase(du);
//			filterTrack = tu.getFilterTrack(track);
//			//System.out.println(filterTrack.size());
//			fixLengthTrack = tu.getFixLengthTrack(20, filterTrack);
//			//System.out.println(fixLengthTrack.size());
//			Vector<Instance> inst = tu.getInstances(fixLengthTrack);
//			
//		}
//		catch(SQLException sqlex){
//			sqlex.printStackTrace();
//		}
		
		
		double oriLon=114.78;
		double oriLat=17.69;
		double maxLon=128.35;
		double maxLat=31;
		int grids=1024;
		
		
		RadarTrack rt = new RadarTrack();
		
		RadarPoint rp = new RadarPoint();
		rp.setHeight(200);
		rp.setLon(120.5);
		rp.setLat(20.3);
		
		rt.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(200);
		rp.setLon(121.3);
		rp.setLat(21.9);
		
		rt.add(rp);
		
		rp = new RadarPoint();
		rp.setHeight(360);
		rp.setLon(122.7);
		rp.setLat(23.0);
		
		rt.add(rp);
		
		
		TrackPassMatrix tpm = TrackUtilities.getTrackPassMatrix(oriLon, oriLat, maxLon, maxLat, grids, rt);
		TrackUtilities tu = new TrackUtilities();
		tu.getTrackFromPointsText("D:\\trackinfot_filter_combined.txt");
		
		try{
			tu.exportTrack("D:\\tracksManufactures_filter10.txt");
		}catch(IOException ioex){
			ioex.printStackTrace();
		}
		
		
		
		//System.out.println(tpm);
		
		
		
		
	}
	
}
