<%@page import="com.chatroom.models.User"%>
<%@page import="com.chatroom.servlet.RegisterServlet"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<link rel="stylesheet" type="text/css" href="asserts/css/chat-public.css">
<link rel="stylesheet" type="text/css" href="asserts/css/chat-register.css">
<link rel="stylesheet" type="text/css" href="asserts/style.css">
<title>聊天室</title>
<script src="asserts/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<script type="text/javascript" src="asserts/js/chat-ui.js"></script>
<script type="text/javascript" src="asserts/js/chat-register.js"></script>
</head>
<body>
	<div class="container">
		<div class="register-area">
			<form method="post" onsubmit="return checkInput();" class="fm-register">
				<div class="fm-group"><a class="icon-user"></a><input type="text" placeholder="用户名" name="username" id="username" required onfocus="userRefocus(this)" onblur="invalideUser(this)" autocomplete="off"><a class="username-status status-indicator icon-yes"></a></div>
				<div class="fm-group"><a class="icon-nickname"></a><input type="text" placeholder="昵称" name="nickname" id="nickname" required onfocus="nicknameRefocus(this)" onblur="invalideNickname(this)" autocomplete="off"><a class="nickname-status status-indicator icon-yes"></a></div>
				<div class="fm-group"><a class="icon-password"></a><input type="password" id="password" name="password" placeholder="密码" required onfocus="passwordRefocus(this)" onblur="invalidePassword(this)" autocomplete="off"><a class="password-status status-indicator icon-yes"></a></div>
				<div class="operate-grid">
					<input type="submit" id="register" value="立即注册" class="btn btn-primary" disabled="disabled" autocomplete="off">
					<a class="notify">*请填写每一项</a>
				</div>
			</form>
		</div>
	</div>
</body>
</html>