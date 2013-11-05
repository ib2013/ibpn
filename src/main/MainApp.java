package main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.TimerTask;
import java.util.Timer;


public class MainApp {
	Timer t;
	
	/*public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.println("Pocetak");
		MainApp app = new MainApp();
		app.start();
	}*/
	
	public MainApp(){
		t = new Timer();
		//t.schedule(new TimerAction(), 500);
	}
	
	public void start(){
		t.schedule(new TimerAction(), 500, 500);
	}
	
	class TimerAction extends TimerTask{
		public void run(){
			System.out.println("Hello world!");
		}
	}
}

