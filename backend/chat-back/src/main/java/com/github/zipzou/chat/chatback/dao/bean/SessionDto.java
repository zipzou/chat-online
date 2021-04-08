package com.github.zipzou.chat.chatback.dao.bean;

import java.util.Date;

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
public class SessionDto {
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
