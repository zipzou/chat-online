package com.github.zipzou.chat.chatback.service.codec;

import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * MD5加密算法
 */
public class Md5Codec {

  /**
   * 生成加密所需salt值
   * @param length salt值的长度
   * @return 由数字、大小写构成的salt序列
   */
  public static String generateSalt(int length) {
    Random random = new Random();
    StringBuffer seq = new StringBuffer();
    for (int i = 0; i < length; i++) {
      int codeVal = random.nextInt(100);
      if (25 > codeVal) { // 在0-25以内，使用数字编码
        codeVal = codeVal % 10;
        seq.append((char)(codeVal + '0'));
      } else if (62 > codeVal) { // 在26-62以内，使用小写字母编码
        codeVal = codeVal % 26;
        seq.append((char)(codeVal + 'a'));
      } else { // 剩下采用大写字母编码
        codeVal = codeVal % 26;
        seq.append((char)(codeVal + 'A'));
      }
    }
    return seq.toString();
  }

  /**
   * 产生长度为4的<code>salt</code>序列
   * @return 由数字、大小写构成的salt序列
   */
  public static String generateSalt() {
    return generateSalt(4);
  }

  /**
   * 生成salt值，并根据salt值加密密码
   * @param password 明文密码
   * @return 经过salt值加密后的密码
   */
  public static String encryptPassword(String password) {
    String salt = generateSalt();
    return encryptPassword(password, salt);
  }

  /**
   * 根据给定的salt值加密密码
   * @param password 明文密码
   * @param salt 加密所用的salt值
   * @return 经过salt值混合加密后的密码
   */
  public static String encryptPassword(String password, String salt) {
    String catPassword = salt + password;
    return DigestUtils.md5Hex(catPassword);
  }
}
