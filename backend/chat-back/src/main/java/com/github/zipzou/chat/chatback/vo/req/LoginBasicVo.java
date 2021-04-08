package com.github.zipzou.chat.chatback.vo.req;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 登录所需基本信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LoginBasicVo extends RequestBase {

  /**
   *
   */
  private static final long serialVersionUID = -9002031359083619141L;

  private String username;
  private String password;
  private String valCode;
  
}