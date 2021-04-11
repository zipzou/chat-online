package com.github.zipzou.chat.chatback.dao.bean;

/**
 * 聊天中消息类型
 */
public enum MessageType {
  /**
   * 纯文本消息
   */
  Plain(1),
  /**
   * 消息阅读状态更新
   */
  ReadStatus(1 << 2),
  /**
   * 系统通知
   */
  SysNotify(1 << 3),
  /**
   * 二进制消息
   */
  Binary(1 << 4),
  /**
   * 群文本消息
   */
  GroupPlain(1 << 5),

  /**
   * 群消息状态更新
   */
  GroupStatus(1 << 6),

  /**
   * 群通知消息
   */
  GroupNotify(1 << 7),

  /**
   * 群内二进制数据
   */
  GroupBinary(1 << 8),
  
  /**
   * 上线提醒
   */
  OnlineNotify(1 << 9),
  /**
   * 下线提醒
   */
  OfflineNotify(1 << 10),
  
  /**
   * 历史消息通知
   */
  HistoryMessage(1 << 11),

  /**
   * 未读消息通知
   */
  UnreadMessage(1 << 12),
  
  /**
   * 在线列表
   */
  OnlineList(1 << 13);

  private int code;
  MessageType(int code) {
    this.code = code;
  }

  public int code() {
    return code;
  }
}
