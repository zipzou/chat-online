package com.chatroom.persiststorage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBase {
	static DBInfo dbinfo = new DBInfo();
	Connection sqlConnection = null;
	public DataBase() {
		super();
	}
	public boolean openConnection()	{
		try {
			Class.forName(dbinfo.getDriver());
			sqlConnection = DriverManager.getConnection(dbinfo.getUrl(), dbinfo.getUsername(), dbinfo.getPassword());
			if (null == sqlConnection)
				return false;
			return true;
		} catch (Exception e) {
			System.err.println(e);
			return false;
		}
	}
	
	public boolean closeConnection() {
		if (null != sqlConnection){
			try {
				sqlConnection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
			return true;
		}
		return true;
	}
	public Connection getSqlConnection() {
		return sqlConnection;
	}
}
