# 在线聊天室 v2.0

在线聊天室v2.0在原有的基础上进一步扩展，结合使用**前后端分离技术**，使用[*React*]()和[*SpringBoot*]()用于创建前端与后端，具有更加稳定的效果。

聊天功能同样基于WebSocket协议，相比v1.0更加完善了在线列表，解决了原有聊天过程中，用户列表的错乱问题。

------------------------

## 环境配置

### 前端支持环境

前端使用React开发，因此需要基于NodeJs进行编译。

```:bash
git clone https://github.com/zipzou/chat-online.git

cd chat-online/frontend/chat && yarn install && yarn build 
```

### 后端支持环境

后端基于Java 1.8，若从源码编译，请事先安装配置Java以及对应的maven环境。

```:bash
git clone https://github.com/zipzou/chat-online.git

cd chat-online/backend/chat-back/ && mvn clean && mvn package
```

```:yml
spring: 
  datasource:
    url: jdbc:xxxx:3306/chat?
```

## 如何使用
