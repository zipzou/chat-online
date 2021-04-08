package com.github.zipzou.chat.chatback.controller;

import javax.servlet.http.HttpSession;

import com.github.zipzou.chat.chatback.beans.mapper.SessionMap;
import com.github.zipzou.chat.chatback.service.SessionId;
import com.github.zipzou.chat.chatback.service.SessionService;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;
import com.github.zipzou.chat.chatback.vo.res.SessionVo;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@CrossOrigin(origins = {"http://127.0.0.1:3000", "http://localhost:3000"}, methods = {RequestMethod.POST, RequestMethod.GET}, allowCredentials = "true", allowedHeaders = {"Content-Type","Content-Length","Authorization","Accept", "X-Requested-With"})
public class SessionController {
  
  private @Autowired SessionService sessionServ;
  private @Autowired SessionMap map;

  @GetMapping("/id")
  public ValueObject createSessionId() {
    SessionVo sessionItem = new SessionVo();
    DateTime now = DateTime.now();
    sessionItem.setCreatedTime(now.toDate());
    sessionItem.setExpiredTime(now.plusHours(12).toDate());
    String sessionId = SessionId.generateId();
    sessionItem.setSessionId(sessionId);

    ValueObject result = sessionServ.newSession(map.from(sessionItem));

    if (200 == ((ResponseVo) result).getCode()) {
      log.info("session id: " + sessionId);
      return ResponseVo.success("ID生成成功", sessionId);
    } else {
      return ResponseVo.fail(((ResponseVo) result).getReason());
    }

  }

}
