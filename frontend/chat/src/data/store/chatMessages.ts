import { action, computed, IObservableArray, observable, ObservableMap } from "mobx";
import { MessageData } from "../../service/MessageService";

import _ from 'lodash'

// export interface MessageProps<T> {
//   /**
//    * 消息ID 
//    */
//   id:number;
//   /**
//    * 消息发送用户
//    */
//   from: string;
//   /**
//    * 消息接收用户
//    */
//    to: string;
//   /**
//    * 消息内容
//    */
//    content: string;
//   /**
//    * 如果发送的是文件，指明文件扩展名
//    */
//    extension?: string;
//   /**
//    * 如果发送的是文件，文件上传后存储的位置
//    */
//    fileUrl?: string;
//   /**
//    * 如果发送的是文件，文件类型
//    */
//    fileType?: string;

//   /**
//    * 已读状态
//    */
//    readStatus: boolean;

//   /**
//    * 消息发送日期
//    */
//   createDate: string;

//   /**
//    * 消息携带的额外数据
//    */
//    payload?: T;

//   /**
//    * 消息类型
//    */
//    type: MessageType;

//   /**
//    * 是否为接收消息
//    */
//    reciever: boolean;
// }

function isMessageProps(a: any): a is MessageData<any> {
  return 'id' in a
}

export class ChatMessages {
  @observable currentTarget?:string

  @observable messageMap:ObservableMap<string, 
  Array<MessageData<any>>> = observable.map({} as Map<string, Array<MessageData<any>>>)

  @computed get message(): Array<MessageData<any>> {
    if (null === this.currentTarget) {
      return [];
    }
    if (null === this.messageMap || undefined === this.messageMap) {
      return []
    }
    if (this.messageMap && this.messageMap.has(this.currentTarget!)) {
      return this.messageMap.get(this.currentTarget!) || []
    }
    return []
  }

  @action set setTarget(target:string) {
    this.currentTarget = target;
  }

  @action get target(): string|undefined {
    return this.currentTarget
  }

  @action addMessage(key:string, message:MessageData<any>) {
    if (!this.messageMap.has(key)) {
      this.messageMap.set(key, observable.array([message]))
    } else {
      this.messageMap.get(key)?.push(message)
    }
  }

  @action removeMessage(key:string, message: MessageData<any> | number) {
    if (this.messageMap.has(key)) {
      let messages = this.messageMap.get(key)
      let toDel = messages?.find(i => {
        if (isMessageProps(message)) {
          return i.id === message.id
        } else {
          return i.id === message
        }
      })
      if (null === toDel || undefined === toDel) {
        console.log('undefined to remove')
      }else {
        _.remove(messages || [], toDel)
      }
    }
  }

}