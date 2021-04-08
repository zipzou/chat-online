package com.github.zipzou.chat.chatback.vo;

import java.util.Date;

import com.github.zipzou.chat.chatback.vo.req.RequestBase;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户基本信息的值对象
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserBasicVo extends RequestBase {
  /**
   *
   */
  private static final long serialVersionUID = 6371701967803459619L;
  /**
   * 用户ID
   */
  private Long id;
  /**
   * 用户名
   */
  private String username;
  /**
   * 昵称
   */
  private String nickname;
  /**
   * 性别
   */
  private int gender;

  /**
   * 创建日期
   */
  private Date createDate;

  /**
   * 用户的唯一标识UUID序列
   */
  private String userUUID;
}
