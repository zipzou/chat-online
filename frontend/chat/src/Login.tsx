import { Col, Input, Row, Form, Avatar, Button, message } from 'antd'
import React from 'react'

import { CodepenOutlined, KeyOutlined, UserOutlined } from '@ant-design/icons'

import 'antd/dist/antd.css'

import './assets/css/Login.css'

import male from './assets/img/male.svg'

import chat from './assets/img/chat.svg'
import groupChat from './assets/img/group-chat.svg'
import messageManage from './assets/img/group-chat.svg'

import { ResponseBody } from './ResponseBody'

import { User, UserInfo } from './model/user'
import { PropsWithRoute } from './PropsWithRoute'
import { AppStatus, checkStatus } from './service/Status'
import { Constants } from './constants'

interface LoginState {
  username?: string,
  password?: string,
  valCode?: string,
  codeUrl?: string,
  canLogin: boolean,
  valImage?: string,
}

interface LoginParam {
  username: string,
  password: string,
  valCode: string,
  accessToken: string,
}

interface LoginProps extends PropsWithRoute {

}

export default class LoginPage extends React.Component<LoginProps, LoginState> {

  constructor(prop:PropsWithRoute) {
    super(prop)
    this.state = {
      codeUrl: this.getCodeUrl(),
      canLogin: true,
    }
  }

  componentDidMount() {
    // super.componentDidMount()
    checkStatus().then((status: AppStatus) => {
      if (status === AppStatus.Uninitilized) {
        this.props.history.replace('/')
      } else if (status === AppStatus.Ready) {
        this.props.history.replace('/chat')
      } else {
        this.getValCode()
      }
    })
  }

  toSubmitLogin() {
    let param:LoginParam = {
      username: this.state.username!,
      password: this.state.password!,
      valCode: this.state.valCode!,
      accessToken: localStorage.getItem(Constants.SESS_KEY)!,
    }
    fetch('http://127.0.0.1:8080/user/login', {
      body: JSON.stringify(param),
      method: 'post',
      headers: {
        'content-type': 'application/json',
        'withCredentials': "same-origin",
      }
    }).then(res => res.json())
    .then((body: ResponseBody<UserInfo>)=> {
      if (200 === body.code) {
        let userData:UserInfo = body.data
        User.username = userData.username
        User.uuid = userData.userUUID
        localStorage.setItem(Constants.USER_UUID_KEY, userData.userUUID)
        localStorage.setItem(Constants.USER_NICKNAME_KEY, userData.nickname)
        localStorage.setItem(Constants.USER_USERID_KEY, userData.id + '')
        localStorage.setItem(Constants.UESR_USERNAME_KEY, userData.username)
        this.props.history.replace('/chat', {
          username: userData.username,
          uuid: userData.userUUID,
          nickname: userData.nickname,
          uid: userData.id
        })
      } else {
        message.error(body.message)
      }
      // this.props.hisotry
    })
    .catch(console.log)
  }

  getCodeUrl() {
    let baseUrl = 'http://127.0.0.1:8080/val/code'
    let timestamp = new Date().getTime()
    return `${baseUrl}?t=${timestamp}&ak=${localStorage.getItem('access_token')}`
  }

  getValCode() {
    fetch(this.getCodeUrl())
    .then(res => res.json())
    .then((data: ResponseBody<string>) => {
      if (200 === data.code) {
        this.setState({
          valImage: data.data as string
        })
      }
    })
    .catch(console.log)
  }

  render() {
    return <div className='container'>
      <Row gutter={[8, 8]}>
      <Col className='left-part' span={16}>
        <Row gutter={[16,16]}>
          <Col className='icon-g' span={12}>
            <img src={chat} className='chat-logo' alt='chat' />
            <p>一对一、点对点畅聊</p>
          </Col>
          <Col className='icon-g' span={12}>
            <img src={groupChat} className='chat-logo' alt='group-chat' />
            <p>一对多、畅快群聊</p>
          </Col>
          <Col className='icon-g' span={12} offset={6}>
            <img src={messageManage} className='chat-logo' alt='message'></img>
            <p>消息，轻松管理</p>
          </Col>
        </Row>
      </Col>
      <Col className='right-part' span={8}>
        <div className='form-group'>
          <Avatar icon={<img src={male} style={{width: 56, height: 56, textAlign: 'center', display: 'inline'}} alt='avator' />} size={88}className='icon-avator'  />
          <Form>
            <Form.Item
              rules={[{required: true, message: '请输入用户名'}]}
            >
              <Input prefix={<UserOutlined />} placeholder='输入用户名'
                size='large'
                name='username'
                onChange={(input) => {this.setState({username: input.currentTarget.value})}}
              ></Input>
            </Form.Item>
            <Form.Item
              rules={[{required: true, message: '请输入密码'}]}
            >
              <Input.Password prefix={<KeyOutlined />} size='large' placeholder='输入密码'
                name='pwd'
                onChange={(input) => {this.setState({password: input.currentTarget.value})}}
              ></Input.Password>
            </Form.Item>
            <Form.Item
              required={true}
              rules={[{required: true, message: '请输入密码'}]}
            >
              <Row gutter={8}>
                <Col span={20}>
                  <Input prefix={<CodepenOutlined />} size='large' placeholder='输入验证码'
                    name='val_code'
                    onChange={(input) => {this.setState({'valCode': input.currentTarget.value})}}
                  ></Input>
                </Col>
                <Col span={4}>
                  <img src={this.state.valImage} width="70px" height="35px" style={{marginLeft: '2.5px', marginTop: '2.5px'}} onClick={this.getValCode.bind(this)} alt='val_image' />
                </Col>
              </Row>
            </Form.Item>
            <Form.Item
              wrapperCol={
                {
                  offset: 11,
                }
              }
            >
              <Button title='登录' type='primary' htmlType='submit' size='large' onSubmit={this.toSubmitLogin.bind(this)} onClick={this.toSubmitLogin.bind(this)}
                disabled = {!this.state.canLogin}
              >登录</Button>
            </Form.Item>
            {/* <Form.Item>
              <Row gutter={8}>
                <Col>
                  <Button type='link'> <GithubFilled /> </Button>
                </Col>
                <Col>
                  <Button type='link'> <GithubFilled /> </Button>
                </Col> 
                <Col>
                  <Button type='link'> <GithubFilled /> </Button>
                </Col> 
                <Col>
                  <Button type='link'> <GithubFilled /> </Button>
                </Col>              
              </Row>
            </Form.Item> */}
            <Form.Item>
              <Button type='link' href='./register'>立即注册</Button>
            </Form.Item>
          </Form>
        </div>
      </Col>
    </Row>
    </div>
  }

}
