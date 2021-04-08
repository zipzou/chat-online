package com.github.zipzou.chat.chatback.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {
  

  @RequestMapping("/test")
  public String testError() throws Exception {
    throw new Exception("未知错误");
  }
}
