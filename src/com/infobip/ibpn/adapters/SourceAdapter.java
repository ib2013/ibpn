package com.infobip.ibpn.adapters;

import java.util.ArrayList;

import com.infobip.ibpn.models.MessageModel;

public interface SourceAdapter {

	public abstract ArrayList<MessageModel> getMessages();

	public abstract boolean isValid(int id);

	public abstract void setUrl(String url);
}
