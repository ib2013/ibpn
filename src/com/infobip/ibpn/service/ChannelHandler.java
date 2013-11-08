package com.infobip.ibpn.service;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.infobip.ibpn.models.ChannelModel;
import com.infobip.ibpn.models.RssPopisModel;
import com.infobip.ibpn.models.RssSourceModel;

public class ChannelHandler {
	public ArrayList<ChannelModel> fetchChannelList() {
		ArrayList<ChannelModel> channelList;
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

			channelList = parseJson(responseText);

			return channelList;

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public ArrayList<ChannelModel> parseJson(String jsonResponse) {
		ArrayList<ChannelModel> channelList = new ArrayList<ChannelModel>();

		JsonParser jsonParser = new JsonParser();
		JsonElement jsonTree = jsonParser.parse(jsonResponse);
		JsonArray jsonArray = jsonTree.getAsJsonArray();

		for (int i = 0; i < jsonArray.size(); i++) {
			JsonObject jsonElement = jsonArray.get(i).getAsJsonObject();
			String channelName;
			String channelDescription;

			try {
				channelName = jsonElement.getAsJsonPrimitive("name")
						.getAsString();
			} catch (Exception e) {
				channelName = "";
			}
			try {
				channelDescription = jsonElement.getAsJsonPrimitive(
						"description").getAsString();
			} catch (Exception e) {
				channelDescription = "";
			}

			channelList.add(new ChannelModel(channelName, channelDescription));
		}

		return channelList;
	}

	public boolean addChannel(ChannelModel channel) {
		Gson gson = new Gson();
		try {
			StringEntity parms = new StringEntity(gson.toJson(channel));
			HttpClient client = new DefaultHttpClient();
			HttpPost request = new HttpPost(
					"https://pushapi.infobip.com/1/application/"
							+ Configuration.APPLICATION_ID + "/channel");
			request.addHeader("Authorization", Configuration.AUTHORIZATION_INFO);
			request.addHeader("content-type", "application/json");
			request.setEntity(parms);
			HttpResponse response = client.execute(request);
			if (response.getStatusLine().toString()
					.contains("HTTP/1.1 201 Created")) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean deleteChannel(ChannelModel channel) {

		try {
			HttpClient client = new DefaultHttpClient();
			String channelName = channel.getName().replaceAll(" ", "%20");
			HttpDelete request = new HttpDelete(
					"https://pushapi.infobip.com/1/application/"
							+ com.infobip.ibpn.service.Configuration.APPLICATION_ID
							+ "/channel/" + channelName);
			request.addHeader("Authorization",
					com.infobip.ibpn.service.Configuration.AUTHORIZATION_INFO);
			request.addHeader("applicationID",
					com.infobip.ibpn.service.Configuration.APPLICATION_ID);
			request.addHeader("channelName", channel.getName());

			HttpResponse response = client.execute(request);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean updateChannel(ChannelModel oldModel, ChannelModel newModel) {

		Gson gson = new Gson();
		try {

			String formatSpace = oldModel.getName().replaceAll(" ", "%20");
			StringEntity parms = new StringEntity(gson.toJson(newModel));

			HttpClient client = new DefaultHttpClient();
			HttpPut request = new HttpPut(
					"https://pushapi.infobip.com/1/application/"
							+ Configuration.APPLICATION_ID + "/channel/"
							+ formatSpace);

			request.addHeader("Authorization", Configuration.AUTHORIZATION_INFO);
			request.addHeader("content-type", "application/json");

			request.setEntity(parms);
			request.addHeader("channelName", oldModel.getName());
			HttpResponse response = client.execute(request);

			if (response.getStatusLine().toString().contains("HTTP/1.1 200 OK")) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
