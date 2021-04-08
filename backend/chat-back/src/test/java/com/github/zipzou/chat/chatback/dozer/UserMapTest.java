package com.github.zipzou.chat.chatback.dozer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.zipzou.chat.chatback.dao.bean.UserDto;
import com.github.zipzou.chat.chatback.dao.mapper.UserDao;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;

import org.dozer.Mapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class UserMapTest {
  private @Autowired Mapper mapper;

  private @Autowired UserDao userDao;

  @Test
  public void testDozerMap() {
    UserDto userDto = userDao.selUserById(1);

    assertNotNull(userDto.getId());

    UserBasicVo userBasic = mapper.map(userDto, UserBasicVo.class);

    assertNotNull(userBasic);

    assertEquals(userBasic.getId(), userDto.getId());
    assertEquals(userBasic.getUsername(), userDto.getUsername());

    log.info(userDto);
    log.info(userBasic);
  }
 }
