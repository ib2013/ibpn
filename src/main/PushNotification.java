package main;

import rss_parser.Model;

public class PushNotification {
	
	String title;
	String payload;
	String link;

	public PushNotification(Model x, String channelName) {
		this.title = "TBP | Nova epizoda: " + channelName + "!";
		this.payload = x.getTitle();
		this.link = x.getLink();
	}
	
	@Override
	public String toString() {
		return "PushNotification [title=" + title + ", payload=" + payload
				+ ", link=" + link + "]";
	}

}
