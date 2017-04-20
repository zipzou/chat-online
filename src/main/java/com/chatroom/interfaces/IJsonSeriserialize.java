package com.chatroom.interfaces;

import org.json.JSONObject;

public interface IJsonSeriserialize {
	public JSONObject toJson();
	public void readFromJson(JSONObject json);
}
