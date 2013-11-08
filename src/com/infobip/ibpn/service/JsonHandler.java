package com.infobip.ibpn.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import java.util.Iterator;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infobip.ibpn.models.ChannelModel;
import com.infobip.ibpn.models.RssPopisModel;
import com.infobip.ibpn.models.RssSourceModel;

public class JsonHandler {
	
	public RssPopisModel JSONtoRssPopisModel(JsonObject jSonObject) {
		int rssFK = Integer.parseInt(jSonObject.get("rss_fk").toString());
		String rssUrl = jSonObject.get("rss_uri").toString().replace('"', ' ');
		String rssDescription = jSonObject.get("rss_description").toString().replace('"', ' ');

		RssPopisModel model = new RssPopisModel(0, rssUrl, rssDescription, rssFK);

		return model;
	}

	public JsonArray RssPopisModelArrayListToJson(ArrayList<RssPopisModel> rssPopisModelList) {

		JsonArray jsonArray = new JsonArray();

		System.out.println(rssPopisModelList.size());

		for (RssPopisModel model : rssPopisModelList) {
			JsonObject jsonObject = new JsonObject();

			jsonObject.addProperty("id", model.getId());
			jsonObject.addProperty("Rss", model.getRssFeed());
			jsonObject.addProperty("rss_opis", model.getOpis());
			jsonObject.addProperty("fk", model.getIdRssSource());

			System.out.println(jsonObject);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	public JsonArray RssSourceModelArrayListToJson(ArrayList<RssSourceModel> rssSourceModelList) {

		JsonArray jsonArray = new JsonArray();

		System.out.println(rssSourceModelList.size());

		for (RssSourceModel model : rssSourceModelList) {
			JsonObject jsonObject = new JsonObject();

			jsonObject.addProperty("id", model.getId());
			jsonObject.addProperty("type", model.getSourceName());
			System.out.println(jsonObject);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}

	public JsonArray ChannelArrayToJson(ArrayList<ChannelModel> channelList) {

		JsonArray jsonArray = new JsonArray();

		System.out.println(channelList.size());

		for (ChannelModel Kanal : channelList) {
			JsonObject jsonObject = new JsonObject();

			jsonObject.addProperty("ime", Kanal.getName());
			jsonObject.addProperty("opis", Kanal.getDescription());

			System.out.println(jsonObject);
			jsonArray.add(jsonObject);
		}
		return jsonArray;
	}
	
	public JsonArray ChannelMapCounterToJson(HashMap<ChannelModel,Integer> channelMap){
		
		JsonArray jsonArray = new JsonArray();
		
		Iterator i = (Iterator) (channelMap.keySet().iterator());

		while(i.hasNext()) {
			
			JsonObject jsonObject = new JsonObject();
			
		    Map.Entry entry = (Map.Entry) i.next();
		    ChannelModel channel = (ChannelModel) entry.getKey();
		    Integer counter = (Integer) entry.getValue();
		    
		    jsonObject.addProperty("ime",channel.getName());
		    jsonObject.addProperty("opis", channel.getDescription());
		    jsonObject.addProperty("brojac", counter);
		    
		    jsonArray.add(jsonObject);
		}
		
		return jsonArray;
	}
}
