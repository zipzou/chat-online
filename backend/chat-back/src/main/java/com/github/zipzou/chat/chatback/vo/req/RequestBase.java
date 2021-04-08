package com.github.zipzou.chat.chatback.vo.req;

import com.github.zipzou.chat.chatback.vo.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public abstract class RequestBase implements ValueObject {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private String accessToken;
}
