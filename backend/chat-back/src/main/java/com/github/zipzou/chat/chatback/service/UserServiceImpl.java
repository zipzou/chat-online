package com.github.zipzou.chat.chatback.service;

import com.github.zipzou.chat.chatback.beans.mapper.UserMap;
import com.github.zipzou.chat.chatback.dao.bean.UserDto;
import com.github.zipzou.chat.chatback.dao.mapper.UserDao;
import com.github.zipzou.chat.chatback.service.codec.Md5Codec;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;
import com.github.zipzou.chat.chatback.vo.res.UserDetailVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

/**
 * 用户信息服务类
 */
@Service
@Log4j2
public class UserServiceImpl implements UserService {

  private @Autowired UserDao userDao;
  private @Autowired UserMap mapper;
  @Override
  public ValueObject getUser(int id) {
    UserDto userDto = userDao.selUserById(id);
    UserBasicVo userBasic = mapper.mapWith(userDto);

    return ResponseVo.success(userBasic);
  }

  @Override
  public ValueObject getUser(String username) {
    UserDto dto = userDao.selUserByUsername(username);
    UserBasicVo userBasic = mapper.mapWith(dto);
    return ResponseVo.success(userBasic);
  }

  @Override
  public ValueObject createUser(UserDetailVo user) {
    // 将用户密码加密
    String salt = Md5Codec.generateSalt();
    user.setSalt(salt);
    String passwordEncrypted = Md5Codec.encryptPassword(user.getPassword(), salt);
    user.setPassword(passwordEncrypted);
    UserDto toCreatedUser = mapper.dto(user);
    int result = userDao.insertUser(toCreatedUser);
    if (0 >= result) {
      return ResponseVo.fail("无法新增用户");
    }
    Long createdUid = toCreatedUser.getId();
    log.info("User created with id:" + createdUid);
    log.info("User information created: " + toCreatedUser);

    return ResponseVo.success("用户创建成功", mapper.mapWith(toCreatedUser));
  }

  @Override
  public ValueObject checkUser(String username, String password) {
    String salt = userDao.selSaltByUsername(username);
    String passwordEncrypted = Md5Codec.encryptPassword(password, salt);

    String passwordForUser = userDao.selPasswordByUsername(username);
    if (null == passwordForUser) {
      return ResponseVo.fail("验证失败", "未找到用户名对应的数据");
    } else if (passwordForUser.equals(passwordEncrypted)) {
      return ResponseVo.success("用户名密码匹配");
    } else {
      return ResponseVo.fail("验证失败", "用户名密码不匹配");
    }
  }

  @Override
  public ValueObject checkUser(UserDetailVo user) {
    return checkUser(user.getUsername(), user.getPassword());
  }

}
