package com.github.zipzou.chat.chatback.service;

import java.util.List;

import com.github.zipzou.chat.chatback.dao.bean.SimpleMessageDto;
import com.github.zipzou.chat.chatback.dao.mapper.SimpleMessageDao;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

/**
 * 消息持久化服务
 */
@Service
@Log4j2
public class MessagePersisitServiceImpl implements MessagePersisitService {

  private @Autowired SimpleMessageDao msgDao;

  @Override
  public ValueObject saveMesasge(SimpleMessageDto msg) {
    int rows = msgDao.insertMessage(msg);
    if (0 >= rows) {
      log.warn("存储消息请求已提交，但未插入成功", msg);
      return ResponseVo.fail("插入失败", "请求已提交，但未响应");
    } else {
      return ResponseVo.success("消息存储成功", msg.getId());
    }
  }

  @Override
  public ValueObject readMessage(long msgId) {
    int rows = msgDao.updateMessageStatus(msgId, true);
    if (0 >= rows) {
      log.warn("更改消息状态请求已提交，但未插入成功", msgId);
      return ResponseVo.fail("更改失败", "请求已提交，但未响应");
    } else {
      return ResponseVo.success("消息更改成功", msgId);
    }
  }

  @Override
  public ValueObject unreadMessage(long msgId) {
    int rows = msgDao.updateMessageStatus(msgId, false);
    if (0 >= rows) {
      log.warn("更改消息状态请求已提交，但未插入成功", msgId);
      return ResponseVo.fail("更改失败", "请求已提交，但未响应");
    } else {
      return ResponseVo.success("消息更改成功", msgId);
    }
  }

  @Override
  public ValueObject unreadMessages(String to) {
    List<SimpleMessageDto> messages = msgDao.selAllMessagesTo(to);
    return ResponseVo.success("获取成功", messages);
  }

  @Override
  public ValueObject allMessages(String to) {
    List<SimpleMessageDto> allMessages = msgDao.selAllMessagesTo(to);
    return ResponseVo.success("获取成功", allMessages);
  }

  @Override
  public ValueObject allMessages(String to, int limit) {
    return ResponseVo.success("获取成功", msgDao.selAllMessagesToLimitBy(to, limit));
  }

  
}