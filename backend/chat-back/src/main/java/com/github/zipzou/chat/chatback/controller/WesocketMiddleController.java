package com.github.zipzou.chat.chatback.controller;

import java.io.IOException;
import java.util.List;

import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.alibaba.fastjson.JSON;
import com.github.zipzou.chat.chatback.dao.bean.MessageType;
import com.github.zipzou.chat.chatback.service.MessageService;
import com.github.zipzou.chat.chatback.service.SessionService;
import com.github.zipzou.chat.chatback.service.WsSessionSupport;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.req.TextMessageVo;
import com.github.zipzou.chat.chatback.vo.res.AppStatus;
import com.github.zipzou.chat.chatback.vo.res.MessageToSendVo;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;

import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
@ServerEndpoint(value = "/chat/{uuid}/{accessToken}")
public class WesocketMiddleController {

  private static UserController userController;
  private static MessageService msgSender;
  private static SessionService sessServ;

  public static void setMsgSender(MessageService msgSender) {
    WesocketMiddleController.msgSender = msgSender;
  }

  public static void setUserController(UserController userController) {
    WesocketMiddleController.userController = userController;
  }

  public static void setSessServ(SessionService sessServ) {
    WesocketMiddleController.sessServ = sessServ;
  }

  /**
   * 打开websocket连接
   * 
   * @param sess     当前会话
   * @param userUUID 用户对应的UUID
   * @param ak       用户使用的会话令牌
   */
  @OnOpen
  public void openConnection(Session sess, @PathParam("uuid") String userUUID, @PathParam("accessToken") String ak) {
    log.info("用户连接");
    UserBasicVo user = new UserBasicVo();
    user.setAccessToken(ak);
    user.setUserUUID(userUUID);
    ValueObject statusVal = userController.checkStatus(user);
    ResponseVo<Integer> status = (ResponseVo<Integer>) statusVal;
    if (200 == status.getCode() && status.isSuccess() && AppStatus.Ready.code() == status.getData()) {
      WsSessionSupport wsPool = WsSessionSupport.sharedInstance();
      wsPool.setSession(userUUID, sess);
      ValueObject userRes = sessServ.getAttribute(ak, userUUID);
      if (null == userRes || 200 != ((ResponseVo) userRes).getCode()) {
        log.warn("无法获取当前用户信息，请检查登录状态", userUUID + ": " + ak);
        try {
          sess.close();
        } catch (IOException e) {
          log.trace("关闭连接异常", e);
        }
        return;
      }
      String username = ((ResponseVo<String>) userRes).getData();
      wsPool.setUsername(userUUID, username);
      msgSender.online(WsSessionSupport.sharedInstance().getUUID(sess));

      UserBasicVo userParam = new UserBasicVo();
      userParam.setAccessToken(ak);
      userParam.setUserUUID(userUUID);
      ValueObject onlineUsers = userController.onlineUsers(userParam);
      if (null != onlineUsers && onlineUsers instanceof ResponseVo && 200 == ((ResponseVo) onlineUsers).getCode()) {
        MessageToSendVo<List<UserBasicVo>> onlineUserMsg = new MessageToSendVo<List<UserBasicVo>>();
        onlineUserMsg.setContent("在线列表");
        onlineUserMsg.setCreateDate(DateTime.now().toDate());
        onlineUserMsg.setFrom(MessageService.USER_SYSTEM);
        onlineUserMsg.setType(MessageType.OnlineList.code());
        onlineUserMsg.setPayload(((ResponseVo<List<UserBasicVo>>) onlineUsers).getData());
        if (sess.isOpen()) {
          sess.getAsyncRemote().sendText(JSON.toJSONString(onlineUserMsg));
        }
      }

    } else {
      log.info("当前用户并未登录，无权限访问");
      try {
        sess.close();
      } catch (IOException e) {
        log.trace(e);
      }
    }
  }

  @OnMessage
  public void recievedMessage(String messageContent) {
    try {
      TextMessageVo messageBody = JSON.parseObject(messageContent, TextMessageVo.class);
      if (messageBody.getType() == MessageType.ReadStatus.code()) {
        msgSender.readMessage(messageBody.getId());
      }
      if (messageBody.getTo() == null) {
        log.warn("未知的发送目标", messageBody);
      }
      msgSender.send(messageBody);
    } catch (Exception ex) {
      // 无法转换
      log.warn(ex.getMessage(), ex);
      // TODO: 是否为二进制数据
    }
  }

  @OnClose
  public void disConnect(Session sess) {
    // TODO: 告知其他用户，下线消息
    msgSender.offline(WsSessionSupport.sharedInstance().getUUID(sess));
    WsSessionSupport.sharedInstance().removeSession(sess);
  }
}
