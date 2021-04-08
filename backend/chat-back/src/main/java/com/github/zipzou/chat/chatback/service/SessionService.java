package com.github.zipzou.chat.chatback.service;

import com.github.zipzou.chat.chatback.dao.bean.SessionDto;
import com.github.zipzou.chat.chatback.vo.ValueObject;

/**
 * 会话管理类，用户管理会话的创建、更新和移除。
 * @author Zip Zou
 */
public interface SessionService {
  /**
   * 创建新的会话
   * @param sess 会话信息
   * @return 创建结果
   */
  public ValueObject newSession(SessionDto sess);

  /**
   * 设置会话的键和值，如键不存在，则创建该键并写入数据，否则将修改原始键对应的内容
   * @param id 会话ID
   * @param key 数据键
   * @param value 数据值
   * @return 创建结果
   */
  public ValueObject setAttribute(String id, String key, String value);

  /**
   * 获取会话对应键的值
   * @param id 会话ID
   * @param key 会话中的目标键
   * @return 创建结果
   */
  public ValueObject getAttribute(String id, String key);

  /**
   * 移除会话中的键
   * @param id 会话ID
   * @param key 待移除的属性键
   * @return 移除结果
   */
  public ValueObject removeAttribute(String id, String key);

  /**
   * 将会话设置为过期，该过程将删除会话ID对应的所有数据
   * @param id 会话ID
   * @return 删除结果
   */
  public ValueObject expired(String id);
}
