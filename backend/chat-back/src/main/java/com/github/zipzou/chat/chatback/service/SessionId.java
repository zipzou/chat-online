package com.github.zipzou.chat.chatback.service;

import java.util.Random;

/**
 * 用于生成会话ID
 * @author ZipZou
 */
public class SessionId {

  private static char[] chs = new char[] {
    '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '-',
    'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
    'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
  };

  /**
   * 生成指定长度的Sessionid
   * @param length session id的长度
   * @return 由数字、字母和短线构成的指定长度序列
   */
  public static String generateId(int length) {
    Random rdn = new Random();
    StringBuffer buf = new StringBuffer();
    for (int i = 0; i < length; i++) {
      buf.append(chs[rdn.nextInt(chs.length)]);
    }
    return buf.toString();
  }

  /**
   * 生成默认长度为32的SessionId
   * @return 由数字、字母和短线构成的32位序列
   */
  public static String generateId() {
    return generateId(32);
  }
}
