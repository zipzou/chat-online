import { ExportOutlined, MessageOutlined, MessageTwoTone, SettingTwoTone, TeamOutlined, UserOutlined } from "@ant-design/icons";
import { Avatar, Button, Col, Image, Input, List, message, Row } from "antd";
import React from "react";
import { Constants } from "../constants";
import { MessageRow } from "../MessageRow";
import { PropsWithRoute } from "../PropsWithRoute";
import { AppStatus, checkStatus } from "../service/Status";

import EmptySvg from '../assets/img/popular.svg'

import './index.css'
import { WebsocketSession } from "../service/WebsocketSession";
import { MessageData, WsConnection } from "../service/MessageService";
import { UserInfo } from "../model/user";
import { MessageType } from "../model/messagetype";
import { UserRow } from "../UserRow";

interface ChatPageProps extends PropsWithRoute {
  username: string,
  nickname: string,
  uuid: string,
}

export interface ChatPageState {
  username: string,
  nickname: string,
  uid: number,
  uuid: string,
  online?: boolean,
  chatTarget?: string,
  inputContent?: string,
  tabIndex: number,
  onlineUsers: Array<UserInfo>,
  ready: boolean,
  chatWith?: UserInfo
}

export class Chat extends React.Component<ChatPageProps, ChatPageState> {

  private ws?: WsConnection

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
    let onlines = [{
      username: 'test',
      nickname: 'test',
      userUUID: 'test',
      id: 1,
      gender: 0,
      createDate: '2020-03-22'
    }, {
      username: 'zip',
      nickname: 'zip',
      userUUID: 'zip',
      id: 2,
      gender: 0,
      createDate: '2020-03-22'
    }]
    this.state = {
      username: localStorage.getItem(Constants.UESR_USERNAME_KEY)!,
      nickname: localStorage.getItem(Constants.USER_NICKNAME_KEY)!,
      uid: Number.parseInt(localStorage.getItem(Constants.USER_USERID_KEY)!),
      uuid: localStorage.getItem(Constants.USER_UUID_KEY)!,
      online: false,
      tabIndex: 2,
      ready: false,
      onlineUsers: onlines,
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
      let payload: MessageData<Array<UserInfo>> = JSON.parse(e.data)
      switch (payload.type) {
        case MessageType.OnlineList:
          console.log(payload)
          break
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
    if (content === '') {
      return;
    }
    this.setState({
      inputContent: ''
    })
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
                <List className='message-list' split={false}>
                  <List.Item>
                    <MessageRow type='recieved' nickname='Pony' content='哈哈哈哈哈' time='2020-01-20 12:40' key={1} />
                  </List.Item>
                  <List.Item>
                    <MessageRow type='send' nickname='Pony' content='哈哈哈哈哈' time='2020-01-20 12:40' key={2} />
                  </List.Item>
                </List>
              </div>
              <div className='input-area'>
                <div className='operation-area'>
                </div>
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

        <h2 className='online-list-title'>在线列表({this.state.onlineUsers.length})</h2>
        <List
          dataSource = {this.state.onlineUsers}
          renderItem = {user => {
            console.log(user)
            return <>
              <List.Item
                key={user.id}
              >
                <UserRow {...user} onClick={() => {
                  this.setState({
                    chatWith: user,
                    tabIndex: 1,
                  })
                }} />
              </List.Item>
            </>
          }}
        >
          
        </List>
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
              <li className='nav-item'>
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