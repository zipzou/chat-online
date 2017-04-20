package com.chatroom.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.chatroom.business.UserBusiness;
import com.chatroom.models.User;

/**
 * Servlet implementation class InformationServlet
 */
@WebServlet("/information")
public class InformationServlet extends JsonServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InformationServlet() {
        super();
        
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=utf-8");
		String username = request.getParameter("username");
		try {
			User resultUser = new UserBusiness().queryUserWithUserName(username);
			resultUser.setPassword(null);
			PrintWriter writer = response.getWriter();
			writer.println(resultUser.toJson().toString());
			writer.close();
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
