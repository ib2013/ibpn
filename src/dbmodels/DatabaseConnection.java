package dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class DatabaseConnection {
	Connection connection = null;
	public ResultSet resultSet = null;
	public Statement statement = null;

	public DatabaseConnection() {
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(
					"jdbc:h2:tcp://192.168.1.178/~/test", "sa", "");
			statement = connection.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				statement.close();
				connection.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
