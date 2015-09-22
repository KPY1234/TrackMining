package core.track;

import db.DatabaseConnection;

import core.RadarPoint;
import core.RadarTrack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Vector;
import java.util.HashMap;
import java.util.Date;


public class GeneratingTrack {
	
	DatabaseConnection dc = null;
	Connection conn = null;
	
	Vector<Integer> groupVector = null;
	
	RadarTrack[] rt = null;
	HashMap<Integer,RadarTrack> hm = null;
	
	public GeneratingTrack() throws SQLException{
		
		dc = new DatabaseConnection("jdbc:mysql://localhost:3306/airdefence","TrackMining","icrdc4isr19+");
		dc.startConnection();
		conn = dc.getConnection();
		
		groupVector = new Vector<Integer>();
		hm = new HashMap<Integer,RadarTrack>();
		
		
	}
	
	public void run() throws SQLException{
		
		Date da = new Date();
		long startTime=da.getTime();
		
		String sql = "SELECT trackNo FROM trackinfot_1000000_filter GROUP BY trackNo ORDER BY trackNo";
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		
		int columnNum=rsmd.getColumnCount();
		
		int count=0;
		
		while (rs.next()){
			groupVector.add(Integer.parseInt(rs.getObject(1).toString()));
			//System.out.println(rs.getObject(1).toString());
			count++;
		}
		
		//System.out.println(count);
		
		//System.out.println(groupVector);
		
		rt = new RadarTrack[groupVector.size()];	
				
		
		
		
		for(int i=0;i<groupVector.size();i++){
			
			sql = "SELECT trackNo,trackId,flightSize,height,xPos,yPos,xVel,yVel,trackTime FROM trackinfot_1000000_filter";
			
			String whereStr=" WHERE trackNo = "+groupVector.elementAt(i).toString()+" ORDER BY trackTime";
			sql+=whereStr;
			rs = stat.executeQuery(sql);
			rsmd = rs.getMetaData();
			columnNum=rsmd.getColumnCount();
			
			rt[i] = new RadarTrack();
			
			while (rs.next()){
				String temStr="";
				for(int j=0;j<columnNum;j++)
					temStr+=rs.getObject(j+1)+"\t";
				//System.out.println(temStr);
			
				rt[i].add(new RadarPoint(temStr));
			}
			hm.put(groupVector.elementAt(i), rt[i]);
			//System.out.println(groupVector.elementAt(i)+"\t"+hm.get(groupVector.elementAt(i)));
			//System.out.println(rt[i]);
			//System.out.println(rt[i].size());
		}
		//System.out.println(rt.length);
		//System.out.println(hm.size());
		
		rt = null;
		System.gc();
		
		dc.closeConnection();
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("Generating track elapse time:"+diffSeconds+"(s)");
		
	}
	
	public HashMap<Integer,RadarTrack> getRadarTrackHashMap(){
		return this.hm;
	}
	
	public Vector<Integer> getGroupVector(){
		return this.groupVector;
	}
	

	
	public static void main(String args[]){
		
		try{
			GeneratingTrack gt = new GeneratingTrack();
			gt.run();
		}catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
		
		
	}
	

}
