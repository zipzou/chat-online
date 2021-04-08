package com.github.zipzou.chat.chatback.beans;

import java.util.Properties;

import com.google.code.kaptcha.impl.DefaultKaptcha;
import com.google.code.kaptcha.util.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KaptchaConfig {

  @Bean(name = "captchaProducer")
  public DefaultKaptcha getKaptchaBean() {
    DefaultKaptcha defaultKaptcha = new DefaultKaptcha();
    Properties properties = new Properties();
    // 验证码是否带边框 No
    properties.setProperty("kaptcha.border", "no");
    // 验证码字体颜色
    properties.setProperty("kaptcha.textproducer.font.color", "blue");
    // 验证码整体宽度
    properties.setProperty("kaptcha.image.width", "400");
    // 验证码整体高度
    properties.setProperty("kaptcha.image.height", "200");
    // 文字个数
    properties.setProperty("kaptcha.textproducer.char.length", "4");
    // 文字大小
    properties.setProperty("kaptcha.textproducer.font.size", "120");
    // 文字随机字体
    properties.setProperty("kaptcha.textproducer.font.names", "宋体,楷体,微软雅黑");
    // 文字距离
    properties.setProperty("kaptcha.textproducer.char.space", "16");
    // 干扰线颜色
    properties.setProperty("kaptcha.noise.color", "blue");
    // 自定义验证码样式
    // properties.setProperty("kaptcha.obscurificator.impl", "com.zhiyingwl.modules.imageCode.base.DisKaptchaCssImpl");
    // 自定义验证码背景
    // properties.setProperty("kaptcha.background.impl", "com.zhiyingwl.modules.imageCode.base.NoKaptchaBackhround");
    Config config = new Config(properties);
    defaultKaptcha.setConfig(config);
    return defaultKaptcha;
  }

}
