package main;

import rss_parser.*;
import dbmodels.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import com.google.*;

public class MainApp {
	Timer t;

	public MainApp() {
		t = new Timer();
	}

	public void start() {

		t.schedule(new TimerAction(), Configuration.startDelay,
				Configuration.refreshInterval);
	}

	public void readRSSFeeds() {
		DatabaseConnection db = new DatabaseConnection();
		ArrayList<RssPopisModel> sourcesList = db.getAllRssPopisModel();
		ArrayList<Model>  feedList = new ArrayList<Model>();
		for (RssPopisModel rss : sourcesList){
			switch(rss.getIdRssSource()){
			case 1: // The Pirate Bay
				
				TorrentAdapter torrentAdapter = new TorrentAdapter(rss.getRssFeed());
				feedList.addAll(torrentAdapter.getMessages());
				break;
			/*case 2: //neki drugi servis
			 * break;
			 */
			}
		}
		
		for (Model x : feedList){
			System.out.println(x.toString());
		}

	}

	class TimerAction extends TimerTask {
		public void run() {
			readRSSFeeds();
		}
	}
}
