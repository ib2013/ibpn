package com.infobip.ibpn.service;

import java.util.ArrayList;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.infobip.ibpn.models.ChannelModel;
import com.infobip.ibpn.models.RssPopisModel;
import com.infobip.ibpn.models.RssSourceModel;

public class JsonHandler {
	public RssPopisModel JSONtoRssPopisModel(JsonObject parse) {
		int RSSfk = Integer.parseInt(parse.get("rss_fk").toString());
		String RSSuri = parse.get("rss_uri").toString().replace('"', ' ');
		String RSSdesc = parse.get("rss_description").toString()
				.replace('"', ' ');

		RssPopisModel model = new RssPopisModel(0, RSSuri, RSSdesc, RSSfk);

		return model;
	}

	public JsonArray RssPopisModelArrayListToJson(
			ArrayList<RssPopisModel> modelToJSON) {

		JsonArray vrati = new JsonArray();

		System.out.println(modelToJSON.size());

		for (RssPopisModel model : modelToJSON) {
			JsonObject objekt = new JsonObject();

			objekt.addProperty("id", model.getId());
			objekt.addProperty("Rss", model.getRssFeed());
			objekt.addProperty("rss_opis", model.getOpis());
			objekt.addProperty("fk", model.getIdRssSource());

			System.out.println(objekt);
			vrati.add(objekt);
		}

		return vrati;
	}

	public JsonArray RssSourceModelArrayListToJson(
			ArrayList<RssSourceModel> modelToJSON) {

		JsonArray vrati = new JsonArray();

		System.out.println(modelToJSON.size());

		for (RssSourceModel model : modelToJSON) {
			JsonObject objekt = new JsonObject();

			objekt.addProperty("id", model.getId());
			objekt.addProperty("type", model.getSourceName());
			System.out.println(objekt);
			vrati.add(objekt);
		}

		return vrati;
	}

	public JsonArray ChannelArrayToJson(ArrayList<ChannelModel> _Kanali) {

		JsonArray vrati = new JsonArray();

		System.out.println(_Kanali.size());

		for (ChannelModel Kanal : _Kanali) {
			JsonObject objekt = new JsonObject();

			objekt.addProperty("ime", Kanal.getName());
			objekt.addProperty("opis", Kanal.getDescription());

			System.out.println(objekt);
			vrati.add(objekt);
		}

		return vrati;
	}
}
