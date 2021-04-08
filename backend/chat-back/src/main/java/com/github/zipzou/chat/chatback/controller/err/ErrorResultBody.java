package com.github.zipzou.chat.chatback.controller.err;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ErrorResultBody extends RuntimeException {
  /**
   *
   */
  private static final long serialVersionUID = -7067760696547318239L;
  private int code;

  public ErrorResultBody(int code) {
    this.code = code;
  }

  public ErrorResultBody(String message, int code) {
    super(message);
    this.code = code;
  }

  public ErrorResultBody(String message) {
    super(message);
  }
}
