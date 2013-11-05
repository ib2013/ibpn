package dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import rss_parser.Model;

public class DatabaseConnection {
	Connection connection;

	public DatabaseConnection() {
		try {
			Class.forName("org.h2.Driver");
			connection = DriverManager.getConnection(
					"jdbc:h2:tcp://192.168.1.178/~/test", "sa", "");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getRSS_POPISModel() {
		String returnValue = new String();
		ResultSet resultSet;
		Statement statement;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_POPIS");

			while (resultSet.next()) {
				returnValue += "Feed:" + resultSet.getString("RSS_FEED") + "\n";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return returnValue;
	}
	
	public void insertIntoRssPopis(RssPopisModel model) {
		boolean resultSet;
		Statement statement;
		
		try {
			statement = connection.createStatement();
			resultSet = statement.execute("INSERT INTO RSS_POPIS(RSS_FEED, OPIS, FK_RSS_SOURCE) VALUES('" + model.getRssFeed() + "', '" + model.getOpis() + "', " + model.getIdRssSource() + ");");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public boolean deleteFromRssPopis(RssPopisModel model) {
		boolean result = false;
		Statement statement;
		try {
			statement = connection.createStatement();
			result = statement.execute("DELETE FROM RSS_POPIS WHERE ID=" + model.getId() +";");
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}

	public boolean insertIntoRssSource(RssSourceModel model) {
		
		boolean result=false;
		Statement statement;
		try {
			statement = connection.createStatement();
			result = statement.execute("INSERT INTO RSS_SOURCE(SOURCE_NAME) VALUES('" + model.getSourceName() + "');");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return result;
	}
	
	public boolean deleteFromRssSource(RssSourceModel model){
		boolean result=false;
		Statement statement;
		try {
			statement = connection.createStatement();
			result = statement.execute("DELETE FROM RSS_SOURCE WHERE ID="+model.getId()+";");
		} catch (Exception e) { 
			e.printStackTrace();
		}

		return result;
	}
}
