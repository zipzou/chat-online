package com.github.zipzou.chat.chatback.service;

import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.UserDetailVo;

/**
 * 用户信息服务接口
 */
public interface UserService {
  /**
   * 根据用户ID获取用户基本信息。
   * @param id 用户ID
   * @return 用户基本信息
   */
  public ValueObject getUser(int id);
  /**
   * 根据用户名获取用户基本信息
   * @param username 用户名
   * @return 用户基本信息
   */
  public ValueObject getUser(String username);

  /**
   * 创建新的用户信息，注意⚠️：<b>密码加密将在方法内部实现，请传入明文密码。</b>
   * @param user 用户信息
   * @return 创建的结果
   */
  public ValueObject createUser(UserDetailVo user);

  /**
   * 根据用户名和密码检查用户名密码是否匹配
   * @param username 用户名
   * @param password 密码明文
   * @return 是否用户名、密码匹配
   */
  public ValueObject checkUser(String username, String password);

  /**
   * 根据用户名和密码检查用户名密码是否匹配
   * @param user 待匹配的用户实体
   * @return 包含用户名和密码的实体
   */
  public ValueObject checkUser(UserDetailVo user);
}
