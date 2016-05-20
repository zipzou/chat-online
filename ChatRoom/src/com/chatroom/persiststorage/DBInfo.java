package com.chatroom.persiststorage;

public class DBInfo {
	private final String url = "jdbc:sqlserver://[urladdress];DatabaseName=[databasename]";			// 数据库连接地址
	private final String username = "[youusername]";											// 数据库用户名
	private final String password = "[yourpassword]";										// 数据库密码
	private final String driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";							// 数据库驱动
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
