package com.github.zipzou.chat.chatback.dao.mapper;

import com.github.zipzou.chat.chatback.dao.bean.UserDto;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

/**
 * 用户Dao层
 */
public interface UserDao {
  
  /**
   * 根据用户ID查找用户信息
   * @param id 用户ID编号
   * @return 用户信息
   */
  @Results(id = "userMap", value = {
    @Result(column="create_date", property = "createDate"),
  })
  @Select("select * from user where id = #{id};")
  UserDto selUserById(int id);

  /**
   * 根据用户名查找用户信息
   * @param username 用户名
   * @return 用户信息
   */
  @ResultMap(value="userMap")
  @Select("select * from user where username = #{username}")
  UserDto selUserByUsername(String username);

  /**
   * 根据用户ID统计用户数量
   * @param id 用户ID
   * @return 用户数量
   */
  @Select("select COUNT(*) from user where id = #{id}")
  int countUserById(int id);

  /**
   * 根据用户名统计用户数量
   * @param username 用户名
   * @return 用户数量
   */
  @Select("select COUNT(*) from user where username = #{username}")
  int countUserByUsername(String username);

  /**
   * 插入新用户数据
   * @param user 用户信息
   * @return 插入成功的用户数量，插入后的ID从<code>user.getId()</code>获取。
   */
  @Insert("insert into user(username, password, salt, nickname, gender, create_date) values(#{username}, #{password}, #{salt}, #{nickname}, #{gender}, NOW());")
  @SelectKey(keyColumn = "id", resultType = Long.class, keyProperty = "id", statement = "SELECT LAST_INSERT_ID()", before = false)
  // @Options(useGeneratedKeys = true, keyProperty = "id", keyColumn = "id")
  int insertUser(UserDto user);

  /**
   * 删除指定用户ID的用户数据
   * @param id 用户ID
   * @return 删除的实际数据行数
   */
  @Delete("delete from user where id = #{id}")
  int delUserById(int id);

  /**
   * 根据用户名删除用户数据
   * @param username 用户名
   * @return 实际删除的数据行数
   */
  @Delete("delete from user where username = #{username}")
  int delUserByUsername(String username);

  /**
   * 更新用户信息中的非null字段
   * @param user 用户信息
   * @return 实际更新的数据行数
   */
  @Update("")
  int updateUserNotNull(UserDto user);

  /**
   * 更新用户的所有信息，慎重！
   * @param user 完整的用户信息
   * @return 实际更新的数据行数
   */
  @Update("update user set username=#{username}, password=#{password}, salt=#{salt}, nickname = #{nickname}, gender = #{gender}, create_date = #{createDate} where id = #{id} ;")
  int updateUser(UserDto user);

  /**
   * 获取对应ID用户加密的salt值
   * @param id 用户ID
   * @return salt加密值
   */
  @Select("select salt from user where id = #{id};")
  String selSaltById(int id);
  
  /**
   * 根据对应用户名获取用户密码的salt值
   * @param username 用户名
   * @return salt加密值
   */
  @Select("select salt from user where username = #{username};")
  String selSaltByUsername(String username);

  /**
   * 根据ID获取对应用户的密码
   * @param id 用户ID
   * @return 用户加密后的密码
   */
  @Select("select password from user where id = #{id};")
  String selPasswordById(int id);
  /**
   * 根据用户名获取对应用户的密码
   * @param username 用户名
   * @return 用户加密后的密码
   */
  @Select("select password from user where username = #{username};")
  String selPasswordByUsername(String username);

  /**
   * 根据ID更新对应用户的密码
   * @param id 用户ID
   * @param password 加密后的密码
   * @return 实际更新的数据行数
   */
  @Update("update user set password = #{password} where id = #{id}; ")
  int updatePasswordById(int id, String password);
  /**
   * 根据用户名更新对应用户的密码
   * @param username 用户名
   * @param password 加密后的密码
   * @return 实际更新的数据行数
   */
  @Update("update user set password = #{password} where username = #{username}; ")
  String updatePasswordByUsername(String username, String password);
}
