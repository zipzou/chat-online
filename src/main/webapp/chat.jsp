<%@page import="com.chatroom.servlet.RegisterServlet"%>
<%@page import="com.chatroom.models.User"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
Object sessionObj = request.getSession().getAttribute(RegisterServlet.LOGINED_USER_SESSION_ATTR);
User loginedUser = null;
if (null != sessionObj) {
	if (sessionObj instanceof User)
		loginedUser = (User)sessionObj;
}else {
	// 重定向
	request.getRequestDispatcher("login.jsp").forward(request, response);
}
%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0" name="viewport">
<link rel="stylesheet" type="text/css" href="asserts/css/chat-public.css">
<link rel="stylesheet" type="text/css" href="asserts/css/chat-main.css">
<link rel="stylesheet" type="text/css" href="asserts/style.css">
<script src="asserts/js/jquery-2.1.4.min.js" type="text/javascript"></script>
<script type="text/javascript">
var loginedUsername = '<%=loginedUser.getUsername()%>';
var loginedUsernick = '<%=loginedUser.getNickname()%>'
var ip = '<%=request.getLocalAddr()%>'
var context = encodeURIComponent('chat-online-maven')
var url = 'ws://' + ip + ':8080/' + context + '/chatroom/chat/' + loginedUsername;
function checkLeave() {
	return  '确定离开当前页面吗？您的连接可能暂时被关闭，我们将自动为您备份聊天记录！'
}
</script>
<script type="text/javascript" src="asserts/js/chat-websocket.js"></script>
<title><%=loginedUser.getNickname() %>, 欢迎进入聊天室</title>
</head>
<body onbeforeunload='return false'> <!-- onload="initConnection()" onbeforeunload="leavePage()" -->
    
	<div class="container">
		<div class="header-nav">
			<a id="chat-title">群聊中...</a>
		</div>
		<div class="content-top">
			<span class="top-grid"><a class="chat-add"><i class="icon-plus"></i></a><a class="chat-title icon-singlechat" id="switch-list"></a><a class="chat-close" id="logout"><i class="icon-delete"></i></a></span>
			<div class="online-list hide-list">
				<div class="panel-nav">
                    <a class="opt-multichat icon-multichat" id="multichat" title="转到群聊"></a>
					<a>在线列表</a>
					<a class="opt-close icon-cancel" href="javascript:;" id="opt-close" title="关闭"></a>
				</div>
				<div class="contact-list">
					<ul class="users-list">
						
					</ul>
				</div>
			</div>
		</div>
		<div class="content-message">
            <ul class="messages-list">
<!--                http://img4.duitang.com/uploads/item/201509/11/20150911230535_iKVzu.thumb.224_0.jpeg-->
            </ul>
            <a class="bottom-anchors"></a>
		</div>
		<div class="binary-source hide-source">
            <ul>
                <!-- <li id="record"><a class="icon-enableaudio"></a></li> -->
                <li id="image"><a class="icon-image"></a></li>
            </ul>
        </div>
		<div class="message-operate">
			<form method="post" onsubmit="return sendMessage();" class="fm-message">
				<input type="text" id="message-content" class="message-input" placeholder="输入消息内容">
                <input type="submit" hidden="hidden">
			</form>
            <input type="file" hidden="hidden" id="image-input">
			<ul>
				<li  id="send-message"><a class="icon-yes"></a></li>
				<li id="other-message"><a class="icon-up"></a></li>
			</ul>
		</div>
	</div>
</body>
</html>