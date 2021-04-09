package com.github.zipzou.chat.chatback.controller;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import com.github.zipzou.chat.chatback.beans.mapper.UserMap;
import com.github.zipzou.chat.chatback.service.SessionService;
import com.github.zipzou.chat.chatback.service.UserService;
import com.github.zipzou.chat.chatback.vo.UserBasicVo;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.req.LoginBasicVo;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;
import com.github.zipzou.chat.chatback.vo.res.UserDetailVo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.log4j.Log4j2;

/**
 * 用户控制器，处理登录、注册、信息更新等请求
 */
@Log4j2
@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*")
public class UserController {
  
  private @Autowired UserService userServ;
  private @Autowired UserMap map;
  private @Autowired ValImageController valImageController;
  private @Autowired SessionService sessServ;

  @ResponseBody
  @GetMapping("/user_info/id/{id}")
  public ResponseVo<ValueObject> testUser(@PathVariable int id) {
    ValueObject user = userServ.getUser(id);
    return ResponseVo.success(user);
  }

  @PostMapping("/register")
  public ResponseVo<ValueObject> register(@RequestBody UserDetailVo user) {
    ValueObject result = userServ.createUser(user);
    if (result instanceof ResponseVo) {
      if (200 == ((ResponseVo) result).getCode()) { // 注册成功

        // 产生UUID
        String uuid = UUID.randomUUID().toString();
        // 添加session
        user = map.extend(((ResponseVo<UserBasicVo>) result).getData());
        user.setUserUUID(uuid);
        // sess.setAttribute(uuid, user.getUsername());
        sessServ.setAttribute(user.getAccessToken(), uuid, user.getUsername());

        return ResponseVo.success("注册成功", user);
      }
    }

    return (ResponseVo<ValueObject>) result;
  }

  private HttpServletRequest req;
  @PostMapping("/login")
  public ResponseVo<ValueObject> login(@RequestBody LoginBasicVo loginInfo) {
    // 先验证验证码
    ResponseVo<ValueObject> codeCheckResult = valImageController.validateCode(loginInfo.getValCode(), loginInfo.getAccessToken());
    if (200 != codeCheckResult.getCode()) {
      log.info(codeCheckResult);
      return ResponseVo.fail("登录失败", "验证码不匹配");
    }

    ValueObject checkResult = userServ.checkUser(loginInfo.getUsername(), loginInfo.getPassword());
    ResponseVo<ValueObject> result = ((ResponseVo<ValueObject>) checkResult);
    if (200 == result.getCode()) {
      ValueObject userVal = userServ.getUser(loginInfo.getUsername());
      UserBasicVo user = ((ResponseVo<UserBasicVo>) userVal).getData();
      String userUUID = UUID.randomUUID().toString();
      user.setUserUUID(userUUID);
      // sess.setAttribute(userUUID, loginInfo.getUsername());
      sessServ.setAttribute(loginInfo.getAccessToken(), userUUID, loginInfo.getUsername());
      return ResponseVo.success("登录成功", user);
    }
    return ResponseVo.fail("登录失败", "用户名密码不匹配");
  }

  
  /**
   * 检查用户登录状态
   * @param basic 用户基本信息
   * @return 是否已经登录，并创建会话
   */
  @PostMapping("/check")
  public ValueObject checkStatus(@RequestBody UserBasicVo basic) {

    ValueObject res = sessServ.getAttribute(basic.getAccessToken(), basic.getUserUUID());
    log.info(res);
    ResponseVo<String> status = (ResponseVo<String>) res;
    if (200 == status.getCode() && status.isSuccess() && status.getData() != null) {
      return ResponseVo.success(true);
    } else {
      return ResponseVo.success(false);
    }
  }
}