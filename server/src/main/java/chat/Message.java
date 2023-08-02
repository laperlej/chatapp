package chat;

class Message {
  String message;
  User sender;

  Message(User sender, String message) {
    this.message = message;
    this.sender = sender;
  }

  public String getText() {
    return this.message;
  }

  public String getJson() {
    return "{\"message\":\"" + this.message + "\",\"user\":\"" + this.sender.getName() + "\"}";
  }

  public User getSender() {
    return this.sender;
  }
}
