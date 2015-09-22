package db;

import db.DatabaseConnection;

import java.sql.Connection;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

public class DatabaseViewer {
	
//	public String DBDRIVER = "org.gjt.mm.mysql.Driver" ;	//MySQL driver
//	public String DBURL = "jdbc:mysql://localhost:3306/airdefence" ;	//database connection address
//	public String DBUSER = "" ;
//	public String DBPASS = "";
	Connection conn = null;
	Statement stat = null;
	ResultSet rs = null;
	ResultSetMetaData rsmd = null;
	
	String limit=" LIMIT 100";
	
	int columnNum=0;
	String[] columnName=null; 
	String[][] databaseText=null;
	
	public DatabaseViewer(DatabaseConnection dc) throws SQLException{
		
		dc.startConnection();
		conn = dc.getConnection();
		viewDatabase();
		dc.closeConnection();
		
	}
	
	public void viewDatabase() throws SQLException{
		
		Date da = new Date();
		long startTime=da.getTime();
		
		String str="";
		String sql = "SELECT trackNo,height,xPos,yPos,trackTime FROM trackinfot GROUP BY trackNo"+limit;
		
		stat = conn.createStatement();
		rs = stat.executeQuery(sql);
		rsmd=rs.getMetaData();
		
		//System.out.println(rsmd.getColumnCount());
		columnNum=rsmd.getColumnCount();
		columnName=new String[columnNum];
		
		//System.out.println(rsmd.getColumnName(1));
		
		for(int i=0;i<columnNum;i++)
			columnName[i]=rsmd.getColumnName(i+1);
		
		Vector<Integer> trackNoGroup = new Vector<Integer>();
	
		System.out.println("trackNo\theight\txPos\tyPos\ttrackTime");
		
		int count=0;
		
		while (rs.next()){
			trackNoGroup.add(rs.getInt("trackNo"));
			System.out.println(rs.getInt("trackNo")+"\t"+rs.getInt("height")+"\t"+rs.getInt("xPos")+"\t"+rs.getInt("yPos")+"\t"+rs.getInt("trackTime"));
			count++;
		}
		
		System.out.println("Count="+count);
			
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("elapse time:"+diffSeconds+"(s)");
		System.out.println("trackNo size:"+trackNoGroup.size());
		
		/*
		
		sql="SELECT * FROM trackinfot WHERE trackNo="+trackNoGroup.elementAt(0);
		rs = stat.executeQuery(sql);
		System.out.println("trackNo\theight\txPos\tyPos\ttrackTime");
		while (rs.next()){
			trackNoGroup.add(rs.getInt("trackNo"));
			System.out.println(rs.getInt("trackNo")+"\t"+rs.getInt("height")+"\t"+rs.getInt("xPos")+"\t"+rs.getInt("yPos")+"\t"+rs.getInt("trackTime"));
		}
		
		*/
		
		
		
		
	}
	
	
	
	public String[][] getDatabaseText(int startLine, int num) throws SQLException{
		return databaseText;
	}
	
	
	
	public String[] getColumnName(){
		return columnName;
	}
	
	
	public static void main(String args[]){
		
		try{
			DatabaseConnection dc = new DatabaseConnection("jdbc:mysql://localhost:3306/airdefence","TrackMining","icrdc4isr19+");
			DatabaseViewer dv = new DatabaseViewer(dc);
			System.out.println(dv.getDatabaseText(0, 20));
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
}
