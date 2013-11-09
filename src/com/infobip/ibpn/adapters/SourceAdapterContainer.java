package com.infobip.ibpn.adapters;

import java.util.ArrayList;

public class SourceAdapterContainer {
	ArrayList<SourceAdapter> adapters;

	public SourceAdapterContainer() {
		adapters = new ArrayList<SourceAdapter>();

		adapters.add(new YouTubeSourceAdapter());
		adapters.add(new TorrentSourceAdapter());
	}

	public ArrayList<SourceAdapter> getAdapters() {
		return adapters;
	}

}
