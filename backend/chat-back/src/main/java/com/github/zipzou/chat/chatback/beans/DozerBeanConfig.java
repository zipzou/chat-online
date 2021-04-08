package com.github.zipzou.chat.chatback.beans;

import org.dozer.spring.DozerBeanMapperFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

// @Component
public class DozerBeanConfig {
  @Bean
  public DozerBeanMapperFactoryBean dozerBean() {
    DozerBeanMapperFactoryBean facBean = new DozerBeanMapperFactoryBean();

    facBean.setMappingFiles(new Resource[] { new ClassPathResource("/dozer/user-map.xml")});

    return facBean;
  }
}
