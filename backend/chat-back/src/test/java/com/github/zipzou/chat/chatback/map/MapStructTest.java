package com.github.zipzou.chat.chatback.map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.github.zipzou.chat.chatback.beans.mapper.UserMap;
import com.github.zipzou.chat.chatback.dao.bean.UserDto;
import com.github.zipzou.chat.chatback.dao.mapper.UserDao;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.log4j.Log4j2;

@SpringBootTest
@Log4j2
public class MapStructTest {

  private @Autowired UserDao userDao;
  private @Autowired UserMap map;
  @Test
  public void testMap() {
    int uId = 1;
    UserDto userDto = userDao.selUserById(uId);
    UserBasicVo userBasic = map.mapWith(userDto);
    assertNotNull(userDto.getId());

    assertNotNull(userBasic);

    assertEquals(userBasic.getId(), userDto.getId());
    assertEquals(userBasic.getUsername(), userDto.getUsername());

    log.info(userDto);
    log.info(userBasic);
  }
}
