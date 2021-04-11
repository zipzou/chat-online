import { createHashHistory } from "history";
import React from "react";
import { Router } from "react-router";
import { Link } from "react-router-dom";
import { Constants } from "../constants";
import { PropsWithRoute } from "../PropsWithRoute";
import { ResponseBody } from "../ResponseBody";
import { AppStatus, checkStatus } from "../service/Status";

export class Loading extends React.Component<PropsWithRoute, unknown> {

  public async getSessId(): Promise<string> {
    let res = await fetch('http://127.0.0.1:8080/id')
    let resBody: ResponseBody<string> = await res.json()
    if (200 === resBody.code) {
      return resBody.data as string
    } else {
      return new Promise<string>((resolve, reject) => {
        reject(resBody.reason)
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