package com.github.zipzou.chat.chatback.vo.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用于Websocket的文本消息
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TextMessageVo extends RequestBase  {
  
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   * 发送用户UUID
   */
  private String from;
  /**
   * 接收用户UUID
   */
  private String to;
  /**
   * 消息内容
   */
  private String content;
  /**
   * 消息类型
   */
  private int type;
  
  /**
   * 消息ID
   */
  private long id;
}
