package modal;

import java.sql.Connection;
import java.sql.DriverManager;

public class DAO {
	private Connection con;
	private String driver = "com.mysql.cj.jdbc.Driver";
	private String url = "jdbc:mysql://localhost:3306/dbjogador";
	private String user = " ";
	private String password = " ";
	
	
	public Connection conectar() {
		try {
			Class.forName(driver);
			con = DriverManager.getConnection(url, user, password);
			return con;				
			
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
		
	}
	

}
