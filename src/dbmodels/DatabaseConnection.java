package dbmodels;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import rss_parser.Model;

public class DatabaseConnection {
	Connection connection;

	public Connection getConnection() {
		return connection;
	}

	public void connect() throws ClassNotFoundException, SQLException {
		Class.forName("org.h2.Driver");
		connection = DriverManager.getConnection(
				"jdbc:h2:tcp://192.168.1.178/~/test", "sa", "");
	}

	public DatabaseConnection() {
	}

	public ArrayList<RssPopisModel> getAllRssPopisModel() {
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			connect();
			ArrayList<RssPopisModel> rezultat = new ArrayList<RssPopisModel>();

			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_POPIS");

			while (resultSet.next()) {
				RssPopisModel rssPopis = new RssPopisModel(
						resultSet.getInt("ID_RSS"),
						resultSet.getString("RSS_FEED"),
						resultSet.getString("OPIS"),
						resultSet.getInt("FK_RSS_SOURCE"));
				rezultat.add(rssPopis);
			}

			return rezultat;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			try {
				connection.close();
				resultSet.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	@SuppressWarnings("finally")
	public ArrayList<RssSourceModel> getAllRssSourceModel() {
		ArrayList<RssSourceModel> rezultat = new ArrayList<RssSourceModel>();

		ResultSet resultSet = null;
		Statement statement = null;

		try {
			connect();

			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_SOURCE");

			while (resultSet.next()) {
				RssSourceModel rssSource = new RssSourceModel(
						resultSet.getInt("ID"),
						resultSet.getString("SOURCE_NAME"));
				rezultat.add(rssSource);
			}
		} catch (Exception e) {
			e.printStackTrace();
			rezultat = null;
		} finally {
			try {
				connection.close();
				resultSet.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return rezultat;
		}
	}

	@SuppressWarnings("finally")
	public String getRSS_POPISModel() {
		String returnValue = new String();
		ResultSet resultSet = null;
		Statement statement = null;
		try {
			connect();

			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM RSS_POPIS");

			while (resultSet.next()) {
				returnValue += "ID:" + resultSet.getInt("ID_RSS") + "\n";
				returnValue += "RSS:" + resultSet.getString("RSS_FEED") + "\n";
				returnValue += "OPIS:" + resultSet.getString("OPIS") + "\n";
				try {
					returnValue += "IZVOR:" + resultSet.getInt("FK_RSS_SOURCE")
							+ "\n";
				} catch (Exception e) {
					returnValue += "IZVOR: NULL";
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				resultSet.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return returnValue;
		}
	}

	public boolean insertIntoRssPopis(RssPopisModel model) {
		boolean result = false;
		Statement statement = null;
		try {
			connect();

			statement = connection.createStatement();
			result = statement
					.execute("INSERT INTO RSS_POPIS(RSS_FEED, OPIS, FK_RSS_SOURCE) VALUES('"
							+ model.getRssFeed()
							+ "', '"
							+ model.getOpis()
							+ "', " + model.getIdRssSource() + ");");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@SuppressWarnings("finally")
	public boolean deleteFromRssPopis(RssPopisModel model) {
		boolean result = false;
		Statement statement = null;
		try {
			connect();

			statement = connection.createStatement();
			result = statement.execute("DELETE FROM RSS_POPIS WHERE ID="
					+ model.getId() + ";");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	@SuppressWarnings("finally")
	public boolean insertIntoRssSource(RssSourceModel model) {

		boolean result = false;
		Statement statement = null;
		try {

			connect();
			statement = connection.createStatement();
			result = statement
					.execute("INSERT INTO RSS_SOURCE(SOURCE_NAME) VALUES('"
							+ model.getSourceName() + "');");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}
	}

	@SuppressWarnings("finally")
	public boolean deleteFromRssSource(RssSourceModel model) {
		boolean result = false;
		Statement statement = null;
		try {
			statement = connection.createStatement();
			result = statement.execute("DELETE FROM RSS_SOURCE WHERE ID="
					+ model.getId() + ";");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				connection.close();
				statement.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
			return result;
		}

	}
}
