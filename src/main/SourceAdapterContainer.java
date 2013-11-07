package main;
import java.util.ArrayList;

import rss_parser.*;

public class SourceAdapterContainer {
	ArrayList<SourceAdapter> adapters;
	
	public SourceAdapterContainer(){
		adapters = new ArrayList<SourceAdapter>();
		
		adapters.add(new TorrentSourceAdapter());
		adapters.add(new YouTubeSourceAdapter());
	}
	
	public ArrayList<SourceAdapter> getAdapters(){
		return adapters;
	}

}
