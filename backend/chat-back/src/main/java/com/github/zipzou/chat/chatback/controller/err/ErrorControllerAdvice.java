package com.github.zipzou.chat.chatback.controller.err;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;

/**
 * 错误处理，用于转发404、500等错误
 */
@ControllerAdvice
public class ErrorControllerAdvice implements ErrorController {

  private static final String JAVAX_SERVLET_ERROR_STATUS_CODE = "javax.servlet.error.status_code";
  private @Autowired HttpServletRequest req;

  @Override

  public String getErrorPath() {
    int code = (Integer) req.getAttribute(JAVAX_SERVLET_ERROR_STATUS_CODE);
    if (400 <= code && 500 > code) {
      return "404";
    } else if (500 <= code && 600 > code) {
      return "500";
    } else {
      return "maintain";
    }
  }
}
