package dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import rss_parser.Model;

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
		String returnValue = new String();
		ResultSet resultSet;
		Statement statement;

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
