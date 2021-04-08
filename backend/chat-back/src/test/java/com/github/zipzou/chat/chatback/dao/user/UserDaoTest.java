package com.github.zipzou.chat.chatback.dao.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.zipzou.chat.chatback.dao.bean.UserDto;
import com.github.zipzou.chat.chatback.dao.mapper.UserDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

/**
 * UserDaoTest
 */
@SpringBootTest
@Log4j2
public class UserDaoTest {

  private @Autowired UserDao userDao;

  @Test
  public void testSelUser() {
    int uId = 1;
    UserDto user = userDao.selUserById(uId);
    assertEquals(uId, user.getId());
    log.info(user);
  }

  @Test
  public void countUser() {
    int uId = 1;
    int count = userDao.countUserById(uId);
    assertTrue(count >= 1);
    log.info(count);
  }

}