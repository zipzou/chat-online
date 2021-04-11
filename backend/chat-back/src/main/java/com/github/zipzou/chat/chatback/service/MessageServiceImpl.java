package com.github.zipzou.chat.chatback.service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.websocket.Session;

import com.alibaba.fastjson.JSON;
import com.github.zipzou.chat.chatback.beans.mapper.TextMessageMap;
import com.github.zipzou.chat.chatback.dao.bean.MessageType;
import com.github.zipzou.chat.chatback.dao.bean.SimpleMessageDto;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.req.TextMessageVo;
import com.github.zipzou.chat.chatback.vo.res.EmptyVo;
import com.github.zipzou.chat.chatback.vo.res.MessageToSendVo;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

/**
 * 消息转发具体服务
 */
@Service
@Log4j2
public class MessageServiceImpl implements MessageService {

  @Deprecated
  private static final int MSG_TYPE_OFFLINE = 402;
  @Deprecated
  private static final int MSG_TYPE_ONLINE = 401;
  @Autowired
  private TextMessageMap msgMap;

  private @Autowired SessionService sessServ;

  private @Autowired MessagePersisitService msgPsersister;

  private @Autowired UserService userServ;

  @Override
  public void send(TextMessageVo msg) {
    String destSessId = WsSessionSupport.sharedInstance().getSessId(msg.getTo());
    if (null != destSessId) {
      Session sess = WsSessionSupport.sharedInstance().getSession(msg.getFrom());
      // 将简单消息数据包装
      ValueObject fromUserSess = sessServ.getAttribute(msg.getAccessToken(), msg.getFrom());

      if (null == fromUserSess || 200 != ((ResponseVo) fromUserSess).getCode()) {
        log.warn("无法获取到指定用户对应的会话，消息被忽略");
        return;
      }

      String fromUser = ((ResponseVo<String>) fromUserSess).getData();
      String toUser = WsSessionSupport.sharedInstance().getUsername(msg.getTo());
      if (null == toUser) {
        log.warn("无法获取到目标用户对应的会话，消息被忽略");
        return;
      }

      MessageToSendVo<EmptyVo> msgToSend = new MessageToSendVo<EmptyVo>();
      msgToSend.setId(DateTime.now().getMillis());
      msgToSend.setFrom(fromUser);
      msgToSend.setTo(toUser);
      msgToSend.setCreateDate(DateTime.now().toDate());
      msgToSend.setReadStatus(false);
      msgToSend.setContent(msg.getContent());

      SimpleMessageDto msgDto = msgMap.from(msgToSend);
      // 将消息暂存服务器或消息队列
      log.info("将消息暂存至服务器：" + msgDto);
      msgPsersister.saveMesasge(msgDto);

      try {
        sess.getBasicRemote().sendText(JSON.toJSONString(msgToSend));
        log.info("发送消息，由" + msg.getFrom() + " -> " + msg.getTo());
      } catch (IOException e) {
        log.trace(e);
      }
    } else {
      log.info("用户已不在线：" + destSessId);
    }
  }

  @Override
  public void online(String uuid) {
    List<Session> allSessions = WsSessionSupport.sharedInstance().getAllSessions(uuid);
    for (Session session : allSessions) {
      MessageToSendVo<UserBasicVo> onlineMsg = buildMessage(uuid, MessageType.OnlineNotify);
      if (null == onlineMsg) {
        continue;
      }
      try {
        session.getBasicRemote().sendText(JSON.toJSONString(onlineMsg));
      } catch (IOException e) {
        log.trace("无法发送消息", e);
      }
    }

    // 给该用户发送历史消息记录
    Session session = WsSessionSupport.sharedInstance().getSession(uuid);
    if (null != session && session.isOpen()) {
      ValueObject msgRes = msgPsersister.allMessages(uuid, 50);
      if (null != msgRes && 200 == ((ResponseVo) msgRes).getCode()) {
        List<SimpleMessageDto> allMessages = ((ResponseVo<List<SimpleMessageDto>>) msgRes).getData();
        if (null == allMessages) {
          log.warn("无法发现历史消息");
        } else if (allMessages.isEmpty()) {
          log.info("无历史消息");
        } else {
          // 发送消息
          List<MessageToSendVo<Object>> messagePayload = allMessages.stream().map(msg -> {
            return msgMap.from(msg);
          }).collect(Collectors.toList());
          messagePayload.forEach(msg -> {
            if (msg.getFrom().equals(WsSessionSupport.sharedInstance().getUsername(uuid))) {
              msg.setReciever(false);
            } else {
              msg.setReciever(true);
            }
          });
          MessageToSendVo<List<MessageToSendVo<Object>>> msg = new MessageToSendVo<List<MessageToSendVo<Object>>>();
          msg.setFrom(USER_SYSTEM);
          msg.setTo(WsSessionSupport.sharedInstance().getUsername(uuid));
          msg.setCreateDate(DateTime.now().toDate());
          msg.setId(DateTime.now().getMillis());
          msg.setContent("历史消息");
          msg.setPayload(messagePayload);

          try {
            session.getBasicRemote().sendText(JSON.toJSONString(msg));
          } catch (IOException e) {
            log.trace("无法发送消息", e);
          }
        }
      }
    } else {
      log.warn("当前会话已经关闭：" + session.getId());
      offline(uuid);
    }
  }

  /**
   * 构建上线下线的消息
   * @param uuid 上线或下线的用户
   * @param type 消息类型：上线或下线
   * @return 构建好的消息实体，如果为<code>null</code>则为用户信息无法对应
   */
  private MessageToSendVo<UserBasicVo> buildMessage(String uuid, MessageType type) {
    MessageToSendVo<UserBasicVo> onlineMsg = new MessageToSendVo<UserBasicVo>();
      onlineMsg.setContent(String.join("->", uuid, "offlined"));
      onlineMsg.setType(MessageType.OnlineNotify.code());
      onlineMsg.setFrom(USER_SYSTEM);
      String username = WsSessionSupport.sharedInstance().getUsername(uuid);
      ValueObject userRes = userServ.getUser(username);
      if (null == userRes || 200 != ((ResponseVo) userRes).getCode()) {
        log.warn("当前用户信息不存在，无法发送具体数据");
        return null;
      }
      onlineMsg.setType(type.code());
      onlineMsg.setFrom(USER_SYSTEM);
      onlineMsg.setPayload(((ResponseVo<UserBasicVo>) userRes).getData());
      onlineMsg.setCreateDate(DateTime.now().toDate());
      onlineMsg.setId(DateTime.now().getMillis());

      return onlineMsg;
  }

  @Override
  public void offline(String uuid) {
    List<Session> allSessions = WsSessionSupport.sharedInstance().getAllSessions(uuid);
    for (Session session : allSessions) {
      MessageToSendVo<UserBasicVo> onlineMsg = buildMessage(uuid, MessageType.OnlineNotify);
      if (null == onlineMsg) {
        continue;
      }
      try {
        session.getBasicRemote().sendText(JSON.toJSONString(onlineMsg));
      } catch (IOException e) {
        log.trace("无法发送消息", e);
      }
    }
  }

  @Override
  public void readMessage(String msgId) {
    readMessage(Long.parseLong(msgId));
  }

  @Override
  public void readMessage(long msgId) {
    msgPsersister.readMessage(msgId);
  }
  
}
