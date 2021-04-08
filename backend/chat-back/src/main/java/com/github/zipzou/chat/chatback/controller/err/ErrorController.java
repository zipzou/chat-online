package com.github.zipzou.chat.chatback.controller.err;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/error")
public class ErrorController {
  
  /**
   * 
   * @param code
   */
  @RequestMapping("/{code}")
  public String toErrorPage(@PathVariable int code) {
    if (400 <= code && 500 > code) {
      return "404";
    } else if (500 <= code && 600 > code) {
      return "500";
    } else {
      return "maintain";
    }
  }
}
