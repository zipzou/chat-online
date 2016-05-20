package com.chatroom.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;

public abstract class JsonServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8799894392674144146L;
	
	public String ReadFromStream(HttpServletRequest request) {
		try {
			BufferedReader bufferedReader = request.getReader();
			char []tmpbuf = new char[2 * 1024];
			StringBuffer buffer = new StringBuffer();
			while (bufferedReader.read(tmpbuf) != -1) {
				buffer.append(tmpbuf);
			}
			return buffer.toString();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
