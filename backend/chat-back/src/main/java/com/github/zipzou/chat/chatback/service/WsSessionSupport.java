package com.github.zipzou.chat.chatback.service;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.Session;

import lombok.extern.log4j.Log4j2;

/**
 * Websocket会话支持类，用于管理当前连接的用户以及对应的会话
 */
@Log4j2
public class WsSessionSupport {
  private static class HOLDER {
    private static WsSessionSupport INSTANCE = new WsSessionSupport();
  }

  /**
   * 私有化构造方法，提供单例接口
   */
  private WsSessionSupport() {
  }

  /**
   * 创建共享的会话池
   * 
   * @return 会话池
   */
  public static WsSessionSupport sharedInstance() {
    return HOLDER.INSTANCE;
  }

  private Map<String, String> uuid2SessionId = new Hashtable<String, String>();
  private Map<String, String> uuid2Username = new Hashtable<String, String>();
  private Map<String, Session> sessionId2Sess = new Hashtable<String, Session>();

  /**
   * 获取所有的会话
   * @return 会话列表
   */
  public synchronized List<Session> getAllSessions() {
    List<Session> sesses = new CopyOnWriteArrayList<>();
    for (Entry<String, Session> entry : sessionId2Sess.entrySet()) {
      sesses.add(entry.getValue());
    }
    return sesses;
  }

  /**
   * 获取过滤后的会话列表
   * @param without 需要过滤的UUID
   * @return 过滤后的会话列表
   */
  public synchronized List<Session> getAllSessions(String... without) {
    List<Session> sesses = getAllSessions();
    List<String> sessIds = new CopyOnWriteArrayList<>();
    for (Entry<String, String> entry : uuid2SessionId.entrySet()) {
      for (String out : without) {
        if (out.equals(entry.getKey())) {
          sessIds.add(entry.getValue());
        }
      }
    }
    List<Session> filteredSesses = sesses.stream().filter((sess) -> {
      int indexed = sessIds.indexOf(sess.getId());
      return indexed <= -1;
    }).collect(Collectors.toList());
    return filteredSesses;
  }

  /**
   * 根据用户的UUID获取对应的Websocket会话
   * 
   * @param uuid 用户UUID
   * @return 用户对应的Websocket连接
   */
  public Session getSession(String uuid) {
    if (!uuid2SessionId.containsKey(uuid)) {
      return null;
    }
    return sessionId2Sess.get(uuid2SessionId.get(uuid));
  }

  /**
   * 根据用户ID设置当前用户会话
   * 
   * @param uuid 用户UUID
   * @param sess 所属会话
   */
  public synchronized void setSession(String uuid, Session sess) {
    log.info("设置用户ID：" + uuid + ", 设置会话ID：" + sess.getId());
    if (uuid2SessionId.containsKey(uuid)) {
      String sessId = uuid2SessionId.get(uuid);
      if (sessionId2Sess.containsKey(sessId)) {
        try {
          sessionId2Sess.get(sessId).close(new CloseReason(CloseCodes.GOING_AWAY, "用户已经离开"));
          sessionId2Sess.remove(sessId);
        } catch (IOException e) {
          log.trace("关闭连接失败：" + sessId, sess);
        }
      } else {
        log.warn("原始连接不存在：" + sessId);
      }
    }
    uuid2SessionId.put(uuid, sess.getId());
    sessionId2Sess.put(sess.getId(), sess);
  }

  /**
   * 设置用户UUID和用户名的映射
   * @param uuid 用户UUID
   * @param username 用户名
   */
  public synchronized void setUsername(String uuid, String username) {
    uuid2Username.put(uuid, username);
  }

  public synchronized String getUsername(String uuid) {
    if (!uuid2Username.containsKey(uuid)) {
      return null;
    }
    return uuid2Username.get(uuid);
  }

  /**
   * 获取会话ID对应的用户UUID
   * @param sessId 会话ID
   * @return 用户UUID
   */
  public synchronized String getUUID(String sessId) {
    log.info("获取会话：" + sessId);
    Map<String, String> sessMap = uuid2SessionId;
    Set<Entry<String, String>> entries = sessMap.entrySet();
    for (Entry<String, String> entry : entries) {
      if (sessId.equals(entry.getValue())) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * 根据会话获取用户ID
   * @param sess 当前websocket会话连接
   * @return 会话对应的用户
   */
  public synchronized String getUUID(Session sess) {
    String sessId = sess.getId();
    return getUUID(sessId);
  }

  /**
   * 根据用户名获取UUID
   * @param username 用户名
   * @return 用户当前会话所用的UUID
   */
  public synchronized String getUUIDWithUser(String username) {
    for (Entry<String, String> entry : uuid2Username.entrySet()) {
      if (entry.getValue().equals(username)) {
        return entry.getKey();
      }
    }
    return null;
  }

  /**
   * 移除一个指定会话
   * 
   * @param sess 待移除的会话
   */
  public synchronized void removeSession(Session sess) {
    String uuid = getUUID(sess);
    if (null == uuid) {
      log.warn("未找到对应的连接：" + sess.getId(), uuid2SessionId);
    } else {
      log.info("移除用户ID -> 会话ID映射：" + uuid + " -> " + sess.getId());
      uuid2SessionId.remove(uuid);
      if (sessionId2Sess.containsKey(sess.getId())) {
        log.info("移除会话：" + sess.getId());
        sessionId2Sess.remove(sess.getId());
      } else {
        log.warn("未找到对应会话：" + sess.getId(), sessionId2Sess);
      }
      if (uuid2Username.containsKey(uuid)) {
        log.info("移除会话映射：" + uuid + " -> " + getUsername(uuid));
        uuid2Username.remove(uuid);
      } else {
        log.warn("未找到UUID与用户名的映射：" + uuid);
      }
    }
  }

  /**
   * 获取用户对应的websocket会话ID
   * 
   * @param uuid 用户UUID
   * @return 用户websocket会话ID
   */
  public String getSessId(String uuid) {
    if (uuid2SessionId.containsKey(uuid)) {
      return uuid2SessionId.get(uuid);
    } else {
      log.warn("未找到连接用户：" + uuid, uuid2SessionId);
    }
    return null;
  }

  /**
   * 根据用户ID删除关联的所有会话
   * 
   * @param uuid 用户UUID
   */
  public synchronized void removeSession(String uuid) {
    String sessId = getSessId(uuid);
    if (null != sessId) {
      log.info("移除用户ID -> 会话ID映射：" + uuid + " -> " + sessId);
      uuid2SessionId.remove(uuid);
      if (uuid2SessionId.containsKey(sessId)) {
        log.info("移除会话：" + sessId);
        uuid2SessionId.remove(sessId);
      } else {
        log.warn("未找到会话：" + sessId, uuid2SessionId);
      }
    }
    if (uuid2Username.containsKey(uuid)) {
      log.info("移除会话映射：" + uuid + " -> " + getUsername(uuid));
      uuid2Username.remove(uuid);
    } else {
      log.warn("未找到UUID与用户名的映射：" + uuid);
    }
  }

  /**
   * 获取所有在线用户名，并过滤掉指定的用户
   * @param uuid 待过滤用户
   * @return 过滤后的所有在线用户
   */
  public synchronized List<String> getAllUsernames(String... uuid) {
    List<String> names = new CopyOnWriteArrayList<>();
    for (String id : uuid2SessionId.keySet()) {
      for (String filterId : uuid) {
        if (filterId.equals(id)) {
          continue;
        }
      }
      names.add(uuid2Username.get(id));
    }
    return names;
  }
}
