package com.infobip.ibpn.service;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.JsonArray;
import com.infobip.ibpn.models.ChannelModel;

public class FeedToPushServlet extends HttpServlet {

	private static final long serialVersionUID = 828338427739767088L;
	public FeedToPushService service;

	public void init() {
		service = new FeedToPushService();
		service.start();
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		JsonHandler jsonHandler = new JsonHandler();

		response.setContentType("text/plain");
		response.setCharacterEncoding("UTF-8");

		HashMap<ChannelModel, Integer> channelMap = service
				.fetchChannelListCounter();

		JsonArray Jarray = jsonHandler.ChannelMapCounterToJson(channelMap);

		response.getWriter().write(Jarray.toString());

	}

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		ChannelModel channel = new ChannelModel(request.getParameter(
				"channel_name").toString(), null);

		ChannelHandler channelHandler = new ChannelHandler();
		channelHandler.deleteChannel(channel);
		service.deleteChannelFromMap(channel);

		PushNotification pushNotification = new PushNotification();
		pushNotification.broadcastDeletedChannel(channel.getName());
		
		response.getWriter().write("success");
	}

}
