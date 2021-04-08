package com.github.zipzou.chat.chatback.controller;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import javax.imageio.ImageIO;

import com.github.zipzou.chat.chatback.service.SessionService;
import com.github.zipzou.chat.chatback.vo.ValueObject;
import com.github.zipzou.chat.chatback.vo.res.ResponseVo;
import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Controller
@RequestMapping("/val")
@CrossOrigin(origins = "*")
public class ValImageController {
  private @Autowired Producer producer;

  private @Autowired SessionService sessServ;

  @GetMapping("/code")
  @ResponseBody
  public ResponseVo getValImage(@RequestParam(required = true, value = "ak") String accessToken) throws IOException {
    String codeText = producer.createText();
    log.info("产生验证码：" + codeText);
    // sess.setAttribute(Constants.KAPTCHA_SESSION_KEY, codeText);
    sessServ.setAttribute(accessToken, Constants.KAPTCHA_SESSION_KEY, codeText);
    BufferedImage img = producer.createImage(codeText);
    ByteArrayOutputStream out = null;
    try {
      out = new ByteArrayOutputStream();
      ImageIO.write(img, "jpg", out);
      out.flush();
      String imgBased64 = Base64.getEncoder().encodeToString(out.toByteArray());
      return ResponseVo.success("获取成功", "data:image/jpg;base64," + imgBased64);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (null != out) {
        out.close();
      }
    }
    return ResponseVo.fail("无法产生验证码", "验证码服务异常");
  }

  @PostMapping("/check/{ak}/{code}")
  @ResponseBody
  public ResponseVo<ValueObject> validateCode(@PathVariable String code, @PathVariable("ak") String accessToken) {
    ResponseVo<String> val = (ResponseVo<String>) sessServ.getAttribute(accessToken, Constants.KAPTCHA_SESSION_KEY);
    sessServ.removeAttribute(accessToken, Constants.KAPTCHA_SESSION_KEY);
    if (null == val) {
      return ResponseVo.fail("无法完成验证", "验证码已过期，请重新获取验证码。");
    }
    String sessionCode = val.getData();

    if (sessionCode.equalsIgnoreCase(code)) {
      return ResponseVo.success("验证码验证成功！");
    } else {
      return ResponseVo.fail("无法完成验证", "验证码验证失败");
    }

  }
}
