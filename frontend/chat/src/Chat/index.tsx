import { ExportOutlined, MessageTwoTone, SettingTwoTone, TeamOutlined } from "@ant-design/icons";
import { FolderUpload } from "@icon-park/react";
import { Avatar, Button, Col, Input, List, message, Row } from "antd";
import { inject, observer } from 'mobx-react';
import React from "react";
import EmptySvg from '../assets/img/popular.svg';
import { Constants } from "../constants";
import { ChatMessages } from "../data/store/chatMessages";
import { OnlineUser } from "../data/store/onlineUsers";
import { MessageRow } from "../MessageRow";
import { MessageType } from "../model/messagetype";
import { UserInfo } from "../model/user";
import { PropsWithRoute } from "../PropsWithRoute";
import { ResponseBody } from "../ResponseBody";
import { MessageData, WsConnection } from "../service/MessageService";
import { AppStatus, checkStatus } from "../service/Status";
import { UserRow } from "../UserRow";
import './index.css';

interface ChatPageProps extends PropsWithRoute {
  username: string,
  nickname: string,
  uuid: string,
  onlineUsers?: OnlineUser,
  messages?: ChatMessages
}

export interface ChatPageState {
  username: string,
  nickname: string,
  uid: number,
  uuid: string,
  online?: boolean,
  // chatTarget?: string,
  inputContent?: string,
  tabIndex: number,
  onlineUsers?: OnlineUser,
  ready: boolean,
  chatWith?: UserInfo
}

@inject('onlineUsers', 'messages')
@observer
class Chat extends React.Component<ChatPageProps, ChatPageState> {

  private ws?: WsConnection
  messageList?: HTMLDivElement
  file_uploader?: HTMLInputElement

  constructor(props: ChatPageProps) {
    super(props)
    if (null === localStorage.getItem(Constants.SESS_KEY)) {
      props.history.replace('/')
      return
    }
    if (null === localStorage.getItem(Constants.UESR_USERNAME_KEY) || null === localStorage.getItem(Constants.USER_UUID_KEY) || null === localStorage.getItem(Constants.USER_USERID_KEY) || null === localStorage.getItem(Constants.USER_NICKNAME_KEY)) {
      props.history.replace('/login')
      return
    }
    // let onlines = [{
    //   username: 'test',
    //   nickname: 'test',
    //   userUUID: 'test',
    //   id: 1,
    //   gender: 0,
    //   createDate: '2020-03-22'
    // }, {
    //   username: 'zip',
    //   nickname: 'zip',
    //   userUUID: 'zip',
    //   id: 2,
    //   gender: 0,
    //   createDate: '2020-03-22'
    // }]
    this.state = {
      username: localStorage.getItem(Constants.UESR_USERNAME_KEY)!,
      nickname: localStorage.getItem(Constants.USER_NICKNAME_KEY)!,
      uid: Number.parseInt(localStorage.getItem(Constants.USER_USERID_KEY)!),
      uuid: localStorage.getItem(Constants.USER_UUID_KEY)!,
      online: false,
      tabIndex: 2,
      ready: false,
    }
  }

  componentDidMount() {
    checkStatus().then((status: AppStatus) => {
      if (AppStatus.Uninitilized === status) {
        this.props.history.replace('/')
        return;
      } else if (AppStatus.Unlogin === status) {
        this.props.history.replace('/login')
        return;
      } else {
        this.connectWs()
      }
    })

    
  }

  componentDidUpdate() {
    if (null !== this.messageList && undefined !== this.messageList) {
      // this.messageList.scrollIntoView({behavior: 'smooth'})
      this.messageList.scrollTop = this.messageList.scrollHeight
    }
  }

  componentWillUnmount() {
    this.ws?.close()
  }

