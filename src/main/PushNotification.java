package main;

import java.util.ArrayList;

import rss_parser.Model;

public class PushNotification {

	final String messageID = "fdsfsdf";
	final String applicationID = "9cabf301d3db";
	String notificationMessage;
	Data androidData;
	String url;
	final String sentType = "channels";
	final String mimeType = "text/html";
	ArrayList<String> channelNames = new ArrayList<String>();
	ArrayList<String> OSTypes = new ArrayList<String>();

	public PushNotification(Model x, String channelName) {
		this.notificationMessage = channelName;
		this.url = x.getLink();
		OSTypes.add("Android");
		androidData = new Data("TBP | Nova epizoda!");
		channelNames.add(channelName);
	}

	@Override
	public String toString() {
		return "PushNotification [messageID=" + messageID + ", applicationID="
				+ applicationID + ", notificationMessage="
				+ notificationMessage + ", androidData=" + androidData
				+ ", sentType=" + sentType + ", mimeType=" + mimeType
				+ ", channelNames=" + channelNames + ", OSTypes=" + OSTypes
				+ "]";
	}

	private class Data {
		String title;
		boolean sound = true;
		boolean vibrate = true;
		boolean light = true;

		public Data(String title) {
			this.title = title;
		}
	}

}
