package rss_parser;

import java.util.Date;

public class ReadTest {
	  public static void main(String[] args) {
		    TorrentAdapter parser = new TorrentAdapter("http://rss.thepiratebay.sx/205");
		    Feed feed = parser.readFeed();
		    
		    // Ispisivanje informacija o feed-u
		    System.out.println(feed.getInfo());
		    
		    // Ispisivanje poruka iz feed-a
		    for (Message message : feed.getMessages()) {
		      System.out.println(message);
		    }
		    
		    // Ispisivanje poruka direktno iz TorrentAdapter-a
		    for (Message message : parser.getMessages()) {
			      System.out.println(message);
			}
		  }
	  
}