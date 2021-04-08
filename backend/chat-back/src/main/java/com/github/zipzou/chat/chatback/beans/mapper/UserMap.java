package com.github.zipzou.chat.chatback.beans.mapper;

import com.github.zipzou.chat.chatback.dao.bean.UserDto;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;
import com.github.zipzou.chat.chatback.vo.res.UserDetailVo;

import org.mapstruct.Mapper;

/**
 * 用户数据的映射
 */
@Mapper(componentModel = "spring")
public interface UserMap {
  /**
   * 将基本用户信息转为dto
   * @param source 待转换的实体
   * @return 转换后的实体
   */
  public UserDto dto(UserBasicVo source);
  /**
   * 将用户dto转换为基本信息
   * @param source 待转换的DTO
   * @return 转换后的实体
   */
  public UserBasicVo mapWith(UserDto source);
  /**
   * 将用户详细信息转换为基本信息
   * @param source 转换后的实体
   * @return 用户详细信息
   */
  public UserBasicVo mapWith(UserDetailVo source);
  /**
   * 将用户基本信息转为详细信息
   * @param source 转换后的实体
   * @return 用户详细信息
   */
  public UserDetailVo extend(UserBasicVo source);
  /**
   * 将用户详细信息转换为dto
   * @param source 用户详细信息
   * @return dto
   */
  public UserDto dto(UserDetailVo source);
}