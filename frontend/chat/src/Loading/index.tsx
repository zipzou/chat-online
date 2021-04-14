import React from "react";
import { Constants } from "../constants";
import { PropsWithRoute } from "../PropsWithRoute";
import { ResponseBody } from "../ResponseBody";
import { AppStatus, checkStatus } from "../service/Status";

export class Loading extends React.Component<PropsWithRoute, unknown> {

  public async getSessId(): Promise<string> {
    try {
      let res = await fetch('http://127.0.0.1:8080/id')
      if (res.status !== 200) {
        return new Promise<string>((r, e) => {
          e("网络错误")
        })
      }
      let resBody: ResponseBody<string> = await res.json()
      if (200 === resBody.code) {
        return resBody.data as string
      } else {
        return new Promise<string>((resolve, reject) => {
          reject(resBody.reason)
        })
      }
    } catch(err) {
      console.log(err)
      return new Promise<string>((resolve, reject) => {
        reject("网络错误")
      })
    }
    
  }

  componentDidMount() {
    checkStatus().then((status: AppStatus) => {
      if (AppStatus.Unlogin === status) {
        this.props.history.replace('/login')
      } else if (AppStatus.Ready === status) {
        this.props.history.replace('/chat', {
          username: localStorage.getItem(Constants.UESR_USERNAME_KEY),
          uuid: localStorage.getItem(Constants.USER_UUID_KEY),
          nickname: localStorage.getItem(Constants.USER_NICKNAME_KEY),
          uid: Number.parseInt(localStorage.getItem(Constants.USER_USERID_KEY) as string)
        })
      } else {
        this.getSessId().then(accessToken => {
          localStorage.setItem(Constants.SESS_KEY, accessToken)
          this.props.history.replace('/login')
        })
        .catch(console.log)
      }
    })
  }

  render() {
    return <div>
      Loading page...
    </div>
  }
}