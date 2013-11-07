package main;

import java.util.ArrayList;

import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ChannelHandler {
	public ArrayList<ChannelModel> fetchChannelList(){
		return null;
	}
	
	public void addChannel(ChannelModel channel){
		HttpClient client = new DefaultHttpClient();
		HttpPost request = new HttpPost(
				"https://pushapi.infobip.com/1/application/9cabf301d3db/channels");
		request.addHeader("Authorization", "Basic cHVzaGRlbW86cHVzaGRlbW8=");
	}
	
	public void deleteChannel(ChannelModel channel){
		
	}
	
}
