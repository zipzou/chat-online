package com.github.zipzou.chat.chatback.beans.mapper;

import com.github.zipzou.chat.chatback.dao.bean.SessionDto;
import com.github.zipzou.chat.chatback.vo.res.SessionVo;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SessionMap {
  public SessionVo from(SessionDto s);
  public SessionDto from(SessionVo s);
}
