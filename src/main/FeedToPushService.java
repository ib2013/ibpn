package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServlet;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import rss_parser.Model;
import rss_parser.TorrentAdapter;
import rss_parser.YouTubeSourceAdapter;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import dbmodels.DatabaseConnection;
import dbmodels.RssPopisModel;

public class FeedToPushService {
	Timer t;
	HashMap<String, Date> lastFeedDates = new HashMap<String, Date>();
	static final long serialVersionUID = 10000;

	public FeedToPushService() {
		t = new Timer();
	}

	public void start() {

		t.schedule(new TimerAction(), Configuration.START_DELAY,
				Configuration.REFRESH_INTERVAL);
	}

	public void readRSSFeeds() {

		DatabaseConnection db = new DatabaseConnection();
		ArrayList<RssPopisModel> sourcesList = db.fetchAllRssPopisModels();
		ArrayList<Model> feedList = new ArrayList<Model>();
		for (RssPopisModel rss : sourcesList) {
			switch (rss.getIdRssSource()) {
			case 1: // The Pirate Bay

				TorrentAdapter torrentAdapter = new TorrentAdapter(
						rss.getRssFeed());

				feedList.addAll(torrentAdapter.getMessages());

				break;
			case 2: // YouTube

				YouTubeSourceAdapter ytAdapter = new YouTubeSourceAdapter(rss.getRssFeed());
				feedList.addAll(ytAdapter.getMessages());
				break;
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

			for (String channelName : channelList) {
				if (!lastFeedDates.containsKey(channelName)) {
					Date date = new Date();
					date.setTime(date.getTime() - 60 * 60 * 1000);
					lastFeedDates.put(channelName, date);
				}
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
				if (hasMatch(x, y)) {
					notifyChannel(new PushNotification(x, y), y);
					System.out
							.println("--------------------------------------------------------------------");
				}
			}
		}
	}

	public boolean hasMatch(Model torrent, String channelName) {

		Date lastTorrentFeedDate = lastFeedDates.get(channelName);
		if (lastTorrentFeedDate == null) {
			lastFeedDates.put(channelName, Configuration.DEFAULT_DATE);
			lastTorrentFeedDate = Configuration.DEFAULT_DATE;
		}

		if (torrent.getDate().compareTo(lastTorrentFeedDate) <= 0)
			return false;

		if (channelName.toUpperCase().equals("ALL TORRENTS")) {
			lastFeedDates.put(channelName, torrent.getDate());
			return true;
		}
		String[] splitString = channelName.split(" ");
		for (int i = 0; i < splitString.length; i++) {
			if (!torrent.getTitle().toLowerCase()
					.contains(splitString[i].toLowerCase())) {
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