package rss_parser;

import java.util.ArrayList;

public interface SourceAdapter {

	public abstract ArrayList<Message> getMessages();

	public abstract boolean canIDoIt(int id);
}
