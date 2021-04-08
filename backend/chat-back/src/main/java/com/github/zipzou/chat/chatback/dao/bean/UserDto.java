package com.github.zipzou.chat.chatback.dao.bean;

import java.io.Serializable;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 数据库用户实体
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDto implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 6168325657198514417L;
  /**
   * 用户ID
   */
  private Long id;
  /**
   * 用户名
   */
  private String username;
  /**
   * 用户密码
   */
  private String password;
  /**
   * 加密所用salt
   */
  private String salt;
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
}
