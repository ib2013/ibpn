package com.infobip.adapters;

import java.util.Date;

public class Message {

	String title;
	String description;
	String link;
	int id;
	Date date;

	public Message() {
		
	}
	
	public Message(String title, String description, String link, String source,
			String guid, int id) {
		this.title = title;
		this.description = description;
		this.link = link;
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	@Override
	public String toString() {
		return "Model [title=" + title + ", description=" + description
				+ ", link=" + link + ", id=" + id + ", date=" + date + "]";
	}

	

}
