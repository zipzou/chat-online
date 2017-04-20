package com.chatroom.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class ResponseInformation {
	public static String getSuccessInformation(){
		JSONObject jo = new JSONObject();
		try {
			jo.put("status", "success");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
	public static String getErrorInformation(String reason) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("status", "error");
			jo.put("reason", reason);
			return jo.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jo.toString();
	}
	
	public static String getErrorInformation(Exception ex) {
		JSONObject jo = new JSONObject();
		try {
			jo.put("status", "success");
			jo.put("reason", ex.getMessage());
		} catch (Exception e) {
			e.getSuppressed();
		}
		return jo.toString();
	}
}
