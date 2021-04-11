package com.github.zipzou.chat.chatback.vo.res;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;
import com.github.zipzou.chat.chatback.vo.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 由服务器发送给用户的消息实体
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class MessageToSendVo<T> implements ValueObject {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  /**
   * 消息ID
   */
  private Long id;
  /**
   * 消息发送用户
   */
  private String from;
  /**
   * 消息接收用户
   */
  private String to;
  /**
   * 消息内容
   */
  private String content;
  /**
   * 如果发送的是文件，指明文件扩展名
   */
  private String extension;
  /**
   * 如果发送的是文件，文件上传后存储的位置
   */
  private String fileUrl;
  /**
   * 如果发送的是文件，文件类型
   */
  private String fileType;

  /**
   * 已读状态
   */
  private boolean readStatus = false;

  /**
   * 消息发送日期
   */
  @JSONField(format = "yyyy-MM-dd HH:mm:ss")
  private Date createDate;

  /**
   * 消息携带的额外数据
   */
  private T payload;

  /**
   * 消息类型
   */
  private long type;

  /**
   * 是否为接收消息
   */
  private boolean reciever;
}
