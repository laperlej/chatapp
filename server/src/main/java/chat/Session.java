package chat;

import java.io.IOException;
import java.nio.channels.SocketChannel;

/** Represents a WebSocket session. */
class Session {
  SocketChannel channel;
  String id;
  public User user;

  Session(SocketChannel channel) {
    this.channel = channel;
    this.id = Utils.generateId();
    this.user = new User(this);
  }

  public String getId() {
    return this.id;
  }

  public void sendMessage(Message message) throws IOException {
    this.channel.write(Utils.encodeFrame(message.getJson()));
  }

  public boolean isOpen() {
    return this.channel.isConnected();
  }

  public boolean isClosed() {
    return !this.isOpen();
  }
}
