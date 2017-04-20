package com.chatroom.business;

import com.chatroom.dbhelper.UserHelper;
import com.chatroom.models.User;

public class UserBusiness {

	public UserBusiness() {
		super();
	}
	
	public boolean authenticateUser(User user) {
		UserHelper helper = new UserHelper();
		return helper.userExists(user);
	}
	
	public boolean authenticateUser(String username, String password) {
		User user = new User();
		user.setUsername(username);
		user.setPassword(password);
		return new UserHelper().userExists(user);
	}
	
	public boolean registerUser(User user) {
		return new UserHelper().addUser(user);
	}
	
	public User queryUserWithUserName(String username) {
		Object[] obj = new UserHelper().queryUserWithName(username);
		if (obj != null && obj.length > 0) {
			if (obj[0] instanceof User) {
				return  (User)obj[0];
			}
		}
		return null;
	}
	
	public User[] queryUserWithUserNick(String nickname) {
		return new UserHelper().queryUserWithNick(nickname);
	}
	
	public String queryUserName(String username) {
		return null;
	}
	
	public String queryUserNick(String username) {
		return null;
	}
	
	public String queryUserPassword(String username) {
		return null;
	}
	
	public String queryUserSex(String username) {
		return null;
	}
}
