import { action, IObservableArray, observable } from 'mobx'
import { UserInfo } from '../../model/user'

import _ from 'lodash'

class OnlineUser {
  @observable private users: IObservableArray<UserInfo> = observable.array([])
  @action addUser(user: UserInfo) {
    // 查找是否已经存在
    let destUser = this.users.find(i => {
      return i.id === user.id && i.username === user.username
    })
    if (null !== destUser && undefined !== destUser) {
      Object.assign(destUser, user)
    } else {
      this.users.push(user)
    }
    
  }
  @action removeUser(uuid: string) {
    let toRemove = this.users.find(v => (v.userUUID === uuid))
    if (null !== toRemove && undefined !== toRemove) {
      this.users.remove(toRemove)
    }
  }

  @action clear() {
    this.users.clear()
  }

  @action get all() {
    return this.users
  }
}

export { OnlineUser }