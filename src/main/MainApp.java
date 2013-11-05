package main;

import rss_parser.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;

public class MainApp {
	Timer t;
	
	public MainApp(){
		t = new Timer();
	}
	
	public void start(){
		
		t.schedule(new TimerAction(), Configuration.refreshInterval, Configuration.refreshInterval);
	}
	
	public void readRSSFeeds(){
		TorrentAdapter torrentAdapter = new TorrentAdapter("http://rss.thepiratebay.sx/205");
		ArrayList<Model> models = torrentAdapter.getMessages();
		
		for (Model message : models){
			System.out.println(message.toString());
		}
	}
	
	class TimerAction extends TimerTask{
		public void run(){
			readRSSFeeds();
		}
	}
}

