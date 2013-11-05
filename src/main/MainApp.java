package main;

import rss_parser.*;
import dbmodels.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimerTask;
import java.util.Timer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import com.google.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class MainApp {
	Timer t;

	public MainApp() {
			t = new Timer();
	}

	public void start() {

		t.schedule(new TimerAction(), Configuration.startDelay,
				Configuration.refreshInterval);
	}

	public void readRSSFeeds() {

		DatabaseConnection db = new DatabaseConnection();
		ArrayList<RssPopisModel> sourcesList = db.getAllRssPopisModel();
		ArrayList<Model> feedList = new ArrayList<Model>();
		for (RssPopisModel rss : sourcesList) {
			switch (rss.getIdRssSource()) {
			case 1: // The Pirate Bay

				TorrentAdapter torrentAdapter = new TorrentAdapter(
						rss.getRssFeed());
				feedList.addAll(torrentAdapter.getMessages());
				break; /*
						 * case 2: /neki drugi servis break;
						 */
			}
		}

		for (Model x : feedList) {
			System.out.println(x.toString());
		}
		
		
		try {
			// dohvatanje svih kanala u JSON formatu
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"https://pushapi.infobip.com/1/application/9cabf301d3db/channels");
			request.addHeader("Authorization", "Basic cHVzaGRlbW86cHVzaGRlbW8=");
			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String responseText = new String();
			String line;
			while ((line = rd.readLine()) != null) {
				responseText += line;
			}

			// parsiranje odgovora servera
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonTree = jsonParser.parse(responseText);
			JsonArray jsonArray = jsonTree.getAsJsonArray();

			ArrayList<String> channelList = new ArrayList<String>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonElement = jsonArray.get(i).getAsJsonObject();
				channelList.add(jsonElement.getAsJsonPrimitive("name")
						.getAsString());
			}
			

			updateUsersWithNotifications(feedList, channelList);

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public void updateUsersWithNotifications(ArrayList<Model> feedList,
			ArrayList<String> channelList) {
		for (Model x : feedList) {
			for (String y : channelList) {
				if (hasMatch(x.getTitle(), y)) {
					notifyChannel(new PushNotification(x,y), y);
				}
			}
		}
	}

	public boolean hasMatch(String torrentName, String channelName) {

		String[] splitString = channelName.split(" ");
		for (int i = 0; i < splitString.length; i++) {
			if (!torrentName.toLowerCase().contains(
					splitString[i].toLowerCase())) {
				return false;
			}
		}
		return true;
	}

	public void notifyChannel(PushNotification notif, String channelName) {
		System.out.println(notif.toString());
	}

	class TimerAction extends TimerTask {
		public void run() {
			readRSSFeeds();
		}
	}
}
