package com.chatroom.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.chatroom.interfaces.IJsonSeriserialize;

public class ChatMessage implements IJsonSeriserialize{
	private String from;
	private String to;
	private Object messageContent;
	private String messageType;
	private String fromNick;
	public static final String FROM = "from";
	public static final String TO = "to";
	public static final String MESSSAGE_CONTENT = "messageContent";
	public static final String MESSAGE_TYPE = "messageType";
	public static final String FROM_NICKNAME = "fromNick";
	
	public ChatMessage() {
		super();
	}
	
	public ChatMessage(String from, String to, Object messageContent) {
		super();
		this.from = from;
		this.to = to;
		this.messageContent = messageContent;
	}

	public String getFrom() {
		return from == null ? null : from.trim();
	}
	public void setFrom(String from) {
		this.from = from;
	}
	public String getTo() {
		return to == null ? null : to.trim();
	}
	public void setTo(String to) {
		this.to = to;
	}
	public Object getMessageContent() {
		return messageContent;
	}
	public void setMessageContent(Object messageContent) {
		this.messageContent = messageContent;
	}
	
	public String getMessageType() {
		return messageType == null ? null : messageType.trim();
	}

	public void setMessageType(String messageType) {
		this.messageType = messageType;
	}
	
	public String getFromNick() {
		return null == fromNick ? null : fromNick.trim();
	}

	public void setFromNick(String fromNick) {
		this.fromNick = fromNick;
	}

	@Override
	public JSONObject toJson() {
		JSONObject json = new JSONObject();
		try {
			json.put(FROM, from);
			json.put(TO, to);
			json.put(MESSSAGE_CONTENT, messageContent);
			json.put(MESSAGE_TYPE, messageType);
			json.put(FROM_NICKNAME, fromNick);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return json;
	}
	@Override
	public void readFromJson(JSONObject json) {
		try {
			if (json.has(FROM)) {
				this.from = json.getString(FROM);
			}
			if (json.has(TO)) {
				this.to = json.getString(TO);
			}
			if (json.has(MESSSAGE_CONTENT)) {
				this.messageContent = json.getString(MESSSAGE_CONTENT);
			}
			if (json.has(MESSAGE_TYPE)) {
				this.messageType = json.getString(MESSAGE_TYPE);
			}
			if (json.has(FROM_NICKNAME)){
				this.fromNick = json.getString(FROM_NICKNAME);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
