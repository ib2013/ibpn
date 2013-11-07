package rss_parser;

import java.util.ArrayList;

public class YTAdapterTester {

	
	
	public static void main(String[] args) {
		YouTubeSourceAdapter adapter = new YouTubeSourceAdapter("http://gdata.youtube.com/feeds/api/standardfeeds/most_recent");
		ArrayList<Model> feedList = new ArrayList<Model>();
		
		feedList.addAll(adapter.getMessages());
		for(Model x : feedList) {
			System.out.println(x.toString());
		}
	}
	
}
