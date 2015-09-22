package db.trans;

import db.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;

import java.util.Date;


public class CopyTable {
	
	
	
	public CopyTable(){
		
		
	}
	
	public static void run() throws SQLException{
		
		Date da = new Date();
		long startTime=da.getTime();
		
		
		DatabaseConnection dc = new DatabaseConnection("jdbc:mysql://localhost:3306/airdefence","TrackMining","icrdc4isr19+");
		dc.startConnection();
		Connection conn = dc.getConnection();
		Statement stat = conn.createStatement();
		
		String sql = "CREATE TABLE trackinfot_filter SELECT * FROM trackinfot WHERE trackId = 1 or trackId = 3 or trackId = 5";
		
		boolean isdone=stat.execute(sql);
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("elapse time:"+diffSeconds+"(s)");
		
		dc.closeConnection();
		
	}
	
	
	public static void main(String args[]){
		
		
		try{
			CopyTable.run();
		}catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
		
	}

}
