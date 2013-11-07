package com.infobip.ibpnservice;

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

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infobip.adapters.Message;
import com.infobip.adapters.SourceAdapter;
import com.infobip.adapters.TorrentSourceAdapter;
import com.infobip.adapters.YouTubeSourceAdapter;
import com.infobip.db.DatabaseConnection;
import com.infobip.db.RssPopisModel;

public class FeedToPushService {
	Timer t;
	HashMap<ChannelModel, Date> lastFeedDates = new HashMap<ChannelModel, Date>();

	public FeedToPushService() {
		t = new Timer();
	}

	public void start() {

		t.schedule(new TimerAction(), Configuration.START_DELAY,
				Configuration.REFRESH_INTERVAL);
		
	}

	public void readRSSFeeds() {

		DatabaseConnection db = new DatabaseConnection();
		ChannelHandler channelHandler = new ChannelHandler();

		ArrayList<RssPopisModel> sourcesList = db.fetchAllRssPopisModels();
		ArrayList<Message> feedList = fetchFeedListFromSources(sourcesList);
		ArrayList<ChannelModel> channelList = channelHandler.fetchChannelList();

		for (ChannelModel channel : channelList) {
			if (!lastFeedDates.containsKey(channel)) {
				Date date = new Date();
				date.setTime(date.getTime() - 60 * 60 * 1000);
				lastFeedDates.put(channel, date);
			}
		}

		updateUsersWithNotifications(feedList, channelList);
	}

	private ArrayList<Message> fetchFeedListFromSources(
			ArrayList<RssPopisModel> sourcesList) {

		ArrayList<Message> feedList = new ArrayList<Message>();
		SourceAdapterContainer container = new SourceAdapterContainer();
		ArrayList<SourceAdapter> adapters = container.getAdapters();

		for (RssPopisModel rss : sourcesList) {
			for (SourceAdapter adapter : adapters) {
				if (adapter.isValid(rss.getIdRssSource())) {
					adapter.setUrl(rss.getRssFeed());
					feedList.addAll(adapter.getMessages());
				}
			}
		}

		return feedList;
	}

	public void updateUsersWithNotifications(ArrayList<Message> feedList,
			ArrayList<ChannelModel> channelList) {
		for (Message x : feedList) {
			for (ChannelModel y : channelList) {
				if (hasMatch(x, y)) {

					PushNotification pushN = new PushNotification(x,
							y.getName());
					pushN.notifyChannel(y.getName());

					System.out.println("=============");
				}
			}
		}
	}

	public boolean hasMatch(Message torrent, ChannelModel channel) {

		Date lastTorrentFeedDate = lastFeedDates.get(channel);
		if (lastTorrentFeedDate == null) {
			lastFeedDates.put(channel, Configuration.DEFAULT_DATE);
			lastTorrentFeedDate = Configuration.DEFAULT_DATE;
		}

		if (torrent.getDate().compareTo(lastTorrentFeedDate) <= 0)
			return false;

		if (channel.getName().toUpperCase().equals("ALL TORRENTS")) {
			lastFeedDates.put(channel, torrent.getDate());
			return true;
		}
		String[] splitString = channel.getName().split(" ");
		for (int i = 0; i < splitString.length; i++) {
			if (!torrent.getTitle().toLowerCase()
					.contains(splitString[i].toLowerCase())) {
				return false;
			}
		}

		System.out.println(torrent.toString());
		lastFeedDates.put(channel, torrent.getDate());
		return true;
	}

	class TimerAction extends TimerTask {
		public void run() {
			readRSSFeeds();
		}
	}
}
