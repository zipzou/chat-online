package com.github.zipzou.chat.chatback;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.session.jdbc.config.annotation.web.http.EnableJdbcHttpSession;

@SpringBootApplication
@MapperScan("com.github.zipzou.chat.chatback.dao.mapper")
@EnableJdbcHttpSession
public class ChatBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatBackApplication.class, args);
	}

}
