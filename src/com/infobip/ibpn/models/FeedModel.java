package com.infobip.ibpn.models;

import java.util.ArrayList;

public class FeedModel {

	final String title;
	final String link;
	final String description;
	final String pubDate;

	final ArrayList<MessageModel> entries = new ArrayList<MessageModel>();

	public FeedModel(String title, String link, String description, String pubDate) {
		this.title = title;
		this.link = link;
		this.description = description;
		this.pubDate = pubDate;
	}

	public ArrayList<MessageModel> getMessages() {
		return entries;
	}

	public void addMessage(MessageModel model) {
		entries.add(model);
	}

	public String getTitle() {
		return title;
	}

	public String getLink() {
		return link;
	}

	public String getDescription() {
		return description;
	}

	public String getPubDate() {
		return pubDate;
	}

	@Override
	public String toString() {
		return "Feed [title=" + title + ", link=" + link + ", description="
				+ description + ", pubDate=" + pubDate + ", entries=" + entries
				+ "]";
	}

	public String getInfo() {
		return this.toString();
	}
}
