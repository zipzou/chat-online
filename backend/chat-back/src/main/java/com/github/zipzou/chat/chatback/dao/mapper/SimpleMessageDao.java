package com.github.zipzou.chat.chatback.dao.mapper;

import java.util.List;

import com.github.zipzou.chat.chatback.dao.bean.SimpleMessageDto;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;

/**
 * 点对点消息存储类
 */
public interface SimpleMessageDao {
  /**
   * 向数据库新增一条新的消息
   * @param message 消息实体
   * @return 新增的数据量
   */
  @Insert("insert into simple_message(id, from, `to`, type, content, extension, file_url, file_type, status, create_date) values(#{id}, #{from}, #{to}, #{type}, #{content}, #{extension}, #{fileUrl}, #{fileType}, #{readStatus}, #{createDate}) ; ")
  public int insertMessage(SimpleMessageDto message);

  /**
   * 更新消息状态
   * @param messageId 消息ID
   * @param status 消息当前状态
   * @return 更新的数据行数
   */
  public int updateMessageStatus(Long messageId, boolean status);

  /**
   * 获取指定用户发送的消息
   * @param from 消息发送者
   * @return 来自指定用户的消息列表
   */
  @Results(id = "messageMap", value = {
    @Result(column = "id", property = "id"),
    @Result(column = "from", property = "from"),
    @Result(column = "to", property = "to"),
    @Result(column = "type", property = "type"),
    @Result(column = "content", property = "content"),
    @Result(column = "extension", property = "extension"),
    @Result(column = "file_url", property = "fileUrl"),
    @Result(column = "file_type", property = "fileType"),
    @Result(column = "status", property = "status"),
    @Result(column = "create_date", property = "createDate"),
  })
  @Select("select* from simple_message where from =  #{from}  order by create_date desc ;")
  public List<SimpleMessageDto> selAllMessagesFrom(String from);

  /**
   * 获取发送给指定用户的消息
   * @param to 消息接收者
   * @return 指定用户接收的消息列表
   */
  @ResultMap(value = "messageMap")
  @Select("select * from simple_message where `to` =  #{to} order by create_date desc ;  ")
  public List<SimpleMessageDto> selAllMessagesTo(String to);

  /**
   * 查询若干条指定用户的所有消息
   * @param to 消息接收者
   * @param limit 消息数量
   * @return 指定用户接收的消息列表
   */
  @ResultMap(value = "messageMap")
  @Select("select * from simple_message where `to` =  #{to} order by create_date desc limit #{limit} ;  ")
  public List<SimpleMessageDto> selAllMessagesToLimitBy(String to, int limit);

  /**
   * 获取指定用户发送但是未被读取的消息
   * @param from 消息发送者
   * @return 指定用户发送的未读消息列表
   */
  @ResultMap(value = "messageMap")
  @Select("select * from simple_message where from =  #{from} and status = 0  order by create_date desc ;  ")
  public List<SimpleMessageDto> selAllUnreadMessageFrom(String from);

  /**
   * 查询所有给指定用户发送但未读取的消息
   * @param to 消息接收者
   * @return 指定用户接收的未读消息列表
   */
  @ResultMap(value = "messageMap")
  @Select("select * simple_message where `to` =  #{to} and status = 0  order by create_date desc ;  ")
  public List<SimpleMessageDto> selAllUnreadMessageTo(String to);

  /**
   * 查询所有来自特定用户的已读消息
   * @param from 消息发送者
   * @return 指定用户发送的已读消息列表
   */
  @ResultMap(value = "messageMap")
  @Select("select * from simple_message where from =  #{from} and status != 0  order by create_date desc ;  ")
  public List<SimpleMessageDto> selAllReadMessageFrom(String from);

  /**
   * 查询所有发送给特定用户的已读消息
   * @param to 消息接收者
   * @return 指定用户接收的已读消息列表
   */
  @ResultMap(value = "messageMap")
  @Select("select * from simple_message where `to` =  #{to} and status != 0  order by create_date desc ;  ")
  public List<SimpleMessageDto> selAllReadMessageTo(String to);


}
