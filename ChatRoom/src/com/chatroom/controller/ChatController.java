package com.chatroom.controller;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;


import org.json.JSONException;
import org.json.JSONObject;

import com.chatroom.business.UserBusiness;
import com.chatroom.models.ChatMessage;
import com.chatroom.models.User;

@ServerEndpoint("/chatroom/chat/{username}")
public class ChatController {
	public static final String MessageTypeSystemNotify = "SystemNotify";
	public static final String MessageTypeChatMessage = "ChatMessage";
	private static Set<ChatController> connectedUsers = new CopyOnWriteArraySet<>();
	private static int onlineCount = 0;
	private Session session;
	private User user;
	private User chatingUser;// 正在私聊的用户
	public ChatController(){
		
	}
	
	public static Set<ChatController> getConnectedUsers() {
		return connectedUsers;
	}
	
	public User getUser() {
		return user;
	}
	

	public User getChatingUser() {
		return chatingUser;
	}

	public void setChatingUser(User chatingUser) {
		this.chatingUser = chatingUser;
	}

	@OnOpen
	public void onOpen(Session session, @PathParam(value="username") String username) {
		this.session = session;
		session.setMaxBinaryMessageBufferSize(5 * 1024 *1024);
		User user = new User();
		user.setUsername(username);
		this.user = new UserBusiness().queryUserWithUserName(username);
		user.setPassword(null);
		connectedUsers.add(this);
		userOnline();
		sendNotifyMessage();
		System.out.println(user.getUsername() + "上线！");
	}
	
	@OnClose
	public void onClose(){
		connectedUsers.remove(this);
		userOutline();
		sendNotifyMessage();
		System.out.println(user.getUsername() + "下线！");
		// 下线后，应该发送通知
		user = null;
	}
	
	@OnMessage
	public void onMessage(String message, Session session){
		try {
			JSONObject messageJson = new JSONObject(message);
			ChatMessage chatMessage = new ChatMessage();
			chatMessage.readFromJson(messageJson);
			if (chatMessage.getFrom() == null) {
				return;
			}
			chatMessage.setMessageType(MessageTypeChatMessage);
			chatMessage.setFromNick(user.getNickname());
			if (chatMessage.getTo() == null) { // 全局群聊消息
				for (ChatController chatController : connectedUsers) {
					if (chatController.user.getUsername().trim().equals(chatMessage.getFrom())) {
						continue;
					}
					// 发送
					if (chatMessage.getMessageContent() instanceof String) {
						try {
							String msgContent = chatMessage.toJson().toString();
							chatController.sendMessageText(msgContent);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						try {
							chatController.sendMessageStream(null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}else { // 私聊消息
				for (ChatController chatController : connectedUsers) {
					if (chatController.user.getUsername().trim().equals(chatMessage.getFrom().trim())) {
						continue;// 避免给自己发送，减少过度通信
					}
					if (!chatController.getUser().getUsername().trim().equals(chatMessage.getTo().trim())) {
						continue;
					}
					if (chatMessage.getMessageContent() instanceof String) {
						try {
							String msgContent = chatMessage.toJson().toString();
							chatController.sendMessageText(msgContent);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}else {
						try {
							chatController.sendMessageStream(null);
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
				}
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@OnMessage 
	public void onMessage(ByteBuffer buffer, Session session) {
		if (chatingUser == null) {
			// 群聊消息
			for (ChatController chatController : connectedUsers) {
				if (chatController.getUser().getUsername().trim().equals(user.getUsername().trim())) {
					continue;
				}
				try {
					chatController.sendMessageStream(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}else {
			// 私聊消息
			for (ChatController chatController : connectedUsers) {
				if (chatController.getUser().getUsername().trim().equals(user.getUsername().trim())) {
					continue;
				}
				if (chatController.getUser().getUsername().trim().equals(chatingUser.getUsername().trim())) { // 目标用户
					try {
						chatController.sendMessageStream(buffer);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				}
			}
		}
		System.out.println(buffer);
	}
	
	@OnError
	public void onError(Throwable throwalble) {
		System.out.println(throwalble.getMessage());
	}
	private synchronized void userOnline() {
		ChatController.onlineCount++;
	}
	private synchronized void userOutline() {
		ChatController.onlineCount--;
	}
	private void sendMessageText(String content) throws IOException {
		this.session.getBasicRemote().sendText(content);
	}
	private void sendMessageStream(ByteBuffer stream) throws IOException {
		this.session.getBasicRemote().sendBinary(stream);
	}
	private void sendNotifyMessage(){
		ChatMessage message = new ChatMessage();
		message.setMessageType(MessageTypeSystemNotify);
		message.setMessageContent(user.toString());
		message.setFrom("system");
		message.setTo(null);
		if (connectedUsers.size() > 0) {
			try {
				connectedUsers.iterator().next().session.getBasicRemote().sendText(message.toJson().toString());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	
}
