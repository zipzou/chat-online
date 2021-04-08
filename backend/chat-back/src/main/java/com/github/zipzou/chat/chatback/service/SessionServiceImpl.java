package com.github.zipzou.chat.chatback.service;

import com.github.zipzou.chat.chatback.dao.bean.SessionDto;
import com.github.zipzou.chat.chatback.dao.mapper.SessionDao;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 会话控制类
 */
@Service
public class SessionServiceImpl implements SessionService {

  @Autowired
  private SessionDao sessDao;
  @Override
  public ValueObject newSession(SessionDto sess) {
    int count = sessDao.insertSession(sess);
    if (0 >= count) {
      return ResponseVo.fail("设置失败");
    } else {
      return ResponseVo.success("更新成功");
    }
  }

  @Override
  public ValueObject setAttribute(String id, String key, String value) {
    String val = sessDao.selSessionValue(key, id);
    if (null == val) {
      int count = sessDao.insertSessionKeyValue(key, id, value);
      if (0 >= count) {
        return ResponseVo.fail("设置失败");
      } else {
        return ResponseVo.success("设置数据：" + count);
      }
    } else {
      int count = sessDao.updateSessionValue(key, id, value);
      if (0 >= count) {
        return ResponseVo.fail("设置失败");
      } else {
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
      return ResponseVo.success("获取成功", val);
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
  
}
