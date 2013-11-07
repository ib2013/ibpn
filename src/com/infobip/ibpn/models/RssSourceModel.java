package com.infobip.ibpn.models;

public class RssSourceModel {
	private int id;
	private String sourceName;

	public RssSourceModel() {

	}

	public RssSourceModel(String sourceName) {
		this.sourceName = sourceName;
	}

	public RssSourceModel(int id, String sourceName) {
		this.id = id;
		this.sourceName = sourceName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSourceName() {
		return sourceName;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	@Override
	public String toString() {
		return "RssSourceModel [id=" + id + ", sourceName=" + sourceName + "]";
	}

}
