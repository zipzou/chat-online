package com.github.zipzou.chat.chatback.vo.res;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * SessionVo
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class SessionVo {
  /**
   * 会话ID
   */
  private String sessionId;
  /**
   * 会话过期时间
   */
  private Date expiredTime;
  /**
   * 会话创建时间
   */
  private Date createdTime;

}