package com.infobip.ibpn.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import com.infobip.ibpn.adapters.SourceAdapter;
import com.infobip.ibpn.adapters.SourceAdapterContainer;
import com.infobip.ibpn.db.DatabaseConnection;
import com.infobip.ibpn.models.ChannelModel;
import com.infobip.ibpn.models.MessageModel;
import com.infobip.ibpn.models.RssPopisModel;

public class FeedToPushService {
	Timer t;
	HashMap<ChannelModel, Date> lastFeedDates = new HashMap<ChannelModel, Date>();
	DatabaseConnection db;
	ChannelHandler channelHandler;

	public FeedToPushService() {
		db = new DatabaseConnection();
		channelHandler = new ChannelHandler();
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
		ArrayList<MessageModel> messagesList = fetchMessageModelListFromSources(sourcesList);
		ArrayList<ChannelModel> channelList = channelHandler.fetchChannelList();

		for (ChannelModel channel : channelList) {
			if (!lastFeedDates.containsKey(channel)) {
				Date date = new Date();
				date.setTime(date.getTime() - 60 * 60 * 1000);
				lastFeedDates.put(channel, date);
			}
		}

		updateUsersWithNotifications(messagesList, channelList);
	}

	private ArrayList<MessageModel> fetchMessageModelListFromSources(
			ArrayList<RssPopisModel> sourcesList) {

		ArrayList<MessageModel> messagesList = new ArrayList<MessageModel>();
		SourceAdapterContainer container = new SourceAdapterContainer();
		ArrayList<SourceAdapter> adapters = container.getAdapters();

		for (RssPopisModel rss : sourcesList) {
			for (SourceAdapter adapter : adapters) {
				if (adapter.isValid(rss.getIdRssSource())) {
					adapter.setUrl(rss.getRssFeed());
					messagesList.addAll(adapter.getMessages());
				}
			}
		}

		return messagesList;
	}

	public void updateUsersWithNotifications(
			ArrayList<MessageModel> messagesList,
			ArrayList<ChannelModel> channelList) {
		for (MessageModel x : messagesList) {
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

	public boolean hasMatch(MessageModel torrent, ChannelModel channel) {

		Date lastTorrentFeedDate = lastFeedDates.get(channel);
		if (lastTorrentFeedDate == null) {
			lastFeedDates.put(channel, Configuration.DEFAULT_DATE);
			lastTorrentFeedDate = Configuration.DEFAULT_DATE;
		}

		if (torrent.getDate().compareTo(lastTorrentFeedDate) <= 0)
			return false;

		if (channel.getName().toUpperCase()
				.equals(Configuration.DEFAULT_CHANNEL_NAME.toUpperCase())) {
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
