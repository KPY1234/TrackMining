package db;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;



public class DatabaseConnection {
	
	public String DBDRIVER = "org.gjt.mm.mysql.Driver" ;	//MySQL driver
	public String DBURL = "jdbc:mysql://localhost:3306/airdefence" ;	//database connection address
	public String DBUSER = "" ;
	public String DBPASS = "";
	Connection conn = null;
	
	public DatabaseConnection(String url, String user, String passwd){
		
		DBURL = url;
		DBUSER = user;
		DBPASS = passwd;
		
		try{
			Class.forName(DBDRIVER);
		}catch(ClassNotFoundException ex){
			
			ex.printStackTrace();
		}
	}
	
	public void startConnection() throws SQLException{
		conn = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
	}
	
	public void closeConnection() throws SQLException{
		conn.close();
	}
	
	public Connection getConnection(){
		return conn;
	}
	
	
	public static void main(String args[]){
		
		DatabaseConnection dc = new DatabaseConnection("jdbc:mysql://localhost:3306/airdefence","TrackMining","icrdc4isr19+");
		try{
			dc.startConnection();
	
			dc.closeConnection();
			
		}catch(SQLException ex){
			ex.printStackTrace();
		}
		
	}
}
