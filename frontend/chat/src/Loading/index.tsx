import React from "react";
import { MustLoginComponent } from "../base/AfterLogin";
import { Constants } from "../constants";
import { User } from "../model/user";
import { PropsWithRoute } from "../PropsWithRoute";
import { ResponseBody } from "../ResponseBody";
import { Session } from "../session";

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
    console.log('loading...')
    if (null === localStorage.getItem(Constants.SESS_KEY)) {
      this.getSessId().then((sessId: string) => {
        Session.sessionId = sessId
        localStorage.setItem(Constants.SESS_KEY, sessId)
        this.props.history.push('/login')
      })
      .catch(console.log)
    } else {
      if (localStorage.getItem(Constants.USER_UUID_KEY) === null) {
        this.props.history.push('/login')
      }
    }
  }

  render() {
    return <div>
      正在加载...
    </div>
  }
}