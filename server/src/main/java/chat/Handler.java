package chat;

interface Handler {
  public void onOpen(Session session);

  public void onMessage(Session session, String message);

  public void onClose(Session session);

  public void onError(Session session, Exception e);
}
