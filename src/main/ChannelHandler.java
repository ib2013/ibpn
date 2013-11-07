package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

import org.apache.http.client.methods.HttpDelete;

import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import org.apache.http.client.methods.HttpGet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChannelHandler {
	public ArrayList<ChannelModel> fetchChannelList() {
		ArrayList<ChannelModel> channelList = new ArrayList<ChannelModel>();
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"https://pushapi.infobip.com/1/application/"
							+ Configuration.APPLICATION_ID + "/channels");

			request.addHeader("Authorization", Configuration.AUTHORIZATION_INFO);
			HttpResponse response = client.execute(request);

			BufferedReader rd = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent()));

			String responseText = new String();
			String line;
			while ((line = rd.readLine()) != null) {
				responseText += line;
			}

			// parsiranje odgovora servera
			JsonParser jsonParser = new JsonParser();
			JsonElement jsonTree = jsonParser.parse(responseText);
			JsonArray jsonArray = jsonTree.getAsJsonArray();

			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonElement = jsonArray.get(i).getAsJsonObject();
				channelList.add(new ChannelModel(jsonElement
						.getAsJsonPrimitive("name").getAsString(), jsonElement
						.getAsJsonPrimitive("description").getAsString()));
			}
			return channelList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public void addChannel(ChannelModel channel) {

	}

	public void deleteChannel(ChannelModel channel) {

		try {
			HttpClient client = new DefaultHttpClient();
			String channelName = channel.getName().replaceAll(" ", "%20");
			HttpDelete request = new HttpDelete(
					"https://pushapi.infobip.com/1/application/"
							+ main.Configuration.APPLICATION_ID + "/channel/"
							+ channelName);
			request.addHeader("Authorization",
					main.Configuration.AUTHORIZATION_INFO);
			request.addHeader("applicationID",
					main.Configuration.APPLICATION_ID);
			request.addHeader("channelName", channel.getName());

			HttpResponse response = client.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void updateChannel(ChannelModel oldModel, ChannelModel newModel) {

		Gson gson = new Gson();
		try {
			StringEntity parms = new StringEntity(gson.toJson(newModel));

			HttpClient client = new DefaultHttpClient();
			HttpPut request = new HttpPut(
					"https://pushapi.infobip.com/1/application/"
							+ Configuration.APPLICATION_ID + "/channel/"
							+ oldModel.getName());
			request.addHeader("Authorization", Configuration.AUTHORIZATION_INFO);
			request.addHeader("content-type", "application/json");
			request.addHeader("channelName", oldModel.getName());
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
