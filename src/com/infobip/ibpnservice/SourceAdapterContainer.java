package com.infobip.ibpnservice;

import java.util.ArrayList;

import com.infobip.adapters.*;

public class SourceAdapterContainer {
	ArrayList<SourceAdapter> adapters;

	public SourceAdapterContainer() {
		adapters = new ArrayList<SourceAdapter>();

		adapters.add(new TorrentSourceAdapter());
		adapters.add(new YouTubeSourceAdapter());
	}

	public ArrayList<SourceAdapter> getAdapters() {
		return adapters;
	}

}
