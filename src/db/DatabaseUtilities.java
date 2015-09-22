package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;


public class DatabaseUtilities {
	
	
	public String DBDRIVER = "" ;	//MySQL driver
	public String DBURL = "" ;	//database connection address
	public String DBUSER = "" ;
	public String DBPASS = "";
	Connection conn = null;
	
	
	
	
	
	
	public DatabaseUtilities() throws SQLException{
		
		this("org.gjt.mm.mysql.Driver","jdbc:mysql://localhost:3306/airdefence","TrackMining","icrdc4isr19+");
		
	}
	
	public DatabaseUtilities(String driver, String url, String user, String pass) throws SQLException{
		
		DBDRIVER=driver;
		DBURL=url;
		DBUSER=user;
		DBPASS=pass;
		connect();
	}
	
	public void connect() throws SQLException{
		
		try{
			Class.forName(DBDRIVER);
			
			conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
		
			
		}catch(ClassNotFoundException ex){
			
			ex.printStackTrace();
		}
	}
	
	
	
	public void closeConnection() throws SQLException{
		conn.close();
	}
	
	public Vector<String> getQuery(String sql) throws SQLException{
		
		Vector<String> text = new Vector<String>();
		
		String[] columnLabel;
		
		ResultSetMetaData rsmd = null;
		
		
		Date da = new Date();
		long startTime=da.getTime();
		
		Statement stat = conn.createStatement();
		ResultSet rs = stat.executeQuery(sql);
		rsmd=rs.getMetaData();
		
		da = new Date();
		long endTime=da.getTime();
		long diffTime=endTime-startTime;
		double diffSeconds=diffTime/1000.0;
		
		//System.out.println("Query Time:"+diffSeconds+"(s)");
		
		int columnNum=rsmd.getColumnCount();
		
		columnLabel = new String[columnNum];
		
		
		for(int i=0;i<columnNum;i++){
			
			columnLabel[i] = rsmd.getColumnName(i+1);
			//System.out.println(columnLabel[i]);
			
		}
		
		int count=0;
		
		while (rs.next()){
			
			String tem="";
			
			for(int i=0;i<columnNum;i++){
				
				tem+=rs.getString(columnLabel[i]);
				
				if(i!=columnNum-1)
					tem+="\t";
				
				//System.out.print(rs.getString(columnLabel[i])+"\t");
			}
			text.add(tem);
			//System.out.print("\n");
			
			count++;
		}
		
//		for(int i=0;i<text.size();i++)
//			System.out.println(text.elementAt(i));
			
		
		return text;
	}
	
	
	public void setDriver(String driverStr){
		DBDRIVER=driverStr;
	}
	public void setURL(String url){
		DBURL=url;
	}
	
	public void setUser(String user){
		DBUSER=user;
	}
	public void setPasswd(String passwd){
		DBPASS=passwd;
	}
	
	
	
	public static void main(String args[]){
		
		
		try{
			DatabaseUtilities du = new DatabaseUtilities();
			
			String sql="SELECT trackNo,height,xPos,yPos,trackTime FROM trackinfot_filter ORDER BY trackNo";
			
			Vector<String> tem = du.getQuery(sql);
			du.closeConnection();
		}
		catch(SQLException sqlex){
			sqlex.printStackTrace();
		}
		
		
	}
	
	
}
