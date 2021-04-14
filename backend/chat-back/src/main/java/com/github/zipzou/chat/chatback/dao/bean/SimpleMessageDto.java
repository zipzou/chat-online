package com.github.zipzou.chat.chatback.dao.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 简单消息实体，用于配置点对点消息
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SimpleMessageDto implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = -4545269561543434842L;
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
   * 消息类型
   */
  private int type;

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
  private Long fileType;

  /**
   * 已读状态
   */
  private boolean readStatus = false;

  /**
   * 消息发送日期
   */
  private Date createDate;
}
