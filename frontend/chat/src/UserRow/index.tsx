import { Mail } from "@icon-park/react";
import Avatar from "antd/lib/avatar/avatar";
import React, { MouseEventHandler } from "react";
import { UserInfo } from "../model/user";


import './index.css'

export interface UserRowProps extends UserInfo {
  onClick?: MouseEventHandler<HTMLDivElement>
}

export interface UserRowState {

}

export class UserRow extends React.Component<UserRowProps, UserRowState> {
  render() {
    return  <>
    <div onClick={this.props.onClick} className='online-user-row' onSelect={(e) => {e.preventDefault()}}>
      <Avatar
        size='large'
        className='user-avatar'
      >
        {this.props.nickname[0].toUpperCase()}
      </Avatar>
      <div className='username'>{this.props.nickname}</div>
      <div className='user-status-row online'><span className="user-status-badge"></span>在线</div>
    </div>
    </>
  }
}