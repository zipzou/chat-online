package com.chatroom.dbhelper;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.chatroom.models.User;
import com.chatroom.persiststorage.DataBase;
import com.chatroom.persiststorage.ExcuteQueryObject;

public class UserHelper extends ExcuteQueryObject {

	private DataBase db = new DataBase();
	
	private final String SELECT_USER_BY_ID_WITH_PASSWORD = "SELECT * FROM CHATUSER WHERE USERNAME = ? AND PASSWORD = ?";
	private final String SELECT_USER_BY_ID_WITH_PASSWORD_FROM_VIEW = "SELECT * FROM ViewUserToChat WHERE USERNAME = ? AND PASSWORD = ?";
	public boolean userExists(User user) {
		//打开数据库连接
		if (!db.openConnection()) {// 打开失败
			return false;
		}
		try (Connection conn = db.getSqlConnection()) {
			PreparedStatement queryStatement = conn.prepareStatement(SELECT_USER_BY_ID_WITH_PASSWORD);// 创建SQL语句
			String username = user.getUsername();
			String password = user.getPassword();
			Map<Object, Object>[]result = select(queryStatement, new Object[]{username, password});
			if (null != result && result.length > 0) {
				return true;
			}
			// 在本数据库中为验证到目标用户，在视图中查找
			queryStatement = conn.prepareStatement(SELECT_USER_BY_ID_WITH_PASSWORD_FROM_VIEW);
			result = select(queryStatement, new Object[]{username, password});
			if (null == result || result.length <= 0) {
				return false;
			}
			return true;
		}catch (NullPointerException ex) {
			return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		} finally {
			db.closeConnection();
		}
	}
	
	private final String DELETE_USER = "DELETE FROM CHATUSER WHERE USERNAME = ?";
	public boolean deleteUserExist(User user) {
		if (!db.openConnection()){
			return false;
		}
		try (Connection conn = db.getSqlConnection()){
			PreparedStatement deleteStatement = conn.prepareStatement(DELETE_USER);
			if (-1 == delete(deleteStatement, new Object[]{user.getUsername()})) {
				return false;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			db.closeConnection();
		}
	}
	
	private final String UPDATE_USER = "UPDATE CHATUSER SET USERNAME = ?, PASSWORD = ?, NICKNAME = ?, SEX = ? WHERE USERNAME = ?";
	public boolean updateUser(User oldUser, User newUser) {
		if (!db.openConnection()) {
			return false;
		}
		try (Connection conn = db.getSqlConnection()){
			PreparedStatement updateStatement = conn.prepareStatement(UPDATE_USER);
			return this.update(updateStatement, new Object[]{oldUser.getUsername()}, newUser);
		}catch(Exception ex){ 
			ex.printStackTrace();
			return false;
		}finally {
			db.closeConnection();
		}
	}
	
	
	private final String SELECT_USER_BY_USERNAME = "SELECT * FROM CHATUSER WHERE USERNAME = ?";
	private final String SELECT_USER_BY_USERNAME_FROM_VIEW = "SELECT * FROM VIEWUSERTOCHAT WHERE USERNAME = ?";
	public User[] queryUserWithName(String username) {
		if (!db.openConnection()) {
			return null;
		}
		try (Connection conn = db.getSqlConnection()){
			PreparedStatement selectStatement = conn.prepareStatement(SELECT_USER_BY_USERNAME);
			Map<Object, Object>[] result = this.select(selectStatement, new Object[]{username});
			if (null == result || result.length <= 0) {
				selectStatement = conn.prepareStatement(SELECT_USER_BY_USERNAME_FROM_VIEW);// 从视图查找
				result = this.select(selectStatement, new Object[]{username});
				if (result == null || result.length <= 0)
					return null;
			}
			List<User> users = new LinkedList<>();
			for (int i = 0; i < result.length; i++) {
				User user = new User();
				user.setUsername(result[i].get(0).toString());
				user.setPassword(null);
				user.setNickname(result[i].get(2).toString());
				user.setSex(result[i].get(3).toString());
				users.add(user);
			}
			return users.toArray(new User[]{});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			db.closeConnection();
		}
	}
	
	private final String SELECT_USER_BY_NICKNAME = "SELECT * FROM CHATUSER WHERE NICKNAME = ?";
	public User[] queryUserWithNick(String nickname) {
		if (!db.openConnection()) {
			return null;
		}
		try (Connection conn = db.getSqlConnection()){
			PreparedStatement selectStatement = conn.prepareStatement(SELECT_USER_BY_NICKNAME);
			Map<Object, Object>[] result = this.select(selectStatement, new Object[]{nickname});
			if (null == result) {
				return null;
			}
			List<User> users = new LinkedList<>();
			for (int i = 0; i < result.length; i++) {
				JSONObject jo = new JSONObject(result[i]);
				User user = new User();
				user.readFromJson(jo);
				users.add(user);
			}
			return users.toArray(new User[]{});
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			db.closeConnection();
		}
	}

	private final String INSERT_USER = "INSERT INTO CHATUSER VALUES(?, ?, ?, ?)";
	public boolean addUser(User user) {
		if (!db.openConnection()) {
			return false;
		}
		try (Connection conn = db.getSqlConnection()) {
			PreparedStatement insertStatement = conn.prepareStatement(INSERT_USER);
			return this.insert(insertStatement, user);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}finally {
			db.closeConnection();
		}
	}
}
