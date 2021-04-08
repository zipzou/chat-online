package com.github.zipzou.chat.chatback.vo.res;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.zipzou.chat.chatback.vo.ValueObject;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class EmptyVo implements ValueObject {

  /**
   *
   */
  private static final long serialVersionUID = -7145093639550467120L;
  
}
