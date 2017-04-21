package com.chatroom.persiststorage;

public class DBInfo {
	private final String url = "jdbc:mysql://10.255.94.133:3306/dbchatroom?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT";
	private final String username = "frank";
	private final String password = "zouzhipeng";
	private final String driver = "com.mysql.cj.jdbc.Driver";							
	public String getUrl() {
		return url;
	}
	public String getUsername() {
		return username;
	}
	public String getPassword() {
		return password;
	}
	public String getDriver() {
		return driver;
	}
	public DBInfo() {
		super();
	}
	
}
