import React from "react";
import { MustLoginComponent } from "../base/AfterLogin";
import { PropsWithRoute } from "../PropsWithRoute";
import { AppStatus, checkStatus } from "../service/Status";

export class Chat extends React.Component<PropsWithRoute, any> {

  componentDidMount() {

    checkStatus().then((status: AppStatus) => {
      if (AppStatus.Uninitilized === status) {
        this.props.history.replace('/')
      } else if (AppStatus.Unlogin === status) {
        this.props.history.replace('/login')
      }
    })

  }

  render() {
    return <div>
      Hello chat-online!
    </div>
  }
}