import React from "react";
import { PropsWithRoute } from "../PropsWithRoute";
import { AppStatus, checkStatus } from "../service/Status";

export abstract class MustLoginComponent<P extends PropsWithRoute, S = {}, SS = {}> extends React.Component<P, S, SS> {

  constructor(props: P) {
    super(props)
  }

  componentDidMount() {
    checkStatus().then((status) => {
      if (status === AppStatus.Uninitilized) {
        this.props.history.replace('/')
      } else if (status === AppStatus.Unlogin) {
        this.props.history.replace('/login')
      } else {
        this.props.history.replace('/chat')
      }
    })
  }

}