package db.trans;

import db.DatabaseConnection;
import db.DatabaseViewer;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.sql.SQLException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Date;
import java.util.StringTokenizer;

public class TransToDatabaseFromText {
	
	
	public TransToDatabaseFromText(){
		
	}
	
	
	
	public static void run(String propFileName, String contentFileName) throws SQLException, IOException {
		
		
		String[] columnName = null;
		String[] columnType = null;
		int columnNum=0;
		
		Date da = new Date();
		long startTime=da.getTime();
		
		BufferedReader br = new BufferedReader(new FileReader(propFileName));
		
		
		String lineStr;
		
		
		lineStr=br.readLine();
		
		System.out.println(lineStr);
		
		StringTokenizer st = new StringTokenizer(lineStr,",");
		
		columnNum=st.countTokens();
		
		columnName = new String[columnNum];
		

		
		//System.out.println(st.countTokens());
		
		int count=0;
		
		while (st.hasMoreTokens()) {
	         //System.out.println(st.nextToken());
			 columnName[count++]=st.nextToken();
	     }
		
		
		
		
		lineStr=br.readLine();
		System.out.println(lineStr);
		
		st = new StringTokenizer(lineStr,",");
		
		columnType = new String[st.countTokens()];
		
		count=0;
		
		while (st.hasMoreTokens()) {
	         //System.out.println(st.nextToken());
			 columnType[count++]=st.nextToken();
	     }
		
		
		
		br.close();
		
		for(int i=0;i<columnType.length;i++)
			if(columnType[i].equals("VARCHAR"))
				columnType[i]+="(20)";
		
		
		DatabaseConnection dc = new DatabaseConnection("jdbc:mysql://localhost:3306/backup","TrackMining","icrdc4isr19+");
		dc.startConnection();
		Connection conn = dc.getConnection();
		
		String sqlContent="";
		
		for(int i=0;i<columnNum;i++){
			sqlContent+=columnName[i]+" "+columnType[i];
			if(i!=columnNum-1)
				sqlContent+=" , ";
		}
		System.out.println(sqlContent);
		
		String sql = "CREATE TABLE trackinfot_filter_20121105 ( "+sqlContent+")";
					 
		
		Statement stat = conn.createStatement();
		
		
		boolean isDone=stat.execute(sql);

		String columnNameLine="";
		
		for(int i=0;i<columnNum;i++){
			columnNameLine+=columnName[i];
			if(i!=columnNum-1)
				columnNameLine+=" , ";
		}
		System.out.println(columnNameLine);
		
		String columnValueLine="";
		
		
		br = new BufferedReader(new FileReader(contentFileName));
		
		lineStr=br.readLine();
		
		while(lineStr!=null){
			
			st = new StringTokenizer(lineStr);
			
			int tokenNum=st.countTokens();
			
			String tem="";
			for(int i=0;i<tokenNum;i++){
				
				tem+=st.nextToken();
				if(i!=tokenNum-1)
					tem+=", ";
				
			}
			//System.out.println("Line"+tem);
			sql = "INSERT INTO trackinfot_filter_20121105("+columnNameLine+")"+"VALUES ("+tem+")";
			stat.executeUpdate(sql);
			lineStr=br.readLine();
		}
		
		br.close();
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		
		System.out.println("elapse time:"+diffSeconds+"(s)");
		dc.closeConnection();
		
	}
	
	
	
	public static void main(String args[]){
		
		try{
			TransToDatabaseFromText.run("D:\\trackinfot_1000000.prop", "D:\\trackinfot_filter_20121105-20121106.txt");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		
	}

}
