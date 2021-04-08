package com.github.zipzou.chat.chatback.vo.res;

import com.github.zipzou.chat.chatback.vo.UserBasicVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 用户详细信息的值对象，用于包含用户密码等敏感信息
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class UserDetailVo extends UserBasicVo {

  /**
   *
   */
  private static final long serialVersionUID = 1L;
  
  /**
   * 用户密码
   */
  private String password;
  /**
   * 用户加密所用salt值
   */
  private String salt;

}
