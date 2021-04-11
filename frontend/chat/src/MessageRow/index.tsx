import { CheckCircleOutlined, MinusCircleOutlined } from "@ant-design/icons";
import Avatar from "antd/lib/avatar/avatar";
import React from "react";

import './index.css'

export interface MessageRowProps {
  type: 'recieved' | 'send',
  content: string,
  time: string,
  nickname: string,
  key: number | string
}

export interface MessageRowState {
  isRead: boolean
}

export class MessageRow extends React.Component<MessageRowProps, MessageRowState> {

  constructor(props: MessageRowProps) {
    super(props)
    this.state = {
      isRead: false,
    }
  }

  renderRecieved() {
    let msgClass = `message-par message-par-left`
    return <div className='row-container row-left'>
    <div className='timestamp'>{this.props.time}</div>
    <div className='message-content'>
      <div style={{marginTop: 6}}>
        <Avatar size='large' style={{background: '#9C27B0'}}>{this.props.nickname[0].toUpperCase()}</Avatar>
      </div>
      <div className='user-meta-row'>
        <p className='user-nick'>{this.props.nickname}</p>
        <div className='msg-status'>
          <div className={msgClass}>{this.props.content}</div>
          {this.state.isRead ? <div className='read-status' color='#66BB6A'><CheckCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#66BB6A'}} />已读</div> : <div className='read-status' color='#455A64'><MinusCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#455A64'}} />未读</div>}
        </div>
      </div>
    </div>
  </div>
  }

  renderSend() {
    let msgClass = `message-par message-par-right`
    return <div className='row-container row-right'>
    <div className='timestamp'>{this.props.time}</div>
    <div className='message-content'>
      <div className='user-meta-row'>
        <p className='user-nick'>{this.props.nickname}</p>
        <div className='msg-status'>
          {this.state.isRead ? <div className='read-status' color='#66BB6A'><CheckCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#66BB6A'}} />已读</div> : <div className='read-status' style={{color: '#455A64'}}><MinusCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#455A64'}} />未读</div>}
          <div className={msgClass}>{this.props.content}</div>
        </div>
      </div>
      <div style={{marginTop: 6}}>
        <Avatar size='large' style={{background: '#673AB7'}}>{this.props.nickname[0].toUpperCase()}</Avatar>
      </div>
    </div>
  </div>
  }

  render() {
    let component = this.props.type === 'recieved' ? this.renderRecieved() : this.renderSend()
    return component
  }

}