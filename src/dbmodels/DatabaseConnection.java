package dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {
	Connection connection;
	public ResultSet resultSet;
	public Statement statement;

	public DatabaseConnection() {
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(
					"jdbc:h2:tcp://192.168.1.178/~/test", "sa", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getRSS_POPISModel(){
		String returnValue = new String();
		
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_POPIS");
			
			while (resultSet.next()) { 
				returnValue += "Feed:" + resultSet.getString("RSS_FEED") + "\n";
            }  
		}
		catch(Exception e){
			e.printStackTrace();
		}
		
		return returnValue;
	}

}
