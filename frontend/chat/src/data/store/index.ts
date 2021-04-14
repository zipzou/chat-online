import { ChatMessages } from "./chatMessages";
import { OnlineUser } from "./onlineUsers";

let onlineUsers = new OnlineUser()

let messages = new ChatMessages()

interface IStore {
  onlineUsers: OnlineUser,
  messages: ChatMessages
}

const store = {
  onlineUsers,
  messages
} as IStore

export { store }