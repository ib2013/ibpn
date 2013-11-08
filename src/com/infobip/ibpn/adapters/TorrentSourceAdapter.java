package com.infobip.ibpn.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

import com.infobip.ibpn.models.FeedModel;
import com.infobip.ibpn.models.MessageModel;

public class TorrentSourceAdapter implements SourceAdapter {

	public String getAdapterdescription() {
		return "TPB | Novi torrent!";
	}

	static final String TITLE = "title";
	static final String DESCRIPTION = "description";
	static final String LINK = "link";
	static final String ITEM = "item";
	static final String PUB_DATE = "pubDate";
	static final String GUID = "guid";

	static FeedModel feed = null;

	URL url = null;

	public TorrentSourceAdapter() {

	}

	public TorrentSourceAdapter(String feedUrl) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private FeedModel readFeed() {
		try {
			boolean isFeedHeader = true;
			// Set header values intial to the empty string
			String description = "";
			String title = "";
			String link = "";
			String pubdate = "";
			String guid = "";

			URL url = null;

			// First create a new XMLInputFactory
			XMLInputFactory inputFactory = XMLInputFactory.newInstance();
			// Setup a new eventReader
			InputStream in = read();
			XMLEventReader eventReader = inputFactory.createXMLEventReader(in);
			// read the XML document
			while (eventReader.hasNext()) {
				XMLEvent event = eventReader.nextEvent();
				if (event.isStartElement()) {
					String localPart = event.asStartElement().getName()
							.getLocalPart();
					switch (localPart) {
					case ITEM:
						if (isFeedHeader) {
							isFeedHeader = false;
							feed = new FeedModel(title, link, description,
									pubdate);
						}
						event = eventReader.nextEvent();
						break;
					case TITLE:
						title = getCharacterData(event, eventReader);
						break;
					case DESCRIPTION:
						description = getCharacterData(event, eventReader);
						break;
					case LINK:
						link = getCharacterData(event, eventReader);
						break;
					case GUID:
						guid = getCharacterData(event, eventReader);
						break;
					case PUB_DATE:
						pubdate = getCharacterData(event, eventReader);
						break;

					}
				} else if (event.isEndElement()) {
					if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
						MessageModel message = new MessageModel();
						message.setDescription(description);
						message.setLink(guid);
						message.setTitle(title);
						message.setId(1); // id za TPB
						try {
							message.setDate(new Date(pubdate));
							feed.addMessage(message);
						}
						catch(Exception e) {
							e.printStackTrace();
						}
						event = eventReader.nextEvent();
						continue;
					}
				}
			}
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		return feed;
	}

	private String getCharacterData(XMLEvent event, XMLEventReader eventReader)
			throws XMLStreamException {
		String result = "";
		event = eventReader.nextEvent();
		if (event instanceof Characters) {
			result = event.asCharacters().getData();
		}
		return result;
	}

	private InputStream read() {
		try {
			return url.openStream();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public ArrayList<MessageModel> getMessages() {
		if (this.url != null
				&& (this.url.toString().startsWith(
						"http://rss.thepiratebay.sx/") || this.url.toString()
						.startsWith("https://rss.thepiratebay.sx/"))) {
			feed = this.readFeed();
			return feed.getMessages();
		} else {
			return new ArrayList<MessageModel>();
		}
	}

	public void setUrl(String feedUrl) {
		try {
			this.url = new URL(feedUrl);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean isValid(int id) {
		if (id == com.infobip.ibpn.service.Configuration.TPB_ID)
			return true;
		return false;
	}

}
