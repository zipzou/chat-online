package com.chatroom.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.chatroom.business.UserBusiness;
import com.chatroom.models.User;
import com.chatroom.persiststorage.ExcuteQueryObject;
import com.chatroom.utils.MD5Utils;
import com.chatroom.utils.ResponseInformation;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/login")
public class LoginServlet extends JsonServlet {
	private static final long serialVersionUID = 1L;
	public static final String LOGINED_USER_SESSION_ATTR = "logined_user";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		//OutputStream output = response.getOutputStream();
		String loginData = ReadFromStream(request);
		try {
			JSONObject loginUserJson = new JSONObject(loginData);
			User user = new User();
			user.readFromJson(loginUserJson);
			user.setPassword(MD5Utils.md5Encode(user.getPassword()));
			if (new UserBusiness().authenticateUser(user)){
				//output.write(ResponseInformation.getSuccessInformation().getBytes(Charset.forName("utf-8")));
				//加SESSION
				PrintWriter writer = response.getWriter();
				writer.println(ResponseInformation.getSuccessInformation());
				writer.close();
				User loginedUser = new UserBusiness().queryUserWithUserName(user.getUsername());
				System.out.println(loginedUser.getUsername());
				request.getSession().setAttribute(LOGINED_USER_SESSION_ATTR, loginedUser);
			}else {
				PrintWriter writer = response.getWriter();
				writer.println(ResponseInformation.getErrorInformation("用户名或密码错误！"));
				writer.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
