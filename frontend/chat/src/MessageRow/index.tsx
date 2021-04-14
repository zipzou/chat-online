import { CheckCircleOutlined, CloudDownloadOutlined, FileExcelFilled, FileMarkdownFilled, FilePdfFilled, FilePptFilled, FileUnknownFilled, FileWordFilled, FileZipFilled, Html5Filled, MinusCircleOutlined } from "@ant-design/icons";
import { FileTxt, FileZip } from "@icon-park/react";
import { Image } from "antd";
import Avatar from "antd/lib/avatar/avatar";
import React from "react";
import { MessageType } from "../model/messagetype";
import { MessageData } from "../service/MessageService";

import './index.css'

export interface MessageRowProps {
  type: 'recieved' | 'send',
  content: string,
  time: string,
  nickname: string,
  key: number | string,
  data?: MessageData<any>
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
    if (this.props.data?.type === MessageType.Binary && !(/svg|jpg|jpeg|gif|png/.test(this.props.data.extension || ''))) {
      msgClass += ' file-msg'
    }
    return <div className='row-container row-left'>
    <div className='timestamp'>{this.props.time}</div>
    <div className='message-content'>
      <div style={{marginTop: 6}}>
        <Avatar size='large' style={{background: '#9C27B0'}}>{this.props.nickname[0].toUpperCase()}</Avatar>
      </div>
      <div className='user-meta-row'>
        <p className='user-nick'>{this.props.nickname}</p>
        <div className='msg-status'>
          <div className={msgClass}>{this.props.data?.type === MessageType.Plain ? this.props.content : this.renderFile()}</div>
          {/* {this.state.isRead ? <div className='read-status' color='#66BB6A'><CheckCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#66BB6A'}} />已读</div> : <div className='read-status' color='#455A64'><MinusCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#455A64'}} />未读</div>} */}
        </div>
      </div>
    </div>
  </div>
  }

  renderFile() {
    if (this.props.data?.type === MessageType.Binary) {
      let fileIcon: React.ReactNode = null
      let filename:string = '未知文件'
      let i = this.props.data.fileUrl?.lastIndexOf('/')
      if (i) {
        filename = this.props.data.fileUrl?.substring(i + 1)!
      }
      if (/svg|jpg|jpeg|gif|png/.test(this.props.data.extension || '')) {
        return <Image src={this.props.data?.fileUrl} className='image-inline' />
      } else if (/zip|tar|tar\.gz|rar|7z|tar|jar/.test(this.props.data.extension || '')) {
        
        fileIcon = <FileZipFilled style={{
          fontSize: 32,
          color: '#fff'
        }} />
      } else if (/txt/.test(this.props.data.extension || '')) {
        fileIcon = <FileTxt theme="filled" size="32" fill="#fff"/>
      } else if (/md/.test(this.props.data.extension || '')) {
        fileIcon = <FileMarkdownFilled 
          style={{
            fontSize: 32,
            color: '#fff'
          }}
        />
      } else if (/ppt|pptx/.test(this.props.data.extension || '')) {
        fileIcon = <FilePptFilled 
          style={{
            fontSize: 32,
            color: '#fff'
          }}
        />
      } else if (/doc|docx/.test(this.props.data.extension || '')) {
        fileIcon = <FileWordFilled className='file-icon' />
      } else if (/xls|xlsx/.test(this.props.data.extension || '')) {
        fileIcon = <FileExcelFilled className='file-icon' />
      } else if (/html|xlsx/.test(this.props.data.extension || '')) {
        fileIcon = <Html5Filled className='file-icon' />
      } else if (/pdf/.test(this.props.data.extension || '')) {
        fileIcon = <FilePdfFilled className='file-icon' />
      } else {
        fileIcon = <FileUnknownFilled className = 'file-icon' />
      }
      return <a target='_blank' href={this.props.data.fileUrl}><div className='file-inline'>
          <div className='file-header'>
            {fileIcon}
            <div className='file-ext'>{this.props.data.extension?.toUpperCase()}</div>
          </div>
          <div className='file-op-row'><a className='file-link'><p className='filename' title={filename!}>{filename}</p></a>
            <CloudDownloadOutlined
              className='download-i'
              style={{
                fontSize: 26,
                color: '#212121'
              }}
            />
          </div>
        </div></a>
    }
  }

  renderSend() {
    let msgClass = `message-par message-par-right`
    if (this.props.data?.type === MessageType.Binary && !(/svg|jpg|jpeg|gif|png/.test(this.props.data.extension || ''))) {
      msgClass += ' file-msg'
    }
    return <div className='row-container row-right'>
    <div className='timestamp'>{this.props.time}</div>
    <div className='message-content'>
      <div className='user-meta-row'>
        <p className='user-nick'>{this.props.nickname}</p>
        <div className='msg-status'>
          {/* {this.state.isRead ? <div className='read-status' color='#66BB6A'><CheckCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#66BB6A'}} />已读</div> : <div className='read-status' style={{color: '#455A64'}}><MinusCircleOutlined style={{fontSize: 8, paddingRight: 2, color: '#455A64'}} />未读</div>} */}
          <div className={msgClass}>{this.props.data?.type === MessageType.Plain ? this.props.content : this.renderFile()}</div>
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