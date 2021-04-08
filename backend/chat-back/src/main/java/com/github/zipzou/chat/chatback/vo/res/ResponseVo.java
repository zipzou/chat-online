package com.github.zipzou.chat.chatback.vo.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.zipzou.chat.chatback.vo.ValueObject;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * 请求响应的统一实体
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(value = { "hibernateLazyInitializer", "handler" })
public class ResponseVo<T> implements ValueObject {
  
  /**
   *
   */
  private static final long serialVersionUID = 153205943360448336L;
  private int code;
  private String message;
  private String reason;
  private T data;
  private boolean success;

  /**
   * 请求成功，并返回数据
   * @param <T> 所携带的数据的类型
   * @param data 所携带的数据
   * @return 响应成功实体
   */
  public static <T> ResponseVo<T> success(T data) {
    return new ResponseVo<T>(200, null, null, data, true);
  }

  /**
   * 请求成功，并返回执行消息和数据
   * @param <T> 携带数据类型
   * @param message 请求执行消息
   * @param data 响应携带的数据
   * @return 响应成功实体
   */
  public static <T> ResponseVo<T> success(String message, T data) {
    ResponseVo<T> res = success(data);
    res.setMessage(message);

    return res;
  }

  /**
   * 请求成功，但不携带任何数据
   * @param message 请求执行消息
   * @return 响应成功实体
   */
  public static ResponseVo<ValueObject> success(String message) {
    return new ResponseVo<ValueObject>(200, message, null, new EmptyVo(), true);
  }

  /**
   * 请求失败，但不携带任何信息，且原因未知
   * @param code 请求失败状态码
   * @return 响应失败实体
   */
  public static ResponseVo<ValueObject> fail(int code) {
    return new ResponseVo<ValueObject>(code, null, null, new EmptyVo(), true);
  }

  /**
   * 请求失败，未知原因，但携带指定附加数据
   * @param <T> 携带的数据类型
   * @param code 请求失败状态码
   * @param data 响应携带的数据
   * @return 响应失败实体
   */
  public static <T> ResponseVo<T> fail(int code, T data) {
    return new ResponseVo<T>(code, null, null, data, true);
  }

  /**
   * 请求失败，已知原因，且携带指定附加数据
   * @param <T> 携带的数据类型
   * @param code 请求失败状态码
   * @param reason 请求失败的原因
   * @param data 响应携带的数据
   * @return 响应失败实体
   */
  public static <T> ResponseVo<T> fail(int code, String reason, T data) {
    return new ResponseVo<T>(code, null, reason, data, true);
  }

  /**
   * 请求失败，已知原因，但不携带任何附加数据
   * @param reason 失败原因
   * @return 响应失败实体
   */
  public static ResponseVo<ValueObject> fail(String reason) {
    return new ResponseVo<ValueObject>(400, null, reason, new EmptyVo(), true);
  }

  /**
   * 请求失败，明确原因和执行过程的消息，但不携带任何数据
   * @param reason 请求失败原因
   * @param message 请求执行消息
   * @return 响应失败实体
   */
  public static ResponseVo<ValueObject> fail(String reason, String message) {
    ResponseVo<ValueObject> res = fail(reason);
    res.setMessage(message);

    return res;
  }

  /**
   * 请求失败，已知原因，且提供附加数据
   * @param <T> 附加数据类型
   * @param reason 请求失败原因
   * @param data 响应携带的数据
   * @return 响应失败实体
   */
  public static <T> ResponseVo<T> fail(String reason, T data) {
    ResponseVo<T> res = new ResponseVo<T>(400, null, reason, data, true);
    res.setData(data);
    return res;
  }

  /**
   * 请求失败，已知原因、请求执行消息和附加数据
   * @param <T> 附带数据的数据类型
   * @param reason 请求失败原因
   * @param message 请求执行消息
   * @param data 响应携带的数据
   * @return 响应失败实体
   */
  public static <T> ResponseVo<T> fail(String reason, String message, T data) {
    ResponseVo<T> res = fail(reason, data);
    res.setMessage(message);

    return res;
  }
  
}
