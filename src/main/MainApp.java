package main;

import rss_parser.*;
import dbmodels.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimerTask;
import java.util.Timer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class MainApp {
	Timer t;
	Date lastFeedDate;
	HashMap<String, Date> lastFeedDates = new HashMap<String, Date>();

	public MainApp() {
		t = new Timer();
		lastFeedDate = new Date(91,11,21);
	}

	public void start() {

		t.schedule(new TimerAction(), Configuration.startDelay,
				Configuration.refreshInterval);
	}

	public void readRSSFeeds() {
		Date maxDate = lastFeedDate;
		
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
			 //System.out.println(x.toString());
		}

		try {
			// dohvatanje svih kanala u JSON formatu
			@SuppressWarnings("deprecation")
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

			
			for (String channelName : channelList){
				if (!lastFeedDates.containsKey(channelName)){
					lastFeedDates.put(channelName, new Date());
				}
			}


			updateUsersWithNotifications(feedList, channelList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		lastFeedDate = maxDate;
		System.out.println("--------------------------------------------------------------------");

	}

	public void updateUsersWithNotifications(ArrayList<Model> feedList,
			ArrayList<String> channelList) {
		for (Model x : feedList) {
			for (String y : channelList) {
				if (hasMatch(x, y)) {
					notifyChannel(new PushNotification(x, y), y);
				}
			}
		}
	}

	public boolean hasMatch(Model torrent, String channelName) {
		
		Date lastTorrentFeedDate = lastFeedDates.get(channelName);
		if (lastTorrentFeedDate == null){
			lastFeedDates.put(channelName, Configuration.defaultDate);
			lastTorrentFeedDate = Configuration.defaultDate;
		}
		
		if (torrent.getDate().compareTo(lastTorrentFeedDate) <= 0) return false;
		
		if (channelName.toUpperCase().equals("ALL TORRENTS")){
			lastFeedDates.put(channelName, torrent.getDate());
			return true;
		}
		String[] splitString = channelName.split(" ");
		for (int i = 0; i < splitString.length; i++) {
			if (!torrent.getTitle().toLowerCase().contains(
					splitString[i].toLowerCase())) {
				return false;
			}
		}
		
		System.out.println(torrent.toString());
		lastFeedDates.put(channelName, torrent.getDate());
		return true;
	}

	public void notifyChannel(PushNotification notif, String channelName) {

		Gson gson = new Gson();
		try {
			StringEntity parms = new StringEntity(gson.toJson(notif));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(
					"https://pushapi.infobip.com/3/application/9cabf301d3db/message");
			request.addHeader("Authorization", "Basic cHVzaGRlbW86cHVzaGRlbW8=");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			System.out.println(notif.toString());
			//System.out.println(response.toString());
			//System.out.println(gson.toJson(notif));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class TimerAction extends TimerTask {
		public void run() {
			readRSSFeeds();
		}
	}
}
