package com.chatroom.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.chatroom.business.UserBusiness;
import com.chatroom.controller.ChatController;
import com.chatroom.models.User;
import com.chatroom.utils.ResponseInformation;

/**
 * Servlet implementation class UserListServlet
 */
@WebServlet("/userlist")
public class UserListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserListServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Set<ChatController> usersOnline = ChatController.getConnectedUsers();
		JSONArray allUsers = new JSONArray();
		for (ChatController chatController : usersOnline) {
			User user = new UserBusiness().queryUserWithUserName(chatController.getUser().getUsername());
			if (null != user) {
				user.setPassword(null);
				allUsers.put(user.toJson());
			}
		}
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		PrintWriter writer = response.getWriter();
		try {
			writer.println(allUsers.toString());
		} catch (Exception e) {
			writer.println(ResponseInformation.getErrorInformation("系统发生异常！"));
		}finally {
			writer.close();
		}
	}

}
