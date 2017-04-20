<%@page import="com.chatroom.servlet.LoginServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<% Object user = session.getAttribute(LoginServlet.LOGINED_USER_SESSION_ATTR);
if (null != user){
	response.sendRedirect("Chat");
}%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<link rel="stylesheet" type="text/css" href="asserts/css/chat-public.css">
<link rel="stylesheet" type="text/css" href="asserts/css/chat-login.css">
<link rel="stylesheet" type="text/css" href="asserts/style.css">
<script src="asserts/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<script type="text/javascript" src="asserts/js/chat-ui.js"></script>
<script type="text/javascript" src="asserts/js/chat-login.js">
	
</script>
<title>用户登录</title>
</head>
<body>
	<div class="container">
		<div class="login-area">
			<span class="login-title"><a class="login-title-content">请登录您的账号</a></span>
			<form method="post" onsubmit="return checkUser()" class="fm-login">
				<div class="fm-group"><a class="icon-user"></a><input type="text" placeholder="username" id="username" name="username" onfocus="inputFocus(this)" onblur="inputBlur(this)"></div>
				<div class="fm-group"><a class="icon-password"></a><input type="password" placeholder="password" id="password" name="password" onfocus="inputFocus(this)" onblur="inputBlur(this)"></div>
				<div class="bottom-content">
					<input type="submit" value="登录" class="btn btn-primary">
					<span class="link-register"><a id="register" href="javascript:;">注册账户?</a></span>
				</div>
			</form>
			<div class="linker">
				<a>Support Us?</a>
			</div>
		</div>
	</div>
</body>
</html>