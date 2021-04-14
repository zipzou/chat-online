import { Constants } from "../constants";
import { MessageType } from "../model/messagetype";

interface MessageReq {
  /**
   * 发送者UUID
   */
  from: string;
  /**
   * 接收用户UUID
   */
  to: string;
  /**
   * 消息内容
   */
  content: string;
  /**
   * 消息类型
   */
  type: MessageType;
  
  /**
   * 会话ID
   */
  accessToken: string,
}

export interface MessageData<T> {
  /**
   * 消息ID
   */
  id: number;
  /**
   * 消息发送用户
   */
  from: string;
  /**
   * 消息接收用户
   */
  to: string;
  /**
   * 消息内容
   */
  content?: string;
  /**
   * 如果发送的是文件，指明文件扩展名
   */
  extension?: string;
  /**
   * 如果发送的是文件，文件上传后存储的位置
   */
  fileUrl?: string;
  /**
   * 如果发送的是文件，文件类型
   */
  fileType?: string;

  /**
   * 已读状态
   */
   readStatus?: boolean;

  /**
   * 消息发送日期
   */
  createDate?: string;

  /**
   * 消息携带的额外数据
   */
  payload?: T;

  /**
   * 消息类型
   */
  type?: MessageType;

  /**
   * 是否为接收消息
   */
   reciever?: boolean;
}

export class WsConnection {

  private session: WebSocket;

  constructor(open: (e: Event) => void | any | any, message: (e: MessageEvent) => void | any | any, close: (e: CloseEvent) => void | any | any) {
    let accessToken = localStorage.getItem(Constants.SESS_KEY)
    let uuid = localStorage.getItem(Constants.USER_UUID_KEY)
    this.session = new WebSocket(`ws://127.0.0.1:8080/chat/${uuid}/${accessToken}`)
    this.session.onopen = open
    this.session.onmessage = message
    this.session.onclose = close
    this.session.onerror = console.log
  }

  /**
   * 关闭会话
   */
  close() {
    this.session.close()
  }

  /**
   * 发送消息
   * @param content 消息内容 
   * @param from 消息发送方
   * @param to 消息接收方
   * @param token 会话令牌
   */
  sendText(content: string, from: string, to: string, token: string) {
    let message:MessageReq = {
      content, from, to, accessToken: token,
      type: MessageType.Plain
    }
    this.session.send(JSON.stringify(message))
  }

}