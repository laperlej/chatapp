package chat;

import java.io.IOException;

/** Represents a chat client. */
class User {
  Session session;
  String id;
  public String name;

  User(Session session) {
    this.id = session.getId();
    this.session = session;
    this.name = Utils.randomName();
  }

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public void sendMessage(Message message) throws IOException {
    // Send message to client
    this.session.sendMessage(message);
  }
}
