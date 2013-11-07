package com.infobip.ibpn.adapters;

import java.util.ArrayList;

import com.infobip.ibpn.adapters.*;

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
