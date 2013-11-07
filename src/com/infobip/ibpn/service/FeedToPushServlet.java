package com.infobip.ibpn.service;

import javax.servlet.http.HttpServlet;

public class FeedToPushServlet extends HttpServlet {

	private static final long serialVersionUID = 828338427739767088L;

	public void init() {
		FeedToPushService service = new FeedToPushService();
		service.start();
	}

}
