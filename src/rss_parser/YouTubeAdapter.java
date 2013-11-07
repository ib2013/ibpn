package rss_parser;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Characters;
import javax.xml.stream.events.XMLEvent;

public class YouTubeAdapter {
	  static final String TITLE = "title";
	  static final String ID = "id";
	  static final String LINK = "link";
	  static final String PUB_DATE = "updated";
	  
	  static Feed feed = null;
	  URL url = null;
	  
	  public YouTubeAdapter(String feedUrl) {
		  try {
			  this.url = new URL(feedUrl);
		  }
		  catch(MalformedURLException e) {
			  e.printStackTrace();
			  this.url=null;
		  }
	  }
	  
	  public Feed readFeed() {
		    try {
		      boolean isFeedHeader = true;
		      // Set header values intial to the empty string
		      String title = "";
		      String link = "";   
		      String published = "";
		      String id = "";
		      

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
		          case "entry":
			            if (isFeedHeader) {
			              isFeedHeader = false;
			              feed = new Feed("YouTube", "www.youtube.com", "YouTube Description", published);
			            }
			            event = eventReader.nextEvent();
			        break;
		          case TITLE:
		            title = getCharacterData(event, eventReader);
		            break;
		          case ID:
		        	  // <id>tag:youtube.com,2008:video:dMH0bHeiRNg</id>
		             String link1 = getCharacterData(event, eventReader);
		             if(link1.length()>42){
		              link = "http://www.youtube.com/watch?v="+link1.substring(42);
		             }
		        	
		            break;
		          case PUB_DATE:
		            published = getCharacterData(event, eventReader);
		            break;
		    
		          }
		        } else if (event.isEndElement()) {
		          if (event.asEndElement().getName().getLocalPart() == "entry") {
		        	
		            Message message = new Message();
		            message.setDescription("YouTube video");
		            message.setLink(link);
		            message.setTitle(title);
		            message.setId(2);
		            SimpleDateFormat formatter;
		            formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
		            
		            try {
		             Date date = formatter.parse(published.substring(0, 24));
		             message.setDate(date);
		            } catch (Exception e) {
		             // TODO Auto-generated catch block
		             e.printStackTrace();
		            
		            }
		            
		            
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
		  return " ";
	  }
	  
	  private InputStream read() {
		    try {
		      return url.openStream();
		    } catch (IOException e) {
		      throw new RuntimeException(e);
		    }
	  }
	  
	  public ArrayList<Message> getMessages() {
		  if(this.url!=null){
		   feed = this.readFeed();
		   return feed.getMessages();
		  }else{
			  return new ArrayList<Message>();
		  }
	  }
	  
}
