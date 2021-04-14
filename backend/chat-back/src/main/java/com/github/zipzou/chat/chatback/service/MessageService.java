package com.github.zipzou.chat.chatback.service;

import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.req.TextMessageVo;
import com.github.zipzou.chat.chatback.vo.res.MessageToSendVo;

/**
 * 
 */
public interface MessageService {
  public static final String USER_SYSTEM = "SYSTEM";
  /**
   * 发送消息
   * @param msg 待发送的消息
   */
  public void send(TextMessageVo msg);

  /**
   * 
   * @param from
   * @param to
   * @param msg
   */
  public void send(String from, String to, MessageToSendVo<ValueObject> msg);

  /**
   * 发送上线通知
   * @param uuid 上线的用户
   */
  public void online(String uuid);

  /**
   * 发送下线通知
   * @param uuid 下线用户
   */
  public void offline(String uuid);

  /**
   * 将信息标记为已读
   * @param msgId 已读消息ID
   */
  public void readMessage(long msgId);
  /**
   * 将信息标记为已读
   * @param msgId 已读消息ID
   */
  public void readMessage(String msgId);
}
