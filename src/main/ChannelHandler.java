package main;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class ChannelHandler {
	public ArrayList<ChannelModel> fetchChannelList() {
		return null;
	}

	public void addChannel(ChannelModel channel) {

	}

	public void deleteChannel(ChannelModel channel) {
		HttpClient client = new DefaultHttpClient();
		HttpDelete request = new HttpDelete(
				"https://pushapi.infobip.com/1/application/"
						+ main.Configuration.AUTHORIZATION_INFO + "/channels");
		request.addHeader("Authorization",
				main.Configuration.AUTHORIZATION_INFO);
		request.addHeader("applicationID", main.Configuration.APPLICATION_ID);
		request.addHeader("channelName", channel.getName());

		try {
			HttpResponse response = client.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
