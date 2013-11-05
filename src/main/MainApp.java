package main;

import rss_parser.*;
import dbmodels.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.TimerTask;
import java.util.Timer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.*;
import com.google.gson.stream.JsonReader;

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
		
	}

	class TimerAction extends TimerTask {
		public void run() {
			readRSSFeeds();
		}
	}
}
