package com.github.zipzou.chat.chatback.vo.res;

import com.github.zipzou.chat.chatback.vo.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SimpleTextMessageVo<T> implements ValueObject {

  /**
   *
   */
  private static final long serialVersionUID = 3325533114110235559L;
  /**
   * 发送用户UUID
   */
  private String from;
  /**
   * 消息内容
   */
  private String content;
  /**
   * 消息类型
   */
  private int type;

  /**
   * 是否已读消息
   */
  private boolean read;

  /**
   * 消息携带的额外数据
   */
  private T payload;
}
