package chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Logger;

class WebSocket {
  private String path;
  private InetSocketAddress port;
  private Handler handler;
  private ServerSocketChannel serverSocketChannel;

  WebSocket(String path, int port, Handler handler) {
    this.path = path;
    this.port = new InetSocketAddress(port);
    this.handler = handler;
  }

  private void bind() throws IOException {
    this.serverSocketChannel = ServerSocketChannel.open();
    this.serverSocketChannel.configureBlocking(false);
    this.serverSocketChannel.bind(this.port);
    Logger.getGlobal().info("Listening on port " + this.port.getPort() + "...");
  }

  private void accept(SelectionKey key) throws IOException {
    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
    SocketChannel socketChannel = serverSocketChannel.accept();
    if (socketChannel != null) {
      socketChannel.configureBlocking(false);
      socketChannel.register(key.selector(), SelectionKey.OP_READ);
    }
  }

  public void listen() throws IOException, NoSuchAlgorithmException {
    this.bind();
    Selector selector = Selector.open();

    this.serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    while (true) {
      int select = selector.select(1);
      for (SelectionKey key : selector.selectedKeys()) {
        if (key.isAcceptable()) {
          this.accept(key);
        }
        if (key.isReadable()) {
          SocketChannel socketChannel = (SocketChannel) key.channel();
          ByteBuffer buffer = ByteBuffer.allocate(2048);
          int count = socketChannel.read(buffer);
          buffer.flip();

          String string = new String(buffer.array());
          if (string.startsWith("GET " + this.path)) {
            this.handshake(string, socketChannel);
            Session session = new Session(socketChannel);
            key.attach(session);
            this.handler.onOpen(session);
            continue;
          }

          String text = Utils.decodeFrame(buffer);
          if (text != null) {
            Session session = (Session) key.attachment();
            this.handler.onMessage(session, text);
          }
        }
      }
    }
  }

  private void handshake(String string, SocketChannel socketChannel)
      throws IOException, NoSuchAlgorithmException {
    int swkIndex = string.indexOf("Sec-WebSocket-Key:");
    int endIndex = string.indexOf("\r\n", swkIndex);
    String swk = string.substring(swkIndex + 19, endIndex);

    String acceptKey = Utils.encode(swk);

    String response =
        "HTTP/1.1 101 Switching Protocols\r\n"
            + "Upgrade: websocket\r\n"
            + "Connection: Upgrade\r\n"
            + "Sec-WebSocket-Accept: "
            + acceptKey
            + "\r\n\r\n";
    socketChannel.write(ByteBuffer.wrap(response.getBytes()));
  }
}
