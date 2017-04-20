package com.chatroom.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.chatroom.business.UserBusiness;
import com.chatroom.models.User;
import com.chatroom.utils.MD5Utils;
import com.chatroom.utils.ResponseInformation;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/register")
public class RegisterServlet extends JsonServlet {
	private static final long serialVersionUID = 1L;
	public static final String LOGINED_USER_SESSION_ATTR = "logined_user";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer = response.getWriter();
		String postData = ReadFromStream(request);
		if (null == postData) {
			String responseStr = ResponseInformation.getErrorInformation("非法的数据请求！");
			writer.println(responseStr);
			writer.close();
			return;
		}
		try {
			JSONObject userJson = new JSONObject(postData);
			User user = new User();
			user.readFromJson(userJson);
			try {
				user.setPassword(MD5Utils.md5Encode(user.getPassword()));
				User checkedUser = new UserBusiness().queryUserWithUserName(user.getUsername());
				if (null != checkedUser) {
					// 用户名存在
					writer.println(ResponseInformation.getErrorInformation("用户名已存在"));
					writer.close();
					return;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (new UserBusiness().registerUser(user)){
				writer.println(ResponseInformation.getSuccessInformation());
				// 加session
				request.getSession().setAttribute(LOGINED_USER_SESSION_ATTR, user);
				writer.close();
				return;
			}else {
				writer.println(ResponseInformation.getErrorInformation("未知的错误"));
				writer.close();
				return;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			writer.println(ResponseInformation.getErrorInformation("系统异常错误！"));
			writer.close();
		}
	}
}
