package chat;

import java.io.IOException;

public class Main {
  public static void main(String[] args) throws IOException {

    ChatHandler chatHandler = new ChatHandler();

    WebSocket socket = new WebSocket("/chat", 8080, chatHandler);
    try {
      socket.listen();
    } catch (Exception e) {
      e.printStackTrace();
    }
    // ServerSocket serverSocket = new ServerSocket(8080);
    // Logger.getGlobal().info("Listening on port 8080...");
    //
    // ChatRoom chatRoom = new ChatRoom();
    // Logger.getGlobal().info("Chat room created...");
    //
    // while (true) {
    // Logger.getGlobal().info("Waiting for client...");
    // Socket client = serverSocket.accept();
    // Logger.getGlobal().info("Client connected...");
    // Thread thread = new Thread(new ClientHandler(client, chatRoom));
    // thread.start();
    // }
  }
}
