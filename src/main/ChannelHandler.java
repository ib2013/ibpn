package main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ChannelHandler {
	public ArrayList<ChannelModel> fetchChannelList() {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet request = new HttpGet(
					"https://pushapi.infobip.com/1/application/" + Configuration.APPLICATION_ID + "9cabf301d3db/channels");
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

			ArrayList<String> channelList = new ArrayList<String>();
			for (int i = 0; i < jsonArray.size(); i++) {
				JsonObject jsonElement = jsonArray.get(i).getAsJsonObject();
				channelList.add(jsonElement.getAsJsonPrimitive("name")
						.getAsString());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void addChannel(ChannelModel channel) {

	}

	public void deleteChannel(ChannelModel channel) {

	}

}
