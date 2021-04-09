import { Constants } from "../constants";
import { ResponseBody } from "../ResponseBody";

/**
 * App状态
 */
export enum AppStatus {
  /**
   * 未经初始化，指未从服务器建立任何连接，无会话状态
   */
  Uninitilized,
  /**
   * 已初始化状态，但是未登录
   */
  Unlogin,
  /**
   * 已就绪
   */
  Ready,
}

/**
 * 
 */
interface CheckLoginStatusParam {
  accessToken: string,
  userUUID: string,
}

/**
 * 检查当前App属于何种状态
 * @returns 三种App状态
 */
export async function checkStatus(): Promise<AppStatus> {
  if (localStorage.getItem(Constants.SESS_KEY) === null) {
    return AppStatus.Uninitilized;
  } else if (localStorage.getItem(Constants.USER_UUID_KEY) === null) {
    return AppStatus.Unlogin
  } else {
    // 在线检查用户登录状态
    let param: CheckLoginStatusParam = {
      accessToken: localStorage.getItem(Constants.SESS_KEY)!,
      userUUID: localStorage.getItem(Constants.USER_UUID_KEY)!
    }
    let res = await fetch('http://127.0.0.1:8080/user/check', {
      method: 'post',
      body: JSON.stringify(param),
      headers: {
        'content-type': 'application/json'
      }
    })
    let body = await res.json() as ResponseBody<boolean>
    if (200 === body.code && body.data && body.success) {
      return AppStatus.Ready
    } else {
      return AppStatus.Unlogin
    }
  }
}
