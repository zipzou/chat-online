package com.chatroom.persiststorage;

public class DBInfo {
	private final String url = "jdbc:mysql://127.0.0.1:3306/db?characterEncoding=utf-8";
	private final String username = "root";
	private final String password = "123456";
	private final String driver = "com.mysql.jdbc.Driver";							
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
