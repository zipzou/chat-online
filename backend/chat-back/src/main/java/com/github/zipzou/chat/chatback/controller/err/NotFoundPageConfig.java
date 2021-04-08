package com.github.zipzou.chat.chatback.controller.err;

import org.springframework.boot.web.server.ErrorPage;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.boot.web.server.ErrorPageRegistry;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * 配置错误页面
 */
@Component
public class NotFoundPageConfig implements ErrorPageRegistrar {

  @Override
  public void registerErrorPages(ErrorPageRegistry registry) {
    ErrorPage notFoundPage = new ErrorPage(HttpStatus.NOT_FOUND, "/error/404");
    ErrorPage notFoundPage2 = new ErrorPage(HttpStatus.BAD_REQUEST, "/error/404");
    ErrorPage notFoundPage3 = new ErrorPage(HttpStatus.UNAUTHORIZED, "/error/404");
    registry.addErrorPages(notFoundPage, notFoundPage2, notFoundPage3, new ErrorPage(HttpStatus.INTERNAL_SERVER_ERROR, "/error/500"), new ErrorPage(HttpStatus.BAD_GATEWAY, "/error/500"));
  }
  
}
