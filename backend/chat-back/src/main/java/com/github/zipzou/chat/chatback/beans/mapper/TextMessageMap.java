package com.github.zipzou.chat.chatback.beans.mapper;

import com.github.zipzou.chat.chatback.dao.bean.SimpleMessageDto;
import com.github.zipzou.chat.chatback.vo.res.MessageToSendVo;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TextMessageMap {
  public SimpleMessageDto from(MessageToSendVo s);
  public MessageToSendVo from(SimpleMessageDto s);
}
