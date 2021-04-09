package com.github.zipzou.chat.chatback.service;

import com.github.zipzou.chat.chatback.beans.mapper.SessionMap;
import com.github.zipzou.chat.chatback.dao.bean.SessionDto;
import com.github.zipzou.chat.chatback.dao.mapper.SessionDao;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;
import com.github.zipzou.chat.chatback.vo.res.SessionVo;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.log4j.Log4j2;

/**
 * 会话控制类
 */
@Log4j2
@Service
public class SessionServiceImpl implements SessionService {

  @Autowired
  private SessionDao sessDao;
  private @Autowired SessionMap map;
  @Override
  public ValueObject newSession(SessionDto sess) {
    int count = sessDao.insertSession(sess);
    if (0 >= count) {
      return ResponseVo.fail("设置失败");
    } else {
      return ResponseVo.success("更新成功", sess.getSessionId());
    }
  }

  @Override
  public ValueObject setAttribute(String id, String key, String value) {
    String val = sessDao.selSessionValue(key, id);
    if (null == val) {
      SessionDto exists = sessDao.selSessById(id);
      if (null == exists) {
        log.info("未初始化的会话");
        return ResponseVo.fail("设置失败");
      } else {
        int count = sessDao.insertSessionKeyValue(key, id, value);
          if (0 >= count) {
            return ResponseVo.fail("设置失败");
          } else {
            log.info("更新session缓存：" + id);
            return ResponseVo.success("设置数据：" + count);
          }
      }
    } else {
      int count = sessDao.updateSessionValue(key, id, value);
      if (0 >= count) {
        return ResponseVo.fail("设置失败");
      } else {
        log.info("更新session缓存：" + id);
        return ResponseVo.success("设置数据：" + count);
      }
    }
  }

  @Override
  public ValueObject getAttribute(String id, String key) {
    String val = sessDao.selSessionValue(key, id);
    if (null == val) {
      return ResponseVo.fail("获取失败，键" + key + "不存在");
    } else {
      return ResponseVo.success("获取成功", (Object) val);
    }
  }

  @Override
  public ValueObject removeAttribute(String id, String key) {
    int delCount = sessDao.delSessionKey(key, id);
    if (0 >= delCount) {
      return ResponseVo.fail("移除失败");
    } else {
      return ResponseVo.success("移除成功：" + delCount);
    }
  }

  @Override
  public ValueObject expired(String id) {
    int count = sessDao.delSessionById(id);
    if (0 >= count) {
      return ResponseVo.fail("移除失败");
    } else {
      return ResponseVo.success("移除成功：" + count);
    }
  }

  @Override
  public ValueObject newSession(String id) {
    SessionVo sessionItem = new SessionVo();
    DateTime now = DateTime.now();
    sessionItem.setCreatedTime(now.toDate());
    sessionItem.setExpiredTime(now.plusHours(12).toDate());
    sessionItem.setSessionId(id);
    return newSession(map.from(sessionItem));
  }

  @Override
  public ValueObject newSession() {
    SessionVo sessionItem = new SessionVo();
    DateTime now = DateTime.now();
    sessionItem.setCreatedTime(now.toDate());
    sessionItem.setExpiredTime(now.plusHours(12).toDate());
    String sessionId = SessionId.generateId();
    sessionItem.setSessionId(sessionId);
    return newSession(map.from(sessionItem));
  }
  
}
