package rss_parser;

import java.util.ArrayList;

public class YTAdapterTester {

	
	
	public static void main(String[] args) {
		YouTubeAdapter adapter = new YouTubeAdapter("http://gdata.youtube.com/feeds/api/standardfeeds/most_recent");
		ArrayList<Message> feedList = new ArrayList<Message>();
		
		feedList.addAll(adapter.getMessages());
		for(Message x : feedList) {
			System.out.println(x.toString());
		}
	}
	
}
