package com.github.zipzou.chat.chatback.beans;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Configuration
public class JacksonConverterConfig {
  public HttpMessageConverters jacksonConvertor() {
    FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();
    // 2、添加fastJson 的配置信息，比如：是否要格式化返回的json数据;
    FastJsonConfig fastJsonConfig = new FastJsonConfig();
    fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
    // 3、在convert中添加配置信息.
    fastConverter.setFastJsonConfig(fastJsonConfig);
    HttpMessageConverter<?> converter = fastConverter;
    return new HttpMessageConverters(converter);
  }
}
