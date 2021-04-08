package com.github.zipzou.chat.chatback.dao.user;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.github.zipzou.chat.chatback.dao.bean.UserDto;
import com.github.zipzou.chat.chatback.dao.mapper.UserDao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class InsertUserTests {
  private @Autowired UserDao userDao;
  @Test
  public void testInsertUser() {
    UserDto user = new UserDto();
    user.setUsername("test3");
    user.setPassword("test3pwd");
    user.setSalt("salt");
    user.setNickname("test3");
    user.setGender(1);

    int row = userDao.insertUser(user);
    Long key = user.getId();
    log.info(user.getId());
    assertTrue(key > 1);
  }
}
