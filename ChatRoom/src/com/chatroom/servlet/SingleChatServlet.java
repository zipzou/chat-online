package com.chatroom.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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
import com.chatroom.utils.ResponseInformation;
import com.mysql.fabric.Response;

/**
 * Servlet implementation class SingleChatServlet
 */
@WebServlet("/singlechat")
public class SingleChatServlet extends JsonServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SingleChatServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		String connectionData = ReadFromStream(request);
		try {
			JSONObject json = new JSONObject(connectionData);
			JSONObject fromUser = new JSONObject(json.getString("from"));// 发起者
			JSONObject destUser = new JSONObject(json.getString("to"));// 接收者
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
			fromController.setChatingUser(to);
			destController.setChatingUser(from);
			// 设置聊天对象
			PrintWriter writer = response.getWriter();
			writer.println(ResponseInformation.getSuccessInformation());
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

}