  connectWs() {
    this.ws = new WsConnection((e) => {
      this.setState({
        online: true,
      })
      window.onbeforeunload = (e: BeforeUnloadEvent) => {
          let confirmationMessage = '如果离开将断开连接，是否继续?';
          (e || window.event).returnValue = confirmationMessage;
          return confirmationMessage;
      }

    this.setState({
      ready: true,
    })
    
    }, (e: MessageEvent) => {
      let payload: MessageData<any> = JSON.parse(e.data)
      switch (payload.type) {
        case MessageType.OnlineNotify:
          let userToOnline:UserInfo = payload.payload
          this.props.onlineUsers?.addUser(userToOnline)
          break;
        case MessageType.OfflineNotify:
          let userToOffline: UserInfo = payload.payload
          this.props.onlineUsers?.removeUser(userToOffline.userUUID)
          if (this.state.chatWith?.userUUID === userToOffline.userUUID) {
            this.setState({
              chatWith: undefined
            })
          }
          break
        case MessageType.OnlineList:
          let users = payload.payload?.filter((user: UserInfo) => {
            return user.userUUID !== this.state.uuid
          })
          users?.forEach((user: UserInfo) => {
            this.props.onlineUsers?.addUser(user)
          })
          break
        case MessageType.HistoryMessage:
          let historyMessages: Array<MessageData<any>> | undefined = payload.payload
          if (undefined === historyMessages || null === historyMessages) {
            return;
          }
          historyMessages.forEach(msg => {
            if (msg.to === this.state.username && msg.from !== this.state.username) {
              msg.reciever = true
              let key = `${this.state.username}-${msg.from}`
              this.props.messages?.addMessage(key, msg)
            } else if (msg.to !== this.state.username && msg.from === this.state.username) {
              msg.reciever = false
              let key = `${this.state.username}-${msg.to}`.trim()
              this.props.messages?.addMessage(key, msg)
            }
            
          })
          break;
        case MessageType.Plain:
        case MessageType.Binary:
          if (payload.to === this.state.username) {
            payload.reciever = true
          }
          this.props.messages?.addMessage(`${payload.to}-${payload.from}`, payload)
          this.setState({'inputContent': ''})
          if (null !== this.messageList && undefined !== this.messageList) {
            this.messageList!.scrollTop = this.messageList?.scrollHeight
          }
          break;
        default:
          break;
      }
    }, (e) => {
      this.setState({
        online: false,
      })
      console.log(e)
    })
  }

  sendMessage(content: string) {
    if (!this.state.online) {
      message.error('当前处于离线状态无法发送消息')
      return;
    }
    if (content === '') {
      return;
    }
    let chatKey = `${this.state.username}-${this.state.chatWith?.username}`
    let now = new Date()
    let messageBody:MessageData<any> = {
      id: new Date().getTime(),
      createDate: `${now.getFullYear()}-${now.getMonth()}-${now.getDate()} ${now.getHours()}-${now.getMinutes()}`,
      content: content,
      from: this.state.uuid,
      to: this.state.chatWith?.userUUID!,
      readStatus: false,
      type: MessageType.Plain,
      reciever: false,
    }
    this.props.messages?.addMessage(chatKey, messageBody)
    this.setState({
      inputContent: ''
    })
    this.ws?.sendText(content, this.state.uuid, this.state.chatWith?.userUUID!, localStorage.getItem(Constants.SESS_KEY)!)
  }

  countOnline() {

  }

  renderChildren() {
    if (this.state.tabIndex === 1) {
      return !this.state.chatWith ? <div className='placeholder-chat'>
                <img src={EmptySvg} alt='empty-placeholder' width='40%' />
                <p>暂无消息</p>
              </div> : (<><div className='chat-header'>
                <p className='chat-title'>与 {this.state?.chatWith?.nickname} 聊天中... </p>
              </div>
              <div className='message-area'>
                <div className='message-list' ref={el => {this.messageList = el!}}><List 
                  split={false}
                  rowKey = {msg => (msg.id + '')}
                  dataSource={this.props.messages?.message || []}
                  renderItem={message => {
                    return <List.Item>
                      <MessageRow type={message.reciever ? 'recieved': 'send'} nickname={message.reciever ? this.state.chatWith?.nickname! : this.state.nickname} content={message.content!} time={message.createDate!} key={message.id}
                        data = {message}
                      />
                    </List.Item>
                  }}
                 />
                 </div>
              </div>
              <div className='input-area'>
                <div className='operation-area'>
                  <a className='op-item' title='发送文件' onClick={() => {
                    if (null !== this.file_uploader && undefined !== this.file_uploader) {
                      this.file_uploader.click()
                    }
                  }}><FolderUpload theme="outline" size="24" strokeLinejoin="miter" strokeLinecap="butt" className=''/></a>
                </div>
                <input type='file' hidden={true} name='file' ref={e => {this.file_uploader = e!}} multiple={false} onChange={e => {
                  let data:FormData = new FormData()
                  if (e.currentTarget.files && 0 < e.currentTarget.files.length) {
                    data.append('file', e.currentTarget.files[0])
                    data.append('accessToken', localStorage.getItem(Constants.SESS_KEY)!)
                    data.append('from', this.state.uuid)
                    data.append('to', this.state.chatWith?.userUUID!)
                    fetch('http://127.0.0.1:8080/file/upload', {
                      method: 'POST',
                      body: data
                    }).then(res => res.json())
                    .then((res: ResponseBody<MessageData<any>>) => {
                      e.target.outerHTML = e.target.outerHTML
                      if (200 === res.code && res.success) {
                        let fileMsg = res.data
                        this.props.messages?.addMessage(`${this.state.username}-${fileMsg.to}`, fileMsg)
                        this.setState({
                          inputContent: ''
                        })
                      } else {
                        message.error('文件发送失败：' + res.message)
                      }
                    })
                    .catch(console.log)
                  }
                }} />
                <Input.TextArea className='message-input' value={this.state.inputContent} bordered={false} placeholder='输入您的消息' onPressEnter={(e) => { e.preventDefault(); this.sendMessage(this.state.inputContent || '') }} onChange={e => {
                  this.setState({
                    inputContent: e.currentTarget.value
                  })
                }} rows={5}>

                </Input.TextArea>
                <Button type='primary' htmlType='submit' className='btn-send' onClick={() => {
                  this.sendMessage(this.state.inputContent || '')
                }}>发送</Button>
              </div></>)
    } else if (this.state.tabIndex === 2) {
      
      return <div style={{
        width: '70%',
        margin: '0 auto',
        padding: '16px 4px'
      }}>

        <h2 className='online-list-title'>在线列表({this.props.onlineUsers?.all?.length})</h2>
        <List
          dataSource = {this.props.onlineUsers?.all}
          renderItem = {user => {
            return <>
              <List.Item
                key={user.id}
              >
                <UserRow {...user} onClick={() => {
                  this.setState({
                    chatWith: user,
                    tabIndex: 1,
                  })
                  this.props.messages!.setTarget = `${this.state.username}-${user.username}`
                }} />
              </List.Item>
            </>
          }}
         />
      </div>
    }
    return null
  }

