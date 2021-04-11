package com.github.zipzou.chat.chatback.dao.mapper;

import com.github.zipzou.chat.chatback.dao.bean.SessionDto;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 操作会话的Dao
 */
public interface SessionDao {
  /**
   * 根据ID查询到会话
   * @param id 会话ID
   * @return 会话实体
   */
  @Results(id="session_map", value = 
    {@Result(column = "sess_id", property = "sessionId"),
    @Result(column = "expired_time", property = "expiredTime"),
    @Result(column = "create_time", property = "createdTime")}
  )
  @Select("select * from session where sess_id = #{id}; ")
  public SessionDto selSessById(String id);

  /**
   * 插入新的会话到数据库
   * @param sess 会话信息
   * @return 插入会话的数量
   */
  @Insert("insert into session(sess_id, expired_time, create_time) values(#{sessionId}, #{expiredTime}, #{createdTime}) ;")
  public int insertSession(SessionDto sess);

  /**
   * 删除对应ID的会话信息
   * @param id 会话ID
   * @return 删除的数据数量
   */
  @Delete("delete from session where sess_id = #{id} ; ")
  public int delSessionById(String id);

  /**
   * 给指定的会话创建键和值
   * @param key 会话值对应的键
   * @param id 会话ID
   * @param value 会话值
   * @return 插入的数据数量
   */
  @Insert("insert into session_attr(session_key, session_id, session_value, create_date) values(#{key}, #{id}, #{val}, NOW()) ; ")
  public int insertSessionKeyValue(@Param("key") String key, @Param("id") String id, @Param("val") Object value);

  /**
   * 更新会话中对应键的值
   * @param key 数据对应的键
   * @param id 所属的会话ID
   * @param value 要更新的数据值
   * @return 更新的数据数量
   */
  @Update("update session_attr set session_value = #{value} where session_key = #{key} and session_id = #{id} ; ")
  public int updateSessionValue(String key, String id, Object value);

  /**
   * 根据会话查询指定键的值
   * @param key 会话中存储的键
   * @param id 会话ID
   * @return 键对应的值
   */
  @Select("select session_value from session_attr where session_key = #{key} and session_id = #{id}  ;")
  public String selSessionValue(String key, String id);

  /**
   * 删除会话中对应的键和值
   * @param key 待删除的键
   * @param id 待删除数据所属的会话
   * @return 删除数据的数量
   */
  @Delete("delete from session_attr where  session_key = #{key} and session_id = #{id} ;")
  public int delSessionKey(String key, String id);

  /**
   * 根据过期时间和当前时间，删除所有过期的数据
   * @return 删除的数据数量
   */
  @Delete("delete from session where expired_time < NOW()  ;")
  public int delExpiredData();

  /**
   * 根据用户分配的UUID查询最新的一个用户名
   * @param uuid 用户UUID
   * @return 用户名
   */
  @Deprecated
  @Select("select distinct session_value from session_attr where session_key = #{uuid} order by create_date desc ; ")
  public String selUsername(String uuid);
}
