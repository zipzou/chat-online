package com.github.zipzou.chat.chatback.beans;

import com.github.zipzou.chat.chatback.controller.UserController;
import com.github.zipzou.chat.chatback.controller.WesocketMiddleController;
import com.github.zipzou.chat.chatback.service.MessageService;
import com.github.zipzou.chat.chatback.service.SessionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebsocketServerConfig {
  
  /**
   * 创建Websocket支持
   * @return Websocket服务
   */
  @Bean
  public ServerEndpointExporter websocketServer() {
    return new ServerEndpointExporter();
  }

  @Autowired
  public void setUserControllerForWs(UserController userController) {
    WesocketMiddleController.setUserController(userController);
  }

  @Autowired
  public void setMessageService(MessageService mService) {
    WesocketMiddleController.setMsgSender(mService);
  }

  @Autowired
  public void setSessService(SessionService sessService) {
    WesocketMiddleController.setSessServ(sessService);
  }
}
