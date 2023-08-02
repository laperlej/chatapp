package chat;

import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Random;
import java.util.UUID;

class Utils {
  private static final String MAGIC_NUMBER = "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";

  public static String generateId() {
    return UUID.randomUUID().toString();
  }

  public static String randomName() {
    ArrayList<String> names = new ArrayList<String>();
    names.add("John");
    names.add("Mary");
    names.add("James");
    names.add("Patricia");
    names.add("Robert");

    Random rand = new Random();
    return names.get(rand.nextInt(names.size()));
  }

  public static byte[] sha1(String string) throws NoSuchAlgorithmException {
    MessageDigest digest;
    digest = MessageDigest.getInstance("SHA-1");
    return digest.digest(string.getBytes());
  }

  public static String base64(byte[] bytes) {
    return Base64.getEncoder().encodeToString(bytes);
  }

  public static String encode(String secWebSocketKey) throws NoSuchAlgorithmException {
    String key = secWebSocketKey + MAGIC_NUMBER;
    return Utils.base64(Utils.sha1(key));
  }

  public static String decodeFrame(ByteBuffer buffer) {
    if (buffer.limit() <= 0) {
      return null;
    }

    byte firstByte = buffer.get();
    byte secondByte = buffer.get();

    byte fin = (byte) ((firstByte & 128) >> 7);
    byte rsv1 = (byte) ((firstByte & 64) >> 6);
    byte rsv2 = (byte) ((firstByte & 32) >> 5);
    byte rsv3 = (byte) ((firstByte & 16) >> 4);

    byte opCode = (byte) ((firstByte & 8) | (firstByte & 4) | (firstByte & 2) | (firstByte & 1));

    byte mask = (byte) ((secondByte & 128) >> 7);
    long length =
        (long)
            ((secondByte & 64)
                | (secondByte & 32)
                | (secondByte & 16)
                | (secondByte & 8)
                | (secondByte & 4)
                | (secondByte & 2)
                | (secondByte & 1));

    if (length == 126) {
      length = buffer.getShort();
    } else if (length == 127) {
      length = buffer.getLong();
    }

    byte[] dataBytes = new byte[(int) length];

    byte[] maskValue;
    if (mask == 1) {
      maskValue = new byte[4];
      buffer.get(maskValue);

      for (int i = 0; i < length; i++) {
        byte data = buffer.get();
        dataBytes[i] = (byte) (data ^ maskValue[i % 4]);
      }
    }
    return new String(dataBytes);
  }

  public static ByteBuffer encodeFrame(String message) {
    // create a WebSocket frame
    // https://tools.ietf.org/html/rfc6455#section-5.2
    byte[] bytes = message.getBytes();
    int length = bytes.length;

    byte fin = 1;
    byte rsv1 = 0;
    byte rsv2 = 0;
    byte rsv3 = 0;
    byte opcode = 1;

    byte firstByte = 0;
    firstByte = (byte) (firstByte | fin << 7); // fin
    firstByte = (byte) (firstByte | rsv1 << 6); // rsv1
    firstByte = (byte) (firstByte | rsv2 << 5); // rsv2
    firstByte = (byte) (firstByte | rsv3 << 4); // rsv3
    firstByte = (byte) (firstByte | opcode); // opcode

    byte secondByte = 0;
    secondByte = (byte) (secondByte | 0 << 7); // mask
    secondByte = (byte) (secondByte | length); // length

    ByteBuffer buffer = ByteBuffer.allocate(2048);
    buffer.put(firstByte);
    buffer.put(secondByte);
    buffer.put(bytes);
    buffer.flip();
    return buffer;
  }
}
