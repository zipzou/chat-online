package com.chatroom.models;

import org.json.JSONException;
import org.json.JSONObject;

import com.chatroom.interfaces.IJsonSeriserialize;

public class User implements IJsonSeriserialize {
	private String username;	// 用户名
	private String password;	// 用户密码
	private String nickname;	// 昵称
	private String sex;			// 性别
	public static final String USERNAME = "username";
	public static final String PASSWORD = "password";
	public static final String NICKNAME = "nickname";
	public static final String SEX = "sex";
	public String getUsername() {
		return username.trim();
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return null == password ? null : password.trim();
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getNickname() {
		return null == nickname ? null : nickname.trim();
	}
	public void setNickname(String nickname) {
		this.nickname = nickname;
	}
	public String getSex() {
		return null == sex ? null : sex.trim();
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public User() {
		super();
	}
	public User(String username, String password, String nickname, String sex) {
		super();
		this.username = username;
		this.password = password;
		this.nickname = nickname;
		this.sex = sex;
	}
	@Override
	public String toString() {
		return "User [username=" + username + ", password=" + password + ", nickname=" + nickname + ", sex=" + sex
				+ "]";
	}
	
	/* (non-Javadoc)
	 * @see com.chatroom.interfaces.IJsonSeriserialize#toJson()
	 */
	public JSONObject toJson() {
		JSONObject jo = new JSONObject();
		try {
			jo.put(USERNAME, username);
			jo.put(SEX, sex);
			jo.put(NICKNAME, nickname);
			return jo;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/* (non-Javadoc)
	 * @see com.chatroom.interfaces.IJsonSeriserialize#readFromJson(org.json.JSONObject)
	 */
	public void readFromJson(JSONObject json) {
		if (json.has(USERNAME)) {
			try {
				this.username = json.getString(USERNAME);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		if (json.has(NICKNAME)) {
			try {
				this.nickname = json.getString(NICKNAME);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (json.has(PASSWORD)) {
			try {
				this.password = json.getString(PASSWORD);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		if (json.has(SEX)) {
			try {
				this.sex = json.getString(SEX);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
