package chat;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

class ChatHandler implements Handler {
  ChatRoom chatRoom;
  Map<String, Session> sessions = new ConcurrentHashMap<String, Session>();

  ChatHandler() {
    this.chatRoom = new ChatRoom();
  }

  public void onOpen(Session session) {
    sessions.put(session.getId(), session);
    chatRoom.addUser(session.user);
  }

  public void onMessage(Session session, String message) {
    if (message == null) {
      return;
    }
    String trimedMessage = message.trim();
    if (trimedMessage.isEmpty()) {
      return;
    }
    Message msg = new Message(session.user, trimedMessage);
    chatRoom.sendMessage(msg);
  }

  public void onClose(Session session) {
    sessions.remove(session.getId());
  }

  public void onError(Session session, Exception e) {
    Logger.getGlobal().info("Error: " + e.getMessage());
    sessions.remove(session.getId());
  }
}
