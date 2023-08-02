package chat;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

class ChatRoom {
  Map<String, User> users;
  RingBuffer<Message> messages;

  ChatRoom() {
    this.users = new ConcurrentHashMap<String, User>();
    this.messages = new RingBuffer<Message>(2000);
  }

  public void addUser(User user) {
    this.users.put(user.getId(), user);
  }

  public void sendMessage(Message message) {
    User sender = message.getSender();
    Logger.getGlobal().info("Message from " + sender.name + ": " + message.getText());
    synchronized (this.messages) {
      this.messages.add(message);
    }
    this.broadcast(message);
  }

  private void broadcast(Message message) {
    for (User user : this.users.values()) {
      try {
        user.sendMessage(message);
      } catch (IOException e) {
        Logger.getGlobal().info("Error: " + e.getMessage());
      }
    }
  }
}
