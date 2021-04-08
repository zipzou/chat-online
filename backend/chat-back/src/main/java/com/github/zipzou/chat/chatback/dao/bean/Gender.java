package com.github.zipzou.chat.chatback.dao.bean;

public enum Gender {
  Male(0), Female(1);
  private int code;
  private Gender(int code) {
    this.code = code;
  }
  public int code() {
    return this.code;
  }
  public static Gender fromCode(int code) {
    if (0 == code) {
      return Gender.Male;
    } else{
      return Gender.Female;
    }
  }
}
