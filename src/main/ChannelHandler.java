package main;

import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

public class ChannelHandler {
	public ArrayList<ChannelModel> fetchChannelList(){
		return null;
	}
	
	public void addChannel(ChannelModel channel){
		
	}
	
	public void deleteChannel(ChannelModel channel){
		
	}
	
	
	public void updateChannel(ChannelModel oldModel, ChannelModel newModel) {
		
		Gson gson = new Gson();
		try {
			StringEntity parms = new StringEntity(gson.toJson(oldModel));
			
			HttpClient client = new DefaultHttpClient();
			HttpPut request = new HttpPut("https://pushapi.infobip.com/1/application/"
					+ Configuration.APPLICATION_ID
					+ "/channel/"
					+ oldModel.getName());
			request.addHeader("Authorization", Configuration.AUTHORIZATION_INFO);
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
