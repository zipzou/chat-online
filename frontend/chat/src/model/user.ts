export class User {
  static username: string
  static nickname: string
  static gender: number
  static uuid: string
}

export interface UserInfo {
  createDate: string,
  gender: number,
  id: number,
  nickname: string,
  userUUID: string,
  username: string
}
