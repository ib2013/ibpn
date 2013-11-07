package com.infobip.ibpn.service;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;
import com.infobip.ibpn.adapters.SourceAdapter;
import com.infobip.ibpn.adapters.SourceAdapterContainer;
import com.infobip.ibpn.models.MessageModel;

public class PushNotification {

	final String messageID = "ibnmessage";
	final String applicationID = Configuration.APPLICATION_ID;
	String notificationMessage;
	Data androidData;
	String url;
	final String sentType = "channels";
	final String mimeType = "text/html";
	ArrayList<String> channelNames = new ArrayList<String>();
	ArrayList<String> OSTypes = new ArrayList<String>();

	public PushNotification(MessageModel x, String channelName) {
		if (channelName.toUpperCase().equals(Configuration.DEFAULT_CHANNEL_NAME.toUpperCase())) {
			this.notificationMessage = x.getTitle();
		} else {
			this.notificationMessage = channelName;
		}
		this.url = x.getLink();
		OSTypes.add("Android");
		
		SourceAdapterContainer adapterContainer = new SourceAdapterContainer();
		ArrayList<SourceAdapter> adapters = adapterContainer.getAdapters();
		
		for (SourceAdapter adapter : adapters){
			if (adapter.isValid(x.getId())){
				androidData = new Data(adapter.getAdapterdescription());
				break;
			}
		}
		
		channelNames.add(channelName);
	}

	public void notifyChannel(String channelName) {
		Gson gson = new Gson();
		try {
			StringEntity parms = new StringEntity(gson.toJson(this));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(
					"https://pushapi.infobip.com/3/application/9cabf301d3db/message");
			request.addHeader("Authorization", "Basic cHVzaGRlbW86cHVzaGRlbW8=");
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			System.out.println(this.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
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
