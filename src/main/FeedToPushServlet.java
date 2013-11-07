package main;

import javax.servlet.http.HttpServlet;

public class FeedToPushServlet extends HttpServlet {
	public void init(){
		FeedToPushService service = new FeedToPushService();
		service.start();
	}

}
