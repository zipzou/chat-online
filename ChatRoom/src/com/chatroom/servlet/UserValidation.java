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
import com.chatroom.utils.ResponseInformation;

/**
 * Servlet implementation class UseValidation
 */
@WebServlet(name = "UseValidationServlet", urlPatterns = { "/uservalidation" })
public class UserValidation extends JsonServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UserValidation() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json;charset=UTF-8");
		String postData = ReadFromStream(request);
		try {
			JSONObject userJson = new JSONObject(postData);
			User user = new User();
			user.readFromJson(userJson);
			if (null == new UserBusiness().queryUserWithUserName(user.getUsername())) {
				PrintWriter writer = response.getWriter();
				writer.write(ResponseInformation.getSuccessInformation());
				writer.close();
				return;
			}else {
				PrintWriter writer = response.getWriter();
				writer.println(ResponseInformation.getErrorInformation("用户名已存在！"));
				writer.close();
				return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter writer = response.getWriter();
			writer.println(ResponseInformation.getErrorInformation("系统异常！"));
			writer.close();
		}
	}

}
