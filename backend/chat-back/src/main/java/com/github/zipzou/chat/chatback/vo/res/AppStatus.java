package com.github.zipzou.chat.chatback.vo.res;

public enum AppStatus {
  Uninitialized(0),
  Unlogin(1),
  Ready(2);

  private int code;

  private AppStatus(int code) {
    this.code = code;
  }

  public int code() {
    return this.code;
  }
}
