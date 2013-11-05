package dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class DatabaseConnection {
	Connection connection;
	
	public Connection getConnection(){
		return connection;
	}

	public DatabaseConnection() {
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(
					"jdbc:h2:tcp://192.168.1.178/~/test", "sa", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ArrayList<RssPopisModel> getAllRssPopisModel() {
		ArrayList<RssPopisModel> rezultat = new ArrayList<RssPopisModel>();

		ResultSet resultSet;
		Statement statement;

		try {

			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_POPIS");

			while (resultSet.next()) {
				RssPopisModel rssPopis = new RssPopisModel(resultSet.getInt("ID_RSS"),
						resultSet.getString("RSS_FEED"), resultSet.getString("OPIS"),
						resultSet.getInt("FK_RSS_SOURCE"));
				rezultat.add(rssPopis);
			}

			return rezultat;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public ArrayList<RssSourceModel> getAllRssSourceModel() {
		ArrayList<RssSourceModel> rezultat = new ArrayList<RssSourceModel>();

		ResultSet resultSet;
		Statement statement;

		try {

			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_SOURCE");

			while (resultSet.next()) {
				RssSourceModel rssSource = new RssSourceModel(resultSet.getInt("ID"),
						resultSet.getString("SOURCE_NAME"));
				rezultat.add(rssSource);
			}

			return rezultat;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public String getRSS_POPISModel() {

		ResultSet resultSet;
		Statement statement;

		String returnValue = new String();

		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_POPIS");

			while (resultSet.next()) {
				returnValue += "ID:" + resultSet.getInt("ID_RSS") + "\n";
				returnValue += "RSS:" + resultSet.getString("RSS_FEED") + "\n";
				returnValue += "OPIS:" + resultSet.getString("OPIS") + "\n";
				try{
					returnValue += "IZVOR:" + resultSet.getInt("FK_RSS_SOURCE") + "\n";
				}
				catch(Exception e){
					returnValue += "IZVOR: NULL";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnValue;
	}
}
