package rss_parser;

import java.util.Date;

public class ReadTest {
	  public static void main(String[] args) {
		    TorrentAdapter parser = new TorrentAdapter("http://rss.thepiratebay.sx/205");
		    Feed feed = parser.readFeed();
		    System.out.println(feed);
		    for (Model message : feed.getMessages()) {
		      System.out.println(message);

		    }
		  }
}