package com.chatroom.servlet;

import java.io.IOException;
import java.util.Iterator;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.chatroom.controller.ChatController;
import com.chatroom.models.User;

/**
 * Servlet implementation class CloseChatServlet
 */
@WebServlet("/closechat")
public class CloseChatServlet extends JsonServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CloseChatServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String connectionData = ReadFromStream(request);
		try {
			JSONObject json = new JSONObject(connectionData);
			JSONObject fromUser = new JSONObject(json.get("from"));// 发起者
			JSONObject destUser = new JSONObject(json.get("to"));// 接收者
			User from = new User();
			from.readFromJson(fromUser);
			
			User to = new User();
			to.readFromJson(destUser);
			
			Set<ChatController> connected = ChatController.getConnectedUsers();
			Iterator<ChatController> iterator = connected.iterator();
			ChatController fromController = null;
			ChatController destController = null;
			while (iterator.hasNext()) {
				ChatController tmpController = iterator.next();
				if (tmpController.getUser().getUsername().equals(from.getUsername())) {
					fromController = tmpController;
				}
				if (tmpController.getUser().getUsername().equals(to.getUsername())) {
					destController = tmpController;
				}
				if (null != fromController && null != destController) {
					break;
				}
			}
			fromController.setChatingUser(null);
			destController.setChatingUser(null);
			// 设置聊天对象
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
