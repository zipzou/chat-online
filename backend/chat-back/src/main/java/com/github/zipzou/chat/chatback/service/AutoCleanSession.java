package com.github.zipzou.chat.chatback.service;

import java.util.List;

import javax.websocket.Session;

import com.github.zipzou.chat.chatback.dao.mapper.SessionDao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

/**
 * 自动清理会话管理中，已经过期的会话数据。
 * @author Zip Zou
 * @version 1.0
 */
@Log4j2
@Service
@EnableScheduling
public class AutoCleanSession {
  
  private @Autowired SessionDao sessDao;

  @Scheduled(fixedDelay = 180000)
  @Async
  public void autoClean() {
    log.info("Start clean expired data...");
    int dataCount = sessDao.delExpiredData();
    log.info("Cleaned expired data: " + dataCount);
  }
}
