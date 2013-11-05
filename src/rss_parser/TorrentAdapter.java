package rss_parser;


import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class TorrentAdapter {
	static final String TITLE = "title";
	  static final String DESCRIPTION = "description";
	  static final String CHANNEL = "channel";
	  static final String LINK = "link";
	  static final String AUTHOR = "author";
	  static final String ITEM = "item";
	  static final String PUB_DATE = "pubDate";
	  static final String GUID = "guid";
	  
	  static Feed feed = null;

	  final URL url;

	  public TorrentAdapter(String feedUrl) {
	    try {
	      this.url = new URL(feedUrl);
	    } catch (MalformedURLException e) {
	      throw new RuntimeException(e);
	    }
	  }

	  public Feed readFeed() {
	    try {
	      boolean isFeedHeader = true;
	      // Set header values intial to the empty string
	      String description = "";
	      String title = "";
	      String link = "";   
	      String author = "";
	      String pubdate = "";
	      String guid = "";

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
	              feed = new Feed(title, link, description, pubdate);
	            }
	            event = eventReader.nextEvent();
	            break;
	          case TITLE:
	            title = getCharacterData(event, eventReader);
	            break;
	          case DESCRIPTION:
	            description = formatString(getCharacterData(event, eventReader));
	            break;
	          case LINK:
	            link = getCharacterData(event, eventReader);
	            break;
	          case GUID:
	            guid = getCharacterData(event, eventReader);
	            break;
	         
	          case AUTHOR:
	            author = getCharacterData(event, eventReader);
	            break;
	          case PUB_DATE:
	            pubdate = getCharacterData(event, eventReader);
	            break;
	    
	          }
	        } else if (event.isEndElement()) {
	          if (event.asEndElement().getName().getLocalPart() == (ITEM)) {
	            Model message = new Model();
	            message.setDescription(description);
	            message.setLink(guid);
	            message.setTitle(title);
	            feed.addMessage(message);
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
	  
	  private String formatString(String inString) {
		  return "New TV Show Episode!";
	  }

	  private InputStream read() {
	    try {
	      return url.openStream();
	    } catch (IOException e) {
	      throw new RuntimeException(e);
	    }
	  }
	  
	  public ArrayList<Model> getMessages() {
		  feed = this.readFeed();
		  return feed.getMessages();
	  }
	  

}
