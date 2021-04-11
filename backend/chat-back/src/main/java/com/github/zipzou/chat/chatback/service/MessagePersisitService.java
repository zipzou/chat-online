package com.github.zipzou.chat.chatback.service;

import com.github.zipzou.chat.chatback.dao.bean.SimpleMessageDto;
import com.github.zipzou.chat.chatback.vo.ValueObject;

/**
 * 消息暂存服务
 */
public interface MessagePersisitService {
  /**
   * 
   * @param msg
   * @return
   */
  public ValueObject saveMesasge(SimpleMessageDto msg);

  /**
   * 
   * @param msgId
   * @return
   */
  public ValueObject readMessage(long msgId);

  /**
   * 
   * @param msgId
   * @return
   */
  public ValueObject unreadMessage(long msgId);

  /**
   * 
   */
  public ValueObject unreadMessages(String to);

  /**
   * 
   * @return
   */
  public ValueObject allMessages(String to);

  /**
   * 
   * @param limit
   * @return
   */
  public ValueObject allMessages(String to, int limit);
}