  render() {
    
    return this.state?.ready ? <div style={{ overflow: 'hidden', paddingTop: 120 }}>
      {/* <Prompt 
        when={true}
        message = {() => ('如果离开将断开连接，是否继续?')}
      /> */}
      <div className='container'>
        <Row className='chat-area'>
          <Col span={3} className='left-nav'>
            <div className='user-area'>
              <Avatar size='large' className='avator'>
                {this.state.nickname?.toUpperCase()[0]}
              </Avatar>
              <div className="status-wrapper" onClick={!this.state.online ? this.connectWs.bind(this) : () => {}}>
                <div className='status-badge' style={{ background: this.state.online ? '#4CAF50' : '#F44336' }}></div>
                <div style={{
                  display: 'inline-block',
                  color: this.state.online ? '#4CAF50' : '#F44336' 
                }}>{this.state.online ? '在线' : '当前离线'}</div>
              </div>
            </div>
            <ul className='col-nav'>
              <li className='nav-item' onClick={() => {this.setState({tabIndex: 1})}}>
                <div className='item-group'>
                  <div><MessageTwoTone style={{ fontSize: 28 }} twoToneColor={this.state.tabIndex === 1 ? ['#1976D2', 'transparent'] : ['#2e2e2e', 'transparent'] } /></div>
                  <div style={{ fontSize: 16, color: this.state.tabIndex === 1 ? '#1976D2': '#000000' }}>消息</div>
                </div>
              </li>
              <li className='nav-item' onClick={() => {this.setState({tabIndex: 2})}}>
                <div className='item-group'>
                  <div><TeamOutlined style={{ fontSize: 28, color: this.state.tabIndex === 2 ? '#1976D2' : '#2e2e2e' }} /></div>
                  <div style={{ fontSize: 16, color: this.state.tabIndex === 2 ? '#1976D2' : '#000000' }}>好友</div>
                </div>
              </li>
            </ul>
            <ul className='col-nav bottom-nav'>
              <li className='nav-item'>
                <div className='item-group'>
                  <div><SettingTwoTone style={{ fontSize: 28, color: '#2e2e2e' }} twoToneColor={['#2e2e2e', 'transparent']} /></div>
                </div>
              </li>
              <li className='nav-item' onClick={() => {
                this.setState({
                  online: false,
                  chatWith: undefined,
                })
                this.props.onlineUsers?.clear()
                this.ws?.close()
                fetch('http://127.0.0.1:8080/user/logout', {
                  method: 'POST',
                  body: JSON.stringify({
                    accessToken: localStorage.getItem(Constants.SESS_KEY)
                  }),
                  headers: {
                    'Content-Type': 'application/json;charset=UTF-8'
                  }
                }).then(res => res.json())
                .then((res: ResponseBody<undefined>) => {
                  if (res && 200 === res.code && res.success) {
                    localStorage.clear()
                    this.props.history.replace('/')
                  }
                })
                .catch(console.log)
                
              }}>
                <div className='item-group'>
                  <div><ExportOutlined style={{ fontSize: 28, color: '#2e2e2e' }} twoToneColor={['#2e2e2e', 'transparent']} /></div>
                  <div>退出登录</div>
                </div>
              </li>
            </ul>
          </Col>
          <Col span={21}>
            <div className='right-chat'>
              {this.renderChildren()}
            </div>
          </Col>
        </Row>
      </div>
    </div> : <></>
  }
}

export { Chat };
