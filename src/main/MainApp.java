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

public class MainApp {
	Timer t;

	public MainApp() {
		t = new Timer();
	}

	public void start() {

		t.schedule(new TimerAction(), 0,
				Configuration.refreshInterval);
	}

	public void readRSSFeeds() {
		DatabaseConnection db = new DatabaseConnection();
		
		try {
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	class TimerAction extends TimerTask {
		public void run() {
			readRSSFeeds();
		}
	}
}
