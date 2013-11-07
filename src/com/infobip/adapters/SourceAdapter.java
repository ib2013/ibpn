package com.infobip.adapters;

import java.util.ArrayList;

public interface SourceAdapter {

	public abstract ArrayList<Message> getMessages();

	public abstract boolean isValid(int id);

	public abstract void setUrl(String url);
}
